/***************************************************************************
 *   Copyright (C) 2005 by                                                 *
 *   Sebastian Gebhardt                                                    *
 *    <sebastian_DOT_gebhardt_AT_informatik_DOT_uni-oldenburg_DOT_de>      *
 *   Andre van Hoorn                                                       *
 *    <andre_DOT_van_DOT_hoorn_AT_informatik_DOT_uni-oldenburg_DOT_de>     *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

/*
 * Created on 27.05.2005
 */
package atfrogs.servlets;

//java util and io classes
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Logger;

//servlet stuff
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//utils, etc.
import atfrogs.ox.api.OXApi;
import atfrogs.ox.api.OXApiException;
import atfrogs.ox.api.OXScriptApi;
import atfrogs.ox.api.OXScriptApiException;
import atfrogs.ox.objects.OXGroup;
import atfrogs.ox.util.OXUserComparator;
import atfrogs.util.ServletUtils;
import atfrogs.util.StringIgoreCaseComparator;



/**
 * This servlet contains the logic for group administration. 
 */
public class ATFrogsGroupServlet extends HttpServlet{
	//for the logger
	private String LOGGER     = "ATFrogs_logger";
	private Logger msgLogger  = null;
	
	//strings for the paths to the scripts
	private static String ADDGROUP_SCRIPT_PATH         = null;
	private static String DELGROUP_SCRIPT_PATH         = null;
	private static String ADDUSERTOGROUP_SCRIPT_PATH   = null;
	private static String DELUSERFROMGROUP_SCRIPT_PATH = null;

	// loaded from session on each request
	private static ResourceBundle messageBundle = null;
	
	/**
	 * The init method initializes the pathes to the scripts.
	 * 
	 * @param conf the configuration for the servlet.
	 * @throws ServletException if initialization fails.
	 */
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		
		//get the paths to the scripts from the web.xml
		ADDGROUP_SCRIPT_PATH = this.getInitParameter("addgroup_script");
		if(ADDGROUP_SCRIPT_PATH == null)
			throw new ServletException("path to addgroup script not set");
		
		DELGROUP_SCRIPT_PATH = this.getInitParameter("delgroup_script");
		if(DELGROUP_SCRIPT_PATH == null)
			throw new ServletException("path to delgroup script not set");
		
		ADDUSERTOGROUP_SCRIPT_PATH = this.getInitParameter("addusertogroup_script");
		if(ADDUSERTOGROUP_SCRIPT_PATH == null)
			throw new ServletException("path to addusertogroup script not set");
		
		DELUSERFROMGROUP_SCRIPT_PATH = this.getInitParameter("deluserfromgroup_script");
		if(DELUSERFROMGROUP_SCRIPT_PATH == null)
			throw new ServletException("path to deluserfromgroup script not set");
		
