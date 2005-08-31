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
import java.util.List;
import java.util.Collections;
import java.util.ResourceBundle;
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
import atfrogs.util.ServletUtils;
import atfrogs.ox.objects.OXUser;
import atfrogs.util.OXUserIgnoreCaseComparator;


/**
 * This servlet contains the logic for user administration. 
 */
public class ATFrogsUserServlet extends HttpServlet{
	//for the logger
	private String LOGGER     = "ATFrogs_logger"; 
	private Logger msgLogger  = null;
	
	//strings for the paths to the scripts
	private static String RESETUSERPASSWORD_SCRIPT_PATH = null;
	private static String DELUSER_SCRIPT_PATH           = null;
	private static String REPLICATE_SCRIPT_PATH         = null;
	
	//new password for the resetpassword script
	private static String RESETPASSWORD                 = null;

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
		RESETUSERPASSWORD_SCRIPT_PATH = this.getInitParameter(
				"resetuserpassword_script_path"); 
		if(RESETUSERPASSWORD_SCRIPT_PATH == null)
			throw new ServletException(
					"path to resetuserpassword script not set"); 
		DELUSER_SCRIPT_PATH = this.getInitParameter(
				"deluser_script_path"); 
		if(DELUSER_SCRIPT_PATH == null)
				throw new ServletException(
					"path to deluser script not set"); 
		REPLICATE_SCRIPT_PATH = this.getInitParameter(
				"replicate_script_path"); 
		if(REPLICATE_SCRIPT_PATH == null)
				throw new ServletException(
					"path to replicate script not set"); 
		
		//get the reset pas
		RESETPASSWORD = this.getInitParameter("resetpassword"); 
		if(RESETPASSWORD == null)
			throw new ServletException("resetpassword not set"); 

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
			response.setHeader("Cache-Control", "no-cache");  //$NON-NLS-2$
			response.setHeader("Pragma", "no-cache");  //$NON-NLS-2$
			
			// load messageBundle. May throw ServletException 
			messageBundle = ServletUtils.getMessageBundleFromSession(request, response);
			
			//read action parameter
			action = ServletUtils.getStringAttribute("action", request, response); 
			
