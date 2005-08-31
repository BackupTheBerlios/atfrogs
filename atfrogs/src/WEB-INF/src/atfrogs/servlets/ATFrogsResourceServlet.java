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
import java.util.*;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atfrogs.ox.api.OXApi;
import atfrogs.ox.api.OXApiException;
import atfrogs.ox.api.OXScriptApi;
import atfrogs.ox.api.OXScriptApiException;
import atfrogs.ox.objects.OXResourceGroup;
import atfrogs.ox.util.OXResourceComparator;
import atfrogs.util.ServletUtils;
import atfrogs.util.StringIgoreCaseComparator;


/**
 * This servlet contains the logic for resource administration. 
 */
public class ATFrogsResourceServlet extends HttpServlet{
	//for the logger
	private String LOGGER     = "ATFrogs_logger"; 
	private Logger msgLogger  = null;
	
	//strings for the paths to the scripts
	private static String ADDRESOURCEGROUP_SCRIPT_PATH      = null;
	private static String ADDRESOURCE_SCRIPT_PATH           = null;
	private static String ADDRESOURCETOGROUP_SCRIPT_PATH    = null;
	private static String DELRESOURCEFROMGROUP_SCRIPT_PATH  = null;
	private static String DELRESOURCEGROUP_SCRIPT_PATH      = null;
	private static String DELRESOURCE_SCRIPT_PATH           = null;

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
		
		String debugString = null;
		
		//get the paths to the scripts from the web.xml
		ADDRESOURCEGROUP_SCRIPT_PATH = this.getInitParameter(
				"addresourcegroup_script_path"); 
		if(ADDRESOURCEGROUP_SCRIPT_PATH == null)
			throw new ServletException(
					"path to addresourcegroup script not set"); 
		
		ADDRESOURCE_SCRIPT_PATH = this.getInitParameter(
				"addresource_script_path"); 
		if(ADDRESOURCE_SCRIPT_PATH == null)
			throw new ServletException(
					"path to addresource script not set"); 
		
		ADDRESOURCETOGROUP_SCRIPT_PATH = this.getInitParameter(
				"addresourcetogroup_script_path"); 
		if(ADDRESOURCETOGROUP_SCRIPT_PATH == null)
			throw new ServletException(
					"path to addresourcetogroup script not set"); 
		
		DELRESOURCEFROMGROUP_SCRIPT_PATH  = this.getInitParameter(
				"delresourcefromgroup_script_path"); 
		if(DELRESOURCEFROMGROUP_SCRIPT_PATH == null)
			throw new ServletException(
					"path to delresourcefromgroup script not set"); 
		
		DELRESOURCEGROUP_SCRIPT_PATH = this.getInitParameter(
				"delresourcegroup_script_path"); 
		if(DELRESOURCEGROUP_SCRIPT_PATH == null)
			throw new ServletException(
					"path to delresourcegroup script not set"); 
		
		DELRESOURCE_SCRIPT_PATH = this.getInitParameter(
				"delresource_script_path"); 
		if(DELRESOURCE_SCRIPT_PATH == null)
			throw new ServletException(
					"path to delresourcegroup script not set"); 
		