		//init the logger
		msgLogger = Logger.getLogger(LOGGER);
		if (msgLogger == null){
			throw new ServletException("cannot initiate the logger");
		}
	}	
	
	/**
	 * This method handles all group related requests.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String action       = null; //for navigating and actions
		
		//catches ServletException and forwards to the error jsp
		try {
			//force pages to be reloaded each time
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			
			// load messageBundle. May throw ServletException 
			messageBundle = 
				ServletUtils.getMessageBundleFromSession(request, response);
			
			//read action parameter
			action = ServletUtils.getStringAttribute("action", request, response);

			if (action.equals("groupDetail")){
				//show the group details
				this.groupDetail(request, response);
			} else if (action.equals("delGroup")){
				//delete group
				this.groupDelete(request, response);
			} else if (action.equals("newGroup")){
				//dialog for creating a new group (field to enter name/gid)
				ServletUtils.forwardTo(
						"/groups/newGroup.jsp", request, response);
			} else if (action.equals("neu")){
				//actually create group
				this.groupCreate(request, response);
			} else {
				//unknown action, go to the start page for groups
				this.groupsShowAll(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//set the error message and forward to the error jsp
			this.msgLogger.warning("EXCEPTION: " + e.toString());
			ServletUtils.forwardToErrorPage(request, response, 
					messageBundle.getString(
							"ATFrogsGroupServlet.MsgErrorOccured"), null);
		}	
	}

	/**
	 * Handle creation of new group.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void groupCreate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String gid            = null;
		OXScriptApi scriptApi = null;
		List scriptOutput     = null; //holds script output

		//read group id to set as gid of new group
		gid = ServletUtils.getStringAttribute("group", request, response);
		
		//run script
	    try {
	    	scriptApi = new OXScriptApi();
		    scriptOutput = scriptApi.addGroup(gid, ADDGROUP_SCRIPT_PATH);
			request.setAttribute("stdOutList", scriptOutput);
            this.msgLogger.info(ServletUtils.getUserFromSession(request) 
            		+ " executed newgroup script with group " + gid);
		} catch (OXScriptApiException e){
			this.msgLogger.warning("EXCEPTION: " + e.toString());
			ServletUtils.setErrorMsg(e.toString(), request);
			request.setAttribute("group", gid);
			ServletUtils.forwardTo("/groups/newGroup.jsp", request, response);
			return;
		}
		
		//go to the group detail page, set the group
		request.setAttribute("group", gid);
		this.groupDetail(request, response);
	}
	
	/**
	 * Handle deletion of group.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void groupDelete(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException{
		String gid            = null;
		OXScriptApi scriptApi = null;
		String confirmString = null; // holds passed parameter value of confirm
		List scriptOutput    = null; // holds script output
		
		//read gid of group to delete
		gid = ServletUtils.getStringAttribute("group", request, response);
		
		confirmString = ServletUtils.getStringAttribute("confirm", request, response);
		
		if(!confirmString.equals("true")){
			ServletUtils.forwardTo("/groups/confirmGroupDel.jsp", request, response);
			return;
		}
		
		//run script, removing is confirmed
		try {
			scriptApi = new OXScriptApi();
			scriptOutput = scriptApi.delGroup(gid, DELGROUP_SCRIPT_PATH);
			request.setAttribute("stdOutList", scriptOutput);
            this.msgLogger.info(ServletUtils.getUserFromSession(request) 
            		+ " executed deletegroup script with group " + gid);
		} catch (OXScriptApiException e){
			this.msgLogger.warning("EXCEPTION: " + e.toString());
			ServletUtils.setErrorMsg(e.toString(), request);
		}
		
		//go to group page
		this.groupsShowAll(request, response);
	}

	/**
	 * Handles request for group details. 
	 * Here the requested action (send button) is evaluated and executed. 
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void groupDetail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		//clicked buttons from the jsp
		boolean doExtract = false;  //true if the extract button is clicked
		boolean doSearch  = false;  //true if the search button is clicked
		boolean doAdd     = false;  //true if the add button is clicked
		boolean doRemove  = false;  //true if the remove button is clicked
		
		//data passed to the jsp
		OXGroup oxGroup    = null;   //holds the current group and its members
		List groupIdList   = null;   //list of all existing group ids. 
		List searchResult  = null;   //users, found by extraction or search

		//data from the jsp
		String searchPattern     = null;  //the search pattern
		String gid               = null;  //the current group
		String selectedresults[] = null;  //selected members of the current group
		String searchresults[]   = null;  //selected members of the searchResult
		String lastSearch        = null;  //to remember that a search was executed
		String lastExtract       = null;  //to remember that a group was extracted
		String[] groupsToExtract = null;  //gids of groups to extract
		
		//data from the session
		OXApi oxApi              = null;  //to get the api
		OXScriptApi scriptApi    = null;  //scripts
		
		//get data from the jsp
		groupsToExtract = request.getParameterValues("groupselect");
		searchPattern   = (String)request.getParameter("searchpattern"); //ServletUtils.getAttribute can not be used
		lastSearch      = request.getParameter("lastSearch");  //ServletUtils.getAttribute can not be used
		lastExtract     = request.getParameter("lastExtract"); //ServletUtils.getAttribute can not be used
		gid             = ServletUtils.getStringAttribute("group", request, response);
		selectedresults = request.getParameterValues("selectedresults"); //for multi value fields
		searchresults   = request.getParameterValues("searchresults");   //for multi value fields
		doExtract       = request.getParameter("action-extract") != null;
		doSearch        = request.getParameter("action-search")  != null;
		doAdd           = request.getParameter("action-add")     != null;
		doRemove        = request.getParameter("action-remove")  != null;
		
		List tmpOutput    = null;
		List scriptOutput = null;
		
		
		//the logic for adding/removing
		if ((doAdd == true) && (searchresults != null)) {
			//add user
			try {
				scriptApi = new OXScriptApi();
				
				//iterate over the selected users
				for (int i = 0; i < searchresults.length; i ++){
					tmpOutput = scriptApi.addUserToGroup(searchresults[i], 
										       gid, 
											   ADDUSERTOGROUP_SCRIPT_PATH);
					if (scriptOutput == null){
						scriptOutput = tmpOutput;
					} else {
						scriptOutput.addAll(tmpOutput);
					}
	                this.msgLogger.info(ServletUtils.getUserFromSession(request) + 
	                		" executed addusertogroup script with user " + searchresults[i] 
						    + " and group " + gid);
				}
				request.setAttribute("stdOutList", scriptOutput);
			} catch (OXScriptApiException e){
				this.msgLogger.warning("EXCEPTION: " + e.toString());
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsGroupServlet.MsgErrorExecutingScript"), request);
			}
		} else if ((doRemove == true) && (selectedresults != null)) {
			//del user
			try {
				scriptApi = new OXScriptApi();
				
				//iterate over the selected users
				for (int i = 0; i < selectedresults.length; i ++){
					tmpOutput = scriptApi.delUserFromGroup(selectedresults[i], 
												 gid, 
												 DELUSERFROMGROUP_SCRIPT_PATH);
					if (scriptOutput == null){
						scriptOutput = tmpOutput;
					} else {
						scriptOutput.addAll(tmpOutput);
					}
	                this.msgLogger.info(ServletUtils.getUserFromSession(request) + 
	                		" executed deleteuserfromgroup script with user " + selectedresults[i]
							+ " and group " + gid);
				}
				request.setAttribute("stdOutList", scriptOutput);
			} catch (OXScriptApiException e){
				this.msgLogger.warning("EXCEPTION: " + e.toString());
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsGroupServlet.MsgErrorExecutingScript"), request);
			}
		}
		

		
		//get the ox api and the selected group
		try{
			oxApi   = ServletUtils.getOXApiFromSession(request, response);
			oxGroup = oxApi.getGroup(gid);
			
			//pass the group to the jsp
			if(oxGroup != null){
				request.setAttribute("group", oxGroup);	 // sorting of members will be done in jsp!
			}else{
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsGroupServlet.MsgErrorReadingParticipants"), 
						request);
			}	
		} catch (OXApiException e){
			this.msgLogger.warning("EXCEPTION: " + e.toString());
			ServletUtils.setErrorMsg(e.toString(), request);

			//go to the groups start page
			this.groupsShowAll(request, response);
			return;
		}
		
		
		
		//the logic for group extraction/ member search
		searchResult = new Vector();
		if((doExtract) && (groupsToExtract != null)){
			//extract group(s)
			List lastExtractedGroups = new Vector();
			try{
				searchResult.addAll(oxApi.getGroupsMembers(groupsToExtract));
					
				// inform jsp about last extracted groups
				for(int i=0; i < groupsToExtract.length; i++){
					lastExtractedGroups.add(groupsToExtract[i]);
				}
				request.setAttribute("lastExtract", "true"); // must be String != null, acts as flag
				request.setAttribute("lastExtractedGroups", lastExtractedGroups);
			}catch(OXApiException exc){
				this.msgLogger.warning("EXCEPTION: " + exc.toString());
				//goto to detail page anyway
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsGroupServlet.MsgErrorOnExtractingGroup"), request);
			}
		}else if((doSearch) && (searchPattern != null)){
			//search
			try{
				searchResult.addAll(oxApi.getUsersByPattern(searchPattern));
				request.setAttribute("lastSearch", searchPattern);
			}catch(OXApiException exc){
				this.msgLogger.warning("EXCEPTION: " + exc.toString());
				// goto to detail page anyway
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsGroupServlet.MsgErrorOnSearching"), request);
			}
		} else {
			//no search or extract clicked (try last one)
			if (lastSearch != null){
				//get last searchlist, if existent
				try{
					searchResult.addAll(oxApi.getUsersByPattern(lastSearch));
					request.setAttribute("lastSearch", lastSearch);
				}catch(OXApiException exc){
					this.msgLogger.warning("EXCEPTION: " + exc.toString());
					//go to detail page anyway
					ServletUtils.setErrorMsg(messageBundle.getString(
							"ATFrogsGroupServlet.MsgErrorOnSearching"), request);
				}
			} else if (lastExtract != null){
				List lastExtractedGroups = new Vector();
				
				if(groupsToExtract != null){
					try{
						searchResult.addAll(oxApi.getGroupsMembers(groupsToExtract));
						
						// inform jsp about last extracted groups
						for(int i=0; i < groupsToExtract.length; i++){
							lastExtractedGroups.add(groupsToExtract[i]);
						}
						request.setAttribute("lastExtract", "true"); // must be String != null, acts as flag
						request.setAttribute("lastExtractedGroups", lastExtractedGroups);
					}catch(OXApiException exc){
						this.msgLogger.warning("EXCEPTION: " + exc.toString());
						//go to detail page anyway
						ServletUtils.setErrorMsg(messageBundle.getString(
								"ATFrogsGroupServlet.MsgErrorReadingGroups"), request);
					}
				}
			}
		}
		Collections.sort(searchResult, 
					     new OXUserComparator(OXUserComparator.FIRSTNAME_SURNAME));
		request.setAttribute("searchResultList", searchResult);
		
		//TODO: check whether an error occured before
		
		try{
			groupIdList = oxApi.getGroupIdList();
			if(groupIdList != null){
				Collections.sort(groupIdList, new StringIgoreCaseComparator());
				request.setAttribute("allGroupIds", groupIdList);				
			}else{
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsGroupServlet.MsgErrorReadingGroups"), request);
				//will go to detail page anyway
			}			
			ServletUtils.forwardTo("/groups/groupDetails.jsp", request, response);
		}catch(OXApiException exc){
			this.msgLogger.warning("EXCEPTION: " + exc.toString());
			// goto to detail page anyway
			ServletUtils.setErrorMsg(messageBundle.getString(
					"ATFrogsGroupServlet.MsgErrorReadingGroups"), request);
			this.groupsShowAll(request, response);
		}
	}
	
	/**
	 * This function gets a list of all groups, puts them into the request and forwards
	 * to the group start page.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void groupsShowAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{ 
		List groupIdList   = null;  //for all groups 
		OXApi oxApi        = null;  //for the oxapi
		
		try{
			oxApi       = ServletUtils.getOXApiFromSession(request, response);
			groupIdList = oxApi.getGroupIdList();
			
			//check whether retrieving of groups was successful
			if(groupIdList != null){
				Collections.sort(groupIdList, new StringIgoreCaseComparator());
				//pass the groups to the jsp
				request.setAttribute("groupIdList", groupIdList);				
			}else{
				ServletUtils.setErrorMsg(messageBundle.getString("ATFrogsGroupServlet.MsgErrorReadingGroups"), request);
			}
			
			//forward to the group start page
			ServletUtils.forwardTo("/groups/allGroups.jsp", request, response);
		}catch(OXApiException e){
			e.printStackTrace();
			this.msgLogger.warning("EXCEPTION: " + e.toString());
			this.msgLogger.warning("NOTE: Error often due to wrong authentification. \n " +
					"Check username and password of user specified in web.xml and assert " +
					"that the user is in at least one group!");			
			ServletUtils.forwardToErrorPage(request, response, 
							messageBundle.getString(
							    "ATFrogsGroupServlet.MsgErrorReadingGroups"),
							null);
		}
	}
	
	/**
	 * We do not distinguish between GET- and POST-requests. 
	 * This method simply calls doGet with passed parameters.
	 * 
	 * @param req the current request.
	 * @param resp the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs. 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		this.doGet(req, resp);
	}
}
