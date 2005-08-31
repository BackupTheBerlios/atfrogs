<%
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
  * The layout of this jsp is derived from the page layout
  * of Open-Xchange server 
  * Copyright (c) Netline Internet Service GmbH 2004
  */
%>
<%@ page errorPage="/error.jsp" %>

<%@ page import="atfrogs.util.*" %>
<%@ page import="javax.servlet.ServletException" %>

<%
	// some globals 
	String baseDir = "/ATFrogs";
    String mainServletURL = "/ATFrogs/ATFrogs";
    String groupServletURL = "/ATFrogs/ATFrogs.groups";
    String resourceServletURL = "/ATFrogs/ATFrogs.resources";
    String userServletURL = "/ATFrogs/ATFrogs.user";
%>

<%
// get message bundle
ResourceBundle messageBundle = null;

if(session != null)
	try{
		messageBundle = ServletUtils.getMessageBundleFromSession(session);
	}catch(Exception exc){
		/* do nothing */
	}

if(messageBundle == null)
	messageBundle = (ResourceBundle)request.getAttribute(ServletUtils.ATTR_MSG_BUNDLE);

/*if(messageBundle == null)
	try{
		messageBundle = ResourceBundle.getBundle("atfrogs.servlets.messages");
	}catch (MissingResourceException exc){
		// do nothing, buttons etc. have default values.
		;
	}*/
%>

<HTML>
<HEAD>
<%= request.getAttribute("extraMeta")==null?"":request.getAttribute("extraMeta") %>

<link rel="stylesheet" type="text/css" href="<%= baseDir %>/include/css/stylesheet.css">
<link rel="SHORTCUT ICON" href="<%= baseDir %>/favicon.ico">
<link rel="ICON" href="<%= baseDir %>/favicon.ico" type="image/x-icon">


<TITLE>
ATFrogs - Administration Tool for OX&#0153;
<%= request.getAttribute("extraTitle")==null?"":" - "+ request.getAttribute("extraTitle") %>
</TITLE>
</HEAD>

<BODY <%= request.getAttribute("extraBody")==null?"":request.getAttribute("extraBody") %>>

<table border="0" width="100%" cellspacing="0" cellpadding="0">
<tr>
	<td height="76" style="padding-top:1;" background="/ATFrogs/include/img/top_name.png">
    <table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center" valign="bottom" class="text-small-black"><img src="/ATFrogs/include/img/dummy.png" border="0" width="35" height="1">&nbsp;</td>
		<td align="center" valign="bottom" class="text-small-black" height="36" width="68"><a href="<%= response.encodeURL(mainServletURL+"?categorie=user") %>"><img src="/ATFrogs/include/img/userlogo.png" border="0" alt="Benutzer" title="Benutzer" name="Benutzer"></a></td>
		<td align="center" valign="bottom" class="text-small-black"><img src="/ATFrogs/include/img/dummy.png" border="0" width="35" height="1">&nbsp;</td>
		<td align="center" valign="bottom" class="text-small-black" height="36" width="68"><a href="<%= response.encodeURL(mainServletURL+"?categorie=groups") %>"><img src="/ATFrogs/include/img/grouplogo.png" border="0" alt="Gruppen" title="Gruppen" name="Gruppen"></a></td>
		<td align="center" valign="bottom" class="text-small-black"><img src="/ATFrogs/include/img/dummy.png" border="0" width="35" height="1">&nbsp;</td>
		<td align="center" valign="bottom" class="text-small-black" height="36" width="68"><a href="<%= response.encodeURL(mainServletURL+"?categorie=resources") %>"><img src="/ATFrogs/include/img/resourcelogo.png" border="0" alt="Resourcen" title="Resourcen" name="Resourcen"></a></td>
	</tr>
	<tr>
		<td align="center" valign="bottom" class="text-small-black"><img src="/ATFrogs/include/img/dummy.png" border="0" width="35" height="1">&nbsp;</td>
		<td align="center" valign="top" class="text-small-black" width="68" nowrap><a href="<%= response.encodeURL(mainServletURL+"?categorie=user") %>">
			<%= messageBundle!=null?messageBundle.getString("JSP.Form.NavigationUsers"):"Users"%></a></td>
		<td align="center" valign="bottom" class="text-small-black"><img src="/ATFrogs/include/img/dummy.png" border="0" width="35" height="1">&nbsp;</td>
		<td align="center" valign="top" class="text-small-black" width="68" nowrap><a href="<%= response.encodeURL(mainServletURL+"?categorie=groups") %>">
			<%= messageBundle!=null?messageBundle.getString("JSP.Form.NavigationGroups"):"Groups"%></a></td>
		<td align="center" valign="bottom" class="text-small-black"><img src="/ATFrogs/include/img/dummy.png" border="0" width="35" height="1">&nbsp;</td>
		<td align="center" valign="top" class="text-small-black" width="68" nowrap><a href="<%= response.encodeURL(mainServletURL+"?categorie=resources") %>">
		<%= messageBundle!=null?messageBundle.getString("JSP.Form.NavigationResources"):"Resources"%></a></td>
	</tr>
    </table>
	</td>
	<td height="76" width="140" background="/ATFrogs/include/img/top_icon.png">
	</td>
</tr>
<tr>
	<td colspan="2" align="right" valign="middle" style="padding-right:4;">
	<a href="<%= response.encodeURL(mainServletURL+"?categorie=logout") %>"><img src="/ATFrogs/include/img/arrow_right.png" border="0">&nbsp;Logout</a>
	</td>
</tr>
<tr>
	<td colspan="2" align="center">
		<%=request.getAttribute("errorMsg")==null?"<br/>":"<p><span style=\"color:red\">"+request.getAttribute("errorMsg")+"</span></p>"%>
		<%=request.getAttribute("warning")==null?"<br/>":"<p><span style=\"color:orange\">"+request.getAttribute("warning")+"</span></p>"%>
		<%=request.getAttribute("msg")==null?"<br/>":"<p><span style=\"color:green\">"+request.getAttribute("msg")+"</span></p>"%>
	</td>		
</tr>
</table>