		//init the logger
		msgLogger = Logger.getLogger(LOGGER);
		if (msgLogger == null){
			throw new ServletException("cannot initiate the logger"); 
		}
	}	
	
	/**
	 * This function handles all requests.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String action            = null; //for navigating and actions
		
		//catches ServletException and forwards to the error jsp
		try {
			//force pages to be reloaded each time
			response.setHeader("Cache-Control", "no-cache");  
			response.setHeader("Pragma", "no-cache");  
			
			// load messageBundle. May throw ServletException 
			messageBundle = ServletUtils.getMessageBundleFromSession(request, response);
			
			//read action parameter
			action = ServletUtils.getStringAttribute("action", request, response); 
			
			if (action.equals("new")){ 
				//dialog for creating a resource group (field to enter name/gid)
				ServletUtils.forwardTo("/resources/new.jsp", request, response); 
			} else if (action.equals("neu")){ 
				//create resource group or resource
				this.newResOrResGroup(request, response);
			} else if (action.equals("allRes")){ 
				//show all res
				this.resShowAll(request, response);
			} else if (action.equals("allResGroups")){ 
				//show all res
				this.resGroupsShowAll(request, response);
			} else if (action.equals("delResGroup")){ 
				//delete the current res group
				this.resGroupDelete(request, response);
			} else if (action.equals("resGroupDetail")){ 
				//show details for the selected res group
				this.resGroupDetail(request, response);
			} else if (action.equals("delRes")){
				//delete selected res
				this.resDelete(request, response);
			} else {
				//unknown action, go to the start page for resources
				this.resGroupsShowAll(request, response);
			}
		} catch (Exception e) {
			//set the error message and forward to the error jsp
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			ServletUtils.forwardToErrorPage(request, response, 
					messageBundle.getString(
							"ATFrogsResourceServlet.MsgErrorOccured"), null); 
		}		
	}
	
	/**
	 * This method gets the member of the current resource group and forwards
	 * to the resGroupDetails-jsp.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void resGroupDetail(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		//clicked buttons from the jsp
		boolean doExtract = false;  //true if the extract button is clicked
		boolean doSearch  = false;  //true if the search button is clicked
		boolean doAdd     = false;  //true if the add button is clicked
		boolean doRemove  = false;  //true if the remove button is clicked
		
		//data passed to the jsp
		OXResourceGroup oxResGroup = null;   //holds the current group and its members
		List resGroupIdList        = null;   //list of all existing group ids. 
		List searchResult          = null;   //users, found by extraction or search
		
		//data from the jsp
		String searchPattern        = null;  //the search pattern
		String resGid               = null;  //the current group
		String selectedresults[]    = null;  //selected members of the current group
		String searchresults[]      = null;  //selected members of the searchResult
		String lastSearch           = null;  //to remember that a search was executed
		String lastExtract          = null;  //to remember that a group was extracted
		String[] resGroupsToExtract = null;  //gids of groups to extract
		
		//data from the session
		OXApi oxApi              = null;  //to get the api
		OXScriptApi scriptApi    = null;  //scripts
		
		
		//get data from the jsp
		resGroupsToExtract = request.getParameterValues("resgroupselect"); 
		searchPattern      = (String)request.getParameter("searchpattern"); //ServletUtils.getAttribute can not be used 
		lastSearch         = request.getParameter("lastSearch");  //ServletUtils.getAttribute can not be used 
		lastExtract        = request.getParameter("lastExtract"); //ServletUtils.getAttribute can not be used 
		resGid             = ServletUtils.getStringAttribute("resgroup", request, response); 
		selectedresults    = request.getParameterValues("selectedresults"); //for multi value fields 
		searchresults      = request.getParameterValues("searchresults");   //for multi value fields 
		doExtract          = request.getParameter("action-extract") != null; 
		doSearch           = request.getParameter("action-search")  != null; 
		doAdd              = request.getParameter("action-add")     != null; 
		doRemove           = request.getParameter("action-remove")  != null; 
		
		List scriptOutput  = null; //hold the script output
		List tmpOutput     = null;
		
		//the logic for adding/removing
		if ((doAdd == true) && (searchresults != null)) {
			//add resource
			try {
				scriptApi = new OXScriptApi();
				
				//iterate over the selected resources
				for (int i = 0; i < searchresults.length; i ++){
					tmpOutput = scriptApi.addResToResGroup(searchresults[i], resGid, 
							    ADDRESOURCETOGROUP_SCRIPT_PATH);
					if (scriptOutput == null){
						scriptOutput = tmpOutput;
					} else {
						scriptOutput.addAll(tmpOutput);
					}
					this.msgLogger.info(ServletUtils.getUserFromSession(request)
							+ " executed addresourcetogroup script for resource " + searchresults[i] 
					        + " and group " + resGid);  
				}
				request.setAttribute("stdOutList", scriptOutput);
			} catch (OXScriptApiException e){
				this.msgLogger.warning("EXCEPTION: " + e.toString()); 
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsResourceServlet.MsgErrorExecutingScript"), request);
			}
		} else if ((doRemove == true) && (selectedresults != null)) {
			//remove resource from group
			try {
				scriptApi = new OXScriptApi();
				
				//iterate over the selected resources
				for (int i = 0; i < selectedresults.length; i ++){
					tmpOutput = scriptApi.delResFromResGroup(selectedresults[i], resGid, 
							DELRESOURCEFROMGROUP_SCRIPT_PATH);
					if (scriptOutput == null){
						scriptOutput = tmpOutput;
					} else {
						scriptOutput.addAll(tmpOutput);
					}
					this.msgLogger.info(ServletUtils.getUserFromSession(request) 
							+ " executed deleteresourcefromgroup script with resource " + selectedresults[i] 
							+ " and group " + resGid);  
				}
				request.setAttribute("stdOutList", scriptOutput);
			} catch (OXScriptApiException e){
				this.msgLogger.warning("EXCEPTION: " + e.toString()); 
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsResourceServlet.MsgErrorExecutingScript"), request);
			}
		}
		
		//get the ox api and the selected res groups
		try{
			oxApi      = ServletUtils.getOXApiFromSession(request, response);
			oxResGroup = oxApi.getResGroup(resGid);
			
			//pass the group to the jsp
			if(oxResGroup != null){
				request.setAttribute("resGroup", oxResGroup); 
			}else{
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsResourceServlet.MsgErrorReadingGroupResources"),  
						request);
			}	
		} catch (OXApiException e){
			e.printStackTrace();
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			ServletUtils.setErrorMsg(messageBundle.getString(
					"ATFrogsResourceServlet.MsgErrorLoadingOXApi"),  
					request);
			
			//go to the groups start page
			this.resGroupsShowAll(request, response);
			return;
		}
		
		//the logic for resgroup extraction/ member search
		searchResult = new Vector();
		if((doExtract) && (resGroupsToExtract != null)){
			//extract resgroup(s)
			List lastExtractedResGroups = new Vector();
			try{
				searchResult.addAll(oxApi.getResGroupsResources(resGroupsToExtract));
				
				// inform jsp about last extracted groups
				for(int i=0; i < resGroupsToExtract.length; i++){
					lastExtractedResGroups.add(resGroupsToExtract[i]);
				}
				request.setAttribute("lastExtract", "true"); // must be String != null, acts as flag  
				request.setAttribute("lastExtractedResGroups", lastExtractedResGroups); 
			}catch(OXApiException exc){
				exc.printStackTrace();
				this.msgLogger.warning("EXCEPTION: " + exc.toString()); 
				//goto to detail page anyway
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsResourceServlet.MsgErrorOnExtractingResGroup"),
						request); 
			}
		}else if((doSearch) && (searchPattern != null)){
			//search
			try{
				searchResult.addAll(oxApi.getResourcesByPattern(searchPattern));
				request.setAttribute("lastSearch", searchPattern); 
			}catch(OXApiException exc){
				this.msgLogger.warning("EXCEPTION: " + exc.toString()); 
				// goto to detail page anyway
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsResourceServlet.MsgErrorOnSearching"), request); 
			}
		} else {
			//no search or extract clicked (try last one)
			if (lastSearch != null){
				//get last searchlist, if existent
				try{
					searchResult.addAll(oxApi.getResourcesByPattern(lastSearch));
					request.setAttribute("lastSearch", lastSearch); 
				}catch(OXApiException exc){
					this.msgLogger.warning("EXCEPTION: " + exc.toString()); 
					//go to detail page anyway
					ServletUtils.setErrorMsg(messageBundle.getString(
							"ATFrogsResourceServlet.MsgErrorOnSearching"), request); 
				}
			} else if (lastExtract != null){
				List lastExtractedGroups = new Vector();
				
				if(resGroupsToExtract != null){
					try{
						searchResult.addAll(oxApi.getResGroupsResources(resGroupsToExtract));
						
						// inform jsp about last extracted groups
						for(int i=0; i < resGroupsToExtract.length; i++){
							lastExtractedGroups.add(resGroupsToExtract[i]);
						}
						request.setAttribute("lastExtract", "true"); // must be String != null, acts as flag  
						request.setAttribute("lastExtractedGroups", lastExtractedGroups); 
					}catch(OXApiException exc){
						this.msgLogger.warning("EXCEPTION: " + exc.toString()); 
						//go to detail page anyway
						ServletUtils.setErrorMsg(messageBundle.getString(
								"ATFrogsResourceServlet.MsgErrorOnExtractingResGroup"), request); 
					}
				}
			}
		}
		
		Collections.sort(searchResult, 
				new OXResourceComparator(OXResourceComparator.RID));
		
		request.setAttribute("searchResultList", searchResult); 
		
		//TODO: check whether an error occured before
		
		try{
			resGroupIdList = oxApi.getResGroupIdList();
			if(resGroupIdList != null){
				Collections.sort(resGroupIdList, new StringIgoreCaseComparator());
				request.setAttribute("allResGroupIds", resGroupIdList);				 
			}else{
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsResourceServlet.ErrorReadingResGroups"), request); 
				//will go to detail page anyway
			}		
			ServletUtils.forwardTo("/resources/resGroupDetails.jsp", request, response); 
		}catch(OXApiException exc){
			exc.printStackTrace();
			this.msgLogger.warning("EXCEPTION: " + exc.toString()); 
			// goto to detail page anyway
			ServletUtils.setErrorMsg(messageBundle.getString(
					"ATFrogsResourceServlet.ErrorReadingResGroups"), request); 
			this.resGroupsShowAll(request, response);
		}
	}
	
	/**
	 * This method deletes the current resource group.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void resGroupDelete(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String resGid         = null;
		OXScriptApi scriptApi = null;
		String confirmString = null; // holds passed parameter value of confirm
		List scriptOutput    = null; // holds the script output
		
		//read gid of resgroup to delete
		resGid = ServletUtils.getStringAttribute("resgroup", request, response); 
		// TODO: assert resGid not empty String
		
		confirmString = ServletUtils.getStringAttribute("confirm", request, response);
		
		if(!confirmString.equals("true")){
			ServletUtils.forwardTo("/resources/confirmResGrpDel.jsp", request, response);
			return;
		}
		
		//run script
		try {
			scriptApi = new OXScriptApi();
			scriptOutput = scriptApi.delResGroup(resGid, DELRESOURCEGROUP_SCRIPT_PATH);
			request.setAttribute("stdOutList", scriptOutput);
			this.msgLogger.info(ServletUtils.getUserFromSession(request) 
					+ " executed deleteresourcegroup with group " + resGid);
		} catch (OXScriptApiException e){
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			ServletUtils.setErrorMsg(messageBundle.getString(
					"ATFrogsResourceServlet.MsgErrorExecutingScript"), request); 
		}
		
		//go to the resgroups start page
		this.resGroupsShowAll(request, response);
	}

	/**
	 * This method deletes the selected resource.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void resDelete(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException{
		String res            = null;
		OXScriptApi scriptApi = null;
		String confirmString = null; // holds passed parameter value of confirm		
		List scriptOutput    = null; // holds script output
		
		//read gid of resgroup to delete
		res = ServletUtils.getStringAttribute("res", request, response); 
		
		confirmString = ServletUtils.getStringAttribute("confirm", request, response);
		
		if(!confirmString.equals("true")){
			ServletUtils.forwardTo("/resources/confirmResDel.jsp", request, response);
			return;
		}
		
		//run script
		try {
			scriptApi = new OXScriptApi();
			scriptOutput = scriptApi.delRes(res, DELRESOURCE_SCRIPT_PATH);
			request.setAttribute("stdOutList", scriptOutput);
			this.msgLogger.info(ServletUtils.getUserFromSession(request) 
					+ " executed deleteresource script with resource " + res); 
		} catch (OXScriptApiException e){
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			ServletUtils.setErrorMsg(messageBundle.getString("ATFrogsResourceServlet.MsgErrorExecutingScript"), request); 
		}
		
		//go to the resgroups start page
		this.resShowAll(request, response);
	}
	
	/**
	 * This method gets the resource(group) to create from the request and runs
	 * the script to create it.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void newResOrResGroup(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String name           = null; //name for new element
		String type           = null; //resgroup or res to create
		OXScriptApi scriptApi = null;
		List scriptOutput     = null; //holds script output
		
		//read name and type
		name     = ServletUtils.getStringAttribute("group_res", request, response); 
		type     = ServletUtils.getStringAttribute("toCreate", request, response); 
		
		//run script
		try {
			scriptApi = new OXScriptApi();
			
			if (type.equals("resgroup")){ 
				scriptOutput = scriptApi.addResGroup(name, ADDRESOURCEGROUP_SCRIPT_PATH);
				this.msgLogger.info(ServletUtils.getUserFromSession(request) 
						    + " executed newresourcegroup script with group " + name);
			} else {
				scriptOutput = scriptApi.addRes(name, ADDRESOURCE_SCRIPT_PATH);
				this.msgLogger.info(ServletUtils.getUserFromSession(request) 
						    + " executed newresource script with resource " + name); 
			}
			request.setAttribute("stdOutList", scriptOutput);
		} catch (OXScriptApiException e){
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			ServletUtils.setErrorMsg(messageBundle.getString(
					"ATFrogsResourceServlet.MsgErrorExecutingScript"), request); 
			request.setAttribute("group_res", name); 
			request.setAttribute("toCreate", type); 
			ServletUtils.forwardTo("/resources/new.jsp", request, response); 
			return;
		}
		
		//go to the start page
		if (type.equals("resgroup")){ 
			//put the group in the request
			request.setAttribute("resgroup", name); 
			this.resGroupDetail(request, response);
		} else {
			this.resShowAll(request, response);
		}
	}
	
	/**
	 * This function gets a list of all resource groups, puts them into the 
	 * request and forwards to the resource group start page.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void resGroupsShowAll(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		List resGroupsList = null;  //for all resgroups
		OXApi oxApi        = null;  //for the oxapi
		
		try{
			oxApi         = ServletUtils.getOXApiFromSession(request, response);
			resGroupsList = oxApi.getResGroupIdList();
			
			//check whether retrieving of groups was successful
			if(resGroupsList != null){
				Collections.sort(resGroupsList, new StringIgoreCaseComparator());
				//pass the resource groups to the jsp
				request.setAttribute("resGroupIdList", resGroupsList);				 
			}else{
				ServletUtils.setErrorMsg(messageBundle.getString(
							     "ATFrogsResourceServlet.ErrorReadingResGroups"), request);
			}
			
			//forward to the resource start page
			ServletUtils.forwardTo("/resources/allResGroups.jsp", request, response); 
		}catch(OXApiException e){
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			this.msgLogger.warning("NOTE: Error often due to wrong authentification. \n " +
					"Check username and password of user specified in web.xml and assert " +
					"that the user is in at least one group!");	
			ServletUtils.forwardToErrorPage(request, 
											response,
											messageBundle.getString(
											"ATFrogsResourceServlet.ErrorReadingResGroups"), 
											null);
		}
	}
	
	/**
	 * This function gets a list of all resources, puts them into the request and forwards
	 * to the group start page.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void resShowAll(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		List resList      = null;  //for all resources
		OXApi oxApi        = null;  //for the oxapi
		
		try{
			oxApi         = ServletUtils.getOXApiFromSession(request, response);
			resList       = oxApi.getResIdList();
			
			//check whether retrieving of resources was successfull
			if(resList != null){
				Collections.sort(resList, new StringIgoreCaseComparator());
				//pass the resource groups to the jsp
				request.setAttribute("resIdList", resList);				 
			}else{
				ServletUtils.setErrorMsg(messageBundle.getString("ATFrogsResourceServlet.MsgErrorReadingResources"), request); 
			}
			
			//forward to the resource start page
			ServletUtils.forwardTo("/resources/allRes.jsp", request, response); 
		}catch(OXApiException e){
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			ServletUtils.forwardToErrorPage(request, response, e.getMessage(), null);
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
