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
 * Global todo-List (highest priority on top of list!):
 *
 * TODO BUG: if creation of resourcegroup fails, 'resource' is selected 
 * TODO print strack traces only in debug mode
 */

/*
 * Created on 27.05.2005
 */
package atfrogs.servlets;

//java util and io classes
import java.io.*;
import java.util.logging.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import java.util.MissingResourceException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import atfrogs.ox.api.OXApi;
import atfrogs.util.ServletUtils;



/**
 * This servlet contains the login logic for atfrogs and forwards requests 
 * to responsible servlets.. 
 */
public class ATFrogsMainServlet extends HttpServlet{
	//for the logger
	private String LOGGER = "ATFrogs_logger"; 
	
	private Logger msgLogger  = null;
	
	private static boolean debug = false;
	
	private static final String MESSAGE_BUNDLE_NAME = "atfrogs.servlets.messages"; 	
	private static ResourceBundle messageBundle = null;
	
	/**
	 * The init method inits the logger. Because this servlet must be used first
	 * (login), the logger must only be created here.
	 * 
	 * @param conf the configuration for the servlet.
	 * @throws ServletException if initialization fails.
	 */
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		
		String debugString = null;
		String languageString = null;
		
		try{
			//get the filename for the logger from the web.xml
			String logFileName = this.getInitParameter("logfile"); 
			if(logFileName == null)
				throw new ServletException("cannot get logfile name"); 
			
			debugString = this.getInitParameter("logLevel"); 
			if(debugString == null)
				debugString = ""; 
			
			//init the logger, use 5 files with max 16MB
			Handler messageHandler     = new FileHandler(logFileName, 16384, 5); 
			Formatter messageFormatter = new XMLFormatter();
			messageHandler.setFormatter(messageFormatter);
			this.msgLogger            = Logger.getLogger(LOGGER);
			this.msgLogger.addHandler(messageHandler);
			
			/* set level */
			if(debugString.equals("off")) 
				this.msgLogger.setLevel(Level.OFF);
			else if(debugString.equals("debug")){ 
				this.msgLogger.setLevel(Level.ALL);
				debug = true;
				if(debug) this.msgLogger.warning("operating in debug level"); 
			}else
				// TODO check whether following level is appropriate
				this.msgLogger.setLevel(Level.INFO);
			
		} catch (Exception e){
			throw new ServletException("cannot initiate the logger"); 
		}
		
		/* get resource language for specific language */
		try{
			
			languageString = this.getInitParameter("language");
			// TODO what happens if invalid String
			if(languageString != null){
				Locale locale = new Locale(languageString);
				messageBundle = PropertyResourceBundle.getBundle(MESSAGE_BUNDLE_NAME, locale);
			}else
				messageBundle = PropertyResourceBundle.getBundle(MESSAGE_BUNDLE_NAME);
		} catch (MissingResourceException e){
			this.msgLogger.warning(
					"resource bundle for the specified base name cannot be found");
			throw new ServletException(
					"resource bundle for the specified base name cannot be found"); 
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
		String categorie         = null; // requested categorie
		HttpSession session      = null; // holds the api and login data
		
		//catches ServletException and forwards to the error jsp
		try {
			//force pages to be reloaded on each call
			response.setHeader("Cache-Control", "no-cache");  
			response.setHeader("Pragma", "no-cache");  
			
			//init some parameter if necessary
			this.initATFrogs(request, response);

			//read categorie parameter
			categorie = ServletUtils.getStringAttribute("categorie", request, response); 
			
			//handle requested categorie
			if (categorie.equals("groups")) { 
				//group administration
				ServletUtils.forwardTo("ATFrogs.groups", request, response); 
			} else if (categorie.equals("resources")) { 
				//resource administration
				ServletUtils.forwardTo("ATFrogs.resources", request, response); 
			} else if (categorie.equals("user")){ 
				//user administration
				ServletUtils.forwardTo("ATFrogs.user", request, response);
			} else if (categorie.equals("logout")){
				this.logout(request, response);
			} else {
				//no selection, handle default (user)
				ServletUtils.forwardTo("ATFrogs.user", request, response); 
			}
		} catch (Exception e) {
			//error
			this.msgLogger.warning("EXCEPTION: " + e.toString()); 
			ServletUtils.forwardToErrorPage(request, response, 
					messageBundle.getString(
							"ATFrogsMainServlet.MsgErrorOccured"), null); 
		}		
	}
	
	/**
	 * This function inits some parameter (oxapi, ...).
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void initATFrogs(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		HttpSession	session = request.getSession(true);
		
		if (session.getAttribute("valid") == null){
			//get the correct login data and the servleturl (groupuser)
			
			//get password and name from the login
			String user = this.getInitParameter("user");
			String pass = this.getInitParameter("pass");
			String servleturl   	= this.getInitParameter("servleturl"); 
			String defaultCategorie = this.getInitParameter("defaultCategorie"); 
		
			if ((user == null) || (pass == null)){
				this.msgLogger.warning("No user or password specified in web.xml!");
				throw new ServletException("No user or password specified in web.xml");
			}
			
			if (servleturl == null){
				this.msgLogger.warning("No servleturl specified in web.xml!");
				throw new ServletException("No servleturl specified in web.xml");	
			}
			
			//validate session
			session.setAttribute("valid", "yes");
			
			if (request.getUserPrincipal() == null){
				session.setAttribute("user", user);
				this.msgLogger.warning("User " + user + " (from web.xml) has logged in");
			} else {
				session.setAttribute("user", request.getUserPrincipal().getName());
				this.msgLogger.info("User " + request.getUserPrincipal().getName() + " has logged in");
			}
			
			//create the OXApi
			OXApi oxApi = new OXApi(servleturl, user, pass);
			oxApi.setDebug(debug);
			session.setAttribute("oxApi", oxApi); 
			
			//create resource bundle object
			session.setAttribute(ServletUtils.ATTR_MSG_BUNDLE, messageBundle); 
			
			//go to categorie user defined his default
			if(defaultCategorie == null) {
				defaultCategorie = "";
			} else {
				request.setAttribute("categorie", defaultCategorie);
			}
		}
	}

	/**
	 * This function logs the user out.
	 * 
	 * @param request the current request.
	 * @param response the current response.
	 * @throws ServletException top level exception.
	 * @throws IOException if an I/O error occurs.
	 */
	private void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		if (request.getSession() != null)
			request.getSession().invalidate();
		ServletUtils.forwardTo("/index.jsp", request, response); 
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
