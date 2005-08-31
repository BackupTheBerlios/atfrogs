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
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="atfrogs.ox.objects.*" %>
<%@ page import="atfrogs.ox.util.*" %>

<%
	// here you may add additional content to meta, title and body tags:
	request.setAttribute("extraTitle", "User");
	request.setAttribute("extraMeta", /* add content String */"");
	request.setAttribute("extraBody", /* add content String */"");
%>

<%  // retrieve objects from servlet
	OXUser user = (OXUser)request.getAttribute("user");
%>

<%@ include file="/include/jsp/header.inc.jsp" %>


<form action="<%= response.encodeURL(userServletURL) %>" method="post">

<input name="action" value="userDetail" class="input-search" type="hidden">
<input name="uid" value="<%= user.getUid() %>" class="input-search" type="hidden">

<table border="0" cellpadding="0" cellspacing="0" width="100%"><tbody>
<tr>
	<td colspan="3" height="6"><spacer type="block" height="6"></td>
</tr>
<tr>
	<td width="25"></td><td nowrap="nowrap"></td>
	<td align="right" nowrap="nowrap">
	</td>
</tr>

<tr>
	<td width="25">
	</td>
	<td>
		<img src="<%= baseDir%>/include/img/dummy.png" height="5" width="1">
	</td>
</tr>
<tr>
	<td width="25">
	</td>
	<td colspan="2">
		<!-- tabs start -->
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
			<td class="reiter_d">
				&nbsp;&nbsp;
				<a class="reiter_d" href="<%= response.encodeURL(userServletURL+"?action=showUser") %>">
				<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.TabUser"):"Users"%>
				</a>
				&nbsp;
			</td>			
			<td background="<%= baseDir%>/include/img/shade.png" width="7">
				&nbsp;
			</td>
			<td class="reiter_a">
				&nbsp;&nbsp;
				<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.TabDetails"):"Userdetails"%>
				&nbsp;
			</td>
			<td background="<%= baseDir%>/include/img/shade.png" width="7">
				&nbsp;
			</td>

		</tr></tbody>
		</table>
		<!-- tabs end -->	
		
		<!-- rahmen start -->
		<table border="0" cellpadding="0" cellspacing="0" width="100%"><tbody>
		<tr>
			<td class="td-back-lc" width="10">
				&nbsp;
			</td>
			<td class="td-back-lc" align="right" height="8">
				<img src="<%= baseDir%>/include/img/dummy.png" height="5" width="1">
     			
				<!-- tab title start -->
				<table border="0" cellpadding="0" cellspacing="0" width="100%"><tbody>
				<tr>
					<td class="td-title-lc" nowrap="nowrap" width="40%">
						<% if (user != null) { %>
						<%= user.getFirstname()%> <%=user.getSurname()%>
						<% } else { %>
						&nbsp;
						<%} %>
					</td>
					<td class="td-title-lc" align="center" nowrap="nowrap" width="20%">
						&nbsp;
					</td>
					<td class="td-title-lc" align="right" nowrap="nowrap" width="40%">
						<a href="<%= response.encodeURL(userServletURL+"?action=resetUserPas&uid=" + user.getUid()) %>">
						<img src="<%= baseDir%>/include/img/item_resetpas.png" BORDER="0" alt="
						<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.ResetIcon"):"Password reset"%>
						" title="
						<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.ResetIcon"):"Password reset"%>
						">
						</a>
						&nbsp;&nbsp;&nbsp;
						<a href="<%= response.encodeURL(userServletURL+"?action=delUser&uid=" + user.getUid()) %>">
						<img src="<%= baseDir%>/include/img/item_delete.png" BORDER="0" alt="
						<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.DeleteIcon"):"Delete user"%>
						" title="
						<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.DeleteIcon"):"Delete user"%>
						">
						</a>
						&nbsp;&nbsp;&nbsp;						
						<img src="<%= baseDir%>/include/img/hox.png">
						&nbsp;
					</td>
				</tr></tbody>
				</table>
				<!-- tab title end -->
				
				<table border="0" width="100%" cellpadding="5" cellspacing="0">
 				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td class="td-box-brown" colspan="4" height="20">
						&nbsp;
						<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.Information"):"Personal information"%>
						&nbsp;
					</td>
				</tr>
 				<tr> 
					<td WIDTH="15%" colspan="2" CLASS="td-feld">
						<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.Name"):"Name"%>
					</td>
					<td WIDTH="35%" colspan="2" CLASS="td-feld-inhalt"><% if (user!=null){%>
						<%=user.getFirstname()%> <%=user.getSurname()%><%}%>
					</td>
				</tr>
 				<tr> 
					<td WIDTH="15%" colspan="2" CLASS="td-feld">
						<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.Forname"):"Forname"%>
					</td>
					<td WIDTH="35%" colspan="2" CLASS="td-feld-inhalt"><% if (user!=null){%>
						<%=user.getFirstname()%><%}%>
					</td>
				</tr>
 				<tr> 
					<td WIDTH="15%" colspan="2" CLASS="td-feld">
						<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.Surename"):"Surename"%>
					</td>
					<td WIDTH="35%" colspan="2" CLASS="td-feld-inhalt"><% if (user!=null){%>
						<%=user.getSurname()%><%}%>
					</td>
				</tr>
 				<tr> 
					<td WIDTH="15%" colspan="2" CLASS="td-feld">
						<%= messageBundle!=null?messageBundle.getString("UserDetail_JSP.Email"):"E-Mail"%>
					</td>
					<td WIDTH="35%" colspan="2" CLASS="td-feld-inhalt"><% if (user!=null){%>
						<%=user.getMail()%><%}%>
					</td>
				</tr>
				
				<tr>
					<td colspan="4" class="td-feld">
						<img src="<%= baseDir%>/include/img/dummy.png" height="5" width="1">
					</td>
				</tr></tbody>
				</table>
				<!-- PARTICIPANTS END -->
     
			</td>
			<td class="td-back-lc" width="10">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="3" class="td-back-lc">
				<img src="<%= baseDir%>/include/img/dummy.png" height="5" width="1">
			</td>
		</tr></tbody>
		</table>
		<!-- rahmen end -->

	</td>
	<td colspan="3" width="25">&nbsp;</td>
</tr>
<tr>
	<td colspan="4" widht="5">
		<img src="<%= baseDir%>/include/img/dummy.png" height="5" width="1">
	</td>
</tr>
<tr>
	<td>
		&nbsp;
	</td>
	<td>
		&nbsp;
	</td>
	<td class="search" align="right">
	</td>
	<td>
		&nbsp;
	</td>
</tr></tbody>
</table>
</form>

<%@ include file="/include/jsp/footer.inc.jsp" %>

