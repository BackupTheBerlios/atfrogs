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
 * Created on 05.06.2005
 * @file ServletUtils.java
 *
 */
package atfrogs.util;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import atfrogs.ox.api.OXApi;
import atfrogs.ox.api.OXApiException;

/**
 * This class contains utility functions for the servlets. 
 */
public class ServletUtils {
	private final static String ERRORPAGE         = "/error.jsp";  //the errorpage
	private final static String RQMSGATTRIBUTE    = "msg";         //the attribute for the message
	private final static String RQWARNINGATTRIBUTE = "warning";    // the attribute for the warning
	private final static String RQERRMSGATTRIBUTE = "errorMsg";    //the attribute for the errormessage
	
	private final static String OXAPIATTRIBUTE    = "oxApi";       //the attribute for the ox api
	public static final String ATTR_MSG_BUNDLE = "resourceBundle"; // the attribute for the resource bundle
	
	/**
	 * This method sets the passed messages and forwards to the errorpage.
	 * 
	 * @param request current request.
	 * @param response current response.
	 * @param errorMsg the error message to set in the request.
	 * @param msg the message to set in the request.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String errorMsg, String msg)
	throws ServletException, IOException {
		if(errorMsg!=null)
			setErrorMsg(errorMsg, request);
		if(msg!=null)
			setMsg(msg, request);
		forwardTo(ERRORPAGE, request, response);
	}
	
	/**
	 * This method forwards to the passed location.
	 * 
	 * @param location The file to forward to.
	 * @param request current request.
	 * @param response current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void forwardTo(String location, HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(location);
		dispatcher.forward(request, response);
	}
	
	/**
	 * For setting the message in the request.
	 * 
	 * @param msg The messages to set.
	 * @param request The current request.
	 */
	public static void setMsg(String msg, HttpServletRequest request){
		if ((msg != null) && (request != null))
			request.setAttribute(RQMSGATTRIBUTE, msg);
	}
	
	/**
	 * For setting the warning in the request.
	 * 
	 * @param msg the warning messages to set.
	 * @param request the current request.
	 */
	public static void setWarning(String msg, HttpServletRequest request){
		if ((msg != null) && (request != null))
			request.setAttribute(RQWARNINGATTRIBUTE, msg);
	}	
	
	/**
	 * For setting the error message in the request.
	 * 
	 * @param errorMsg The error messages to set.
	 * @param request The current request.
	 */
	public static void setErrorMsg(String errorMsg, HttpServletRequest request){
		if ((errorMsg != null) && (request != null))
			request.setAttribute(RQERRMSGATTRIBUTE, errorMsg);
	}
	
	/**
	 * For testing if a user is logged in.
	 * 
	 * @param request The current request.
	 * @param response The current response.
	 * @return true iff user logged in.
	 */
	public static boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response){
		//if exists, get sesson
		HttpSession session = request.getSession(false);
		
		//test for beeing valid (attribute "valid" is bound to the session)
		if ((session == null) || (session.getAttribute("valid") == null)){
			//no session or not logged in
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the current used ox api from the session.
	 * 
	 * @param request current request.
	 * @param response current response.
	 * @return the ox api from the current session.
	 * @throws OXApiException If no session exists or the api is not set.
	 */
	public static OXApi getOXApiFromSession(HttpServletRequest request, HttpServletResponse response)
	throws OXApiException{
		HttpSession session   = request.getSession(false);
		OXApi oxApi           = null;
		
		//if the session exists, get the ox api
		if (session != null){
			oxApi = (OXApi)session.getAttribute(OXAPIATTRIBUTE);
			if (oxApi != null){
				return oxApi;
			}
		}
		throw new OXApiException("Fehler beim Laden der OXApi!");
	}
	
	/**
	 * Get the resoure bundle containing message strings from the session
	 *  
	 * @param request current request.
	 * @param response current response.
	 * @return the resource bundle.
	 */
	public static ResourceBundle 
	getMessageBundleFromSession(HttpServletRequest request, HttpServletResponse response)
	throws ServletException{
		HttpSession session = request.getSession(false);
		ResourceBundle resBundle = null;
		
		//if the session exists, get the messageBundle api
		if(session == null)
			throw new ServletException("session is null");
		
		
		resBundle = (ResourceBundle)session.getAttribute(ATTR_MSG_BUNDLE);
		if(resBundle == null)
			throw new ServletException("no resource bundle within session");
		
		return resBundle; 
	}
	
	/**
	 * Get the resoure bundle containing message strings from the session
	 *  
	 * @param session the session
	 * @return the resource bundle.
	 */
	public static ResourceBundle getMessageBundleFromSession(HttpSession session)
	throws ServletException{
		ResourceBundle resBundle = null;
		
		if(session == null)
			throw new ServletException("session is null");
		
		resBundle = (ResourceBundle)session.getAttribute(ATTR_MSG_BUNDLE);
		if(resBundle == null)
			throw new ServletException("no resource bundle wihtin session");
		
		return resBundle; 
		
	}
	
	/**
	 * This functon gets the passed attribute from the request and returns it. If the
	 * attribute is not set, an empty string is returned, not null.
	 * 
	 * @param attribute The attribute to get.
	 * @param request The current request.
	 * @param response The current response.
	 * @return The attribute or "" if not set.
	 */
	public static String 
	getStringAttribute(String attribute, HttpServletRequest request, HttpServletResponse response){
		
		if (request.getParameter(attribute) != null){
			return (String)request.getParameter(attribute);
		} else if (request.getAttribute(attribute) != null){
			return (String)request.getAttribute(attribute);
		} else {
			return "";
		}
	}
	
	/**
	 * Get the actual user from the session and returns it.
	 * 
	 * @param request the actual request
	 * @return the username
	 */
	public static String getUserFromSession(HttpServletRequest request){
		HttpSession session = request.getSession();
		if (session == null){
			return "UNKNOWN";
		}
		String user = (String)session.getAttribute("user");
		if (user == null){
			return "UNKNOWN";
		}
		return user;
	}
}