			if (action.equals("userDetail")){ 
				//go to the detail page for the selected user
				this.userDetail(request, response);
			} else	if (action.equals("resetUserPas")){ 
				//reset the user pas
				this.resetUserPas(request, response);
			} else if (action.equals("replicateUser")){
				// replicate users
				this.replicateUser(request, response);
			} else if (action.equals("delUser")){
				//del user
				this.delUser(request, response);
			} else {
				//unknown action, go to the start page for user
				this.showUser(request, response);
			}
		} catch (Exception e) {
			//set the error message and forward to the error jsp
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			ServletUtils.forwardToErrorPage(request, response, 
					messageBundle.getString("ATFrogsUserServlet.MsgErrorOccured"), null); 
		}		
	}

	/**
	 * This function resets the user password and returns to the detail page.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void resetUserPas(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String uid            = null; //the uid passed from the jsp
		OXScriptApi scriptApi = null;
		String confirmString = null; // holds passed parameter value of confirm
		List scriptOutput    = null; //holds the scriptoutput
		
		uid = request.getParameter("uid");
		
		if (uid != null){
			confirmString = ServletUtils.getStringAttribute("confirm", request, response);
			
			if(!confirmString.equals("true")){
				ServletUtils.forwardTo("/user/confirmUserResPass.jsp", request, response);
				return;
			}
			
			try{
				scriptApi = new OXScriptApi();
				//run script
				scriptOutput = scriptApi.resetUserPassword(uid, 
											RESETPASSWORD, 
											RESETUSERPASSWORD_SCRIPT_PATH);
				request.setAttribute("stdOutList", scriptOutput);
                this.msgLogger.info(ServletUtils.getUserFromSession(request) 
                		+ " executed resetuserpasswd script for user " + uid); 
			} catch (OXScriptApiException e){
				this.msgLogger.warning("EXCEPTION: " + e.toString()); 
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsUserServlet.MsgErrorExecutingScript"), request);
			}
		} else {
			ServletUtils.setErrorMsg(messageBundle.getString(
					"ATFrogsUserServlet.MsgErrorNoUserPassed"), request); 
		}
		
		//go back to the user detail page
		this.userDetail(request, response);
	}	
	
	/**
	 * This function get the selected user (uid) and forwards with the
	 * object for the uid to the detail page.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void userDetail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		OXUser user        = null;  //for all user
		OXApi oxApi        = null;  //for the oxapi
		String uid         = null;  //the uid of the selected user
		
		uid = (String)request.getParameter("uid"); 
		if (uid == null){
			ServletUtils.setErrorMsg(messageBundle.getString(
					"ATFrogsUserServlet.MsgErrorNoUserSelected"), request); 
			ServletUtils.forwardTo("/user/showUser.jsp", request, response); 
			return;
		}

		try{
			oxApi         = ServletUtils.getOXApiFromSession(request, response);
			user          = oxApi.getUser(uid);
			
			//check whether retrieving of user was successful and there is exactly one user
			if(user != null){
				//pass the user to the jsp
				request.setAttribute("user", user);				 
			}else{
				ServletUtils.setErrorMsg(messageBundle.getString(
							     "ATFrogsUserServlet.MsgErrorReadingUser"), request);
				ServletUtils.forwardTo(
				    "/user/showUser.jsp", request, response);
				return;
			}
			
			//forward to the detail page
			ServletUtils.forwardTo("/user/userDetail.jsp", request, response); 
		}catch(OXApiException e){
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			ServletUtils.forwardToErrorPage(request,
					response, 
					messageBundle.getString(
						"ATFrogsUserServlet.MsgErrorReadingUser"),
					null);
		}
	}
	
	/**
	 * This function gets all user, passes them to the jsp and forwards to
	 * the start page.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void showUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		List userList      = null;  //for all user
		OXApi oxApi        = null;  //for the oxapi

		boolean doSearch  = false; //the searchbutton
		String toSearch    = null;  //the searchpattern
		
		doSearch = request.getParameter("doSearch")!=null;  
		toSearch = ServletUtils.getStringAttribute("toSearch", request, response); 
		
		if (doSearch){
			//do the search and pass the data to the jsp
			try{
				oxApi         = ServletUtils.getOXApiFromSession(request, response);
				userList      = oxApi.getUsersByPattern(toSearch);
				
				//put the pattern back into the request
				request.setAttribute("toSearch", toSearch); 
				
				//check whether retrieving of user was successful
				if(userList != null){
					Collections.sort(userList, new OXUserIgnoreCaseComparator());
					//pass the user list to the jsp
					request.setAttribute("userIdList", userList);				 
				}else{
					ServletUtils.setErrorMsg(
					    messageBundle.getString(
						"ATFrogsUserServlet.MsgErrorReadingUser"), 
					    request);
				}
			}catch(OXApiException e){
				this.msgLogger.warning("EXCEPTION: " + e.toString()); 
				ServletUtils.forwardToErrorPage(request,
												response, 
												messageBundle.getString(
													"ATFrogsUserServlet.MsgErrorReadingUser"),
												null);
				return;
			}
		}
		
		//forward to the user start page
		ServletUtils.forwardTo("/user/showUser.jsp", request, response); 
	}
	
	/**
	 * This function deletes an existing ox user.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void delUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String uid            = null;
		OXScriptApi scriptApi = null;
		String confirmString  = null; // holds passed parameter value of confirm
		List scriptOutput     = null; // holds script output
		
		//read gid of resgroup to delete
		uid = ServletUtils.getStringAttribute("uid", request, response); 
		
		confirmString = ServletUtils.getStringAttribute("confirm", request, response);
		
		if(!confirmString.equals("true")){
			ServletUtils.forwardTo("/user/confirmUserDel.jsp", request, response);
			return;
		}
		
		//run script
		try {
			scriptApi = new OXScriptApi();
			scriptOutput = scriptApi.delUser(uid, DELUSER_SCRIPT_PATH);
			request.setAttribute("stdOutList", scriptOutput);
			this.msgLogger.info(ServletUtils.getUserFromSession(request) +
					" executed delete user script with uid " + uid);
		} catch (OXScriptApiException e){
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			ServletUtils.setErrorMsg(messageBundle.getString(
					"ATFrogsUserServlet.MsgErrorExecutingScript"), request); 
		}
		
		//go to the resgroups start page
		this.showUser(request, response);	
	}
	
	/**
	 * This function replicates LDAP user information to OX user information by means of
	 * the replicate script.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void replicateUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String uidList            = null; // list of uids separated by whitespaces
		String staffOrStu         = null; // checkbox
		List scriptOutput         = null; // holds the script output
		boolean execute = false; // whether to show input form or execute script
		boolean staff = false; // !staff == student

		OXScriptApi scriptApi = null;

		execute = request.getParameter("execute")!=null && 
			request.getParameter("execute").equals("true");
		
		if(!execute){
			ServletUtils.forwardTo("/user/replicate.jsp", request, response); 
			return;
		}
		
		uidList    = ServletUtils.getStringAttribute("uidList", request, response);
		staffOrStu = ServletUtils.getStringAttribute("staffOrStu", request, response);
		if (staffOrStu.equals("staff"))
			staff = true;
		else
			staff = false;
		
		if (uidList != null){
			try{
				scriptApi = new OXScriptApi();
				//run script
				scriptOutput = scriptApi.replicate(uidList, staff, REPLICATE_SCRIPT_PATH);
				request.setAttribute("stdOutList", scriptOutput);
                this.msgLogger.info(ServletUtils.getUserFromSession(request) 
                		+ " executed replicate script for: " + uidList); 
			} catch (OXScriptApiException e){
				this.msgLogger.warning("EXCEPTION: " + e.toString()); 
				ServletUtils.setErrorMsg(messageBundle.getString(
						"ATFrogsUserServlet.MsgErrorExecutingScript"), request);
				ServletUtils.forwardTo("/user/replicate.jsp", request, response);
				return;
			}
		} else {
			ServletUtils.setErrorMsg(messageBundle.getString(
					"ATFrogsUserServlet.MsgErrorNoListPassed"), request); 
			ServletUtils.forwardTo("/user/replicate.jsp", request, response);
			return;
		}
		
		//go back to the user detail page
		this.showUser(request, response);
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
