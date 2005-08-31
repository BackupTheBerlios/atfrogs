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
	request.setAttribute("extraTitle", "Benutzer");
	request.setAttribute("extraMeta", /* add content String */"");
	request.setAttribute("extraBody", /* add content String */"");
%>

<%@ include file="/include/jsp/header.inc.jsp" %>

<% // get old values if wrong inputs made
	String oldUidList = request.getParameter("uidList");
	if(oldUidList == null) oldUidList = "";
	
	// staff default:
	boolean staff = !(request.getParameter("staffOrStu")!= null &&
		 !request.getParameter("staffOrStu").equals("staff"));
%>

<form action="<%= response.encodeURL(userServletURL) %>" method="post">

<input name="action" value="replicateUser" class="input-search" type="hidden">
<input name="execute" value="true" class="input-search" type="hidden">

<table border="0" cellpadding="0" cellspacing="0" width="100%"><tbody>
<tr>
	<td colspan="3" height="6"><spacer type="block" height="6"></td>
</tr>
<tr>
	<td width="25"></td><td nowrap="nowrap"></td>
	<td align="right" nowrap="nowrap"></td>
	<td width="25">&nbsp;</td>
</tr>
<tr>
	<td colspan="3" ><img src="<%= baseDir%>/include/img/dummy.png" width="25" height="10"></td>
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
				Replicate-Skript
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
						&nbsp;
					</td>
					<td class="td-title-lc" align="center" nowrap="nowrap" width="20%">
						&nbsp;
					</td>
					<td class="td-title-lc" align="right" nowrap="nowrap" width="40%">

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
						Replicate-Users
						&nbsp;
					</td>
				</tr>
 				<tr> 
					<td WIDTH="15%" colspan="2" CLASS="td-feld">
						UID-Liste<br /><small>(durch Leerzeichen getrennt)</small>
					</td>
					<td WIDTH="35%" colspan="2" CLASS="td-feld-inhalt">
					<textarea name="uidList" class="area-normal"><%= oldUidList %></textarea>
					</td>
				</tr>
 				<tr> 
					<td WIDTH="15%" colspan="2" CLASS="td-feld">
					&nbsp;
					</td>
					<td WIDTH="35%" colspan="2" CLASS="td-feld-inhalt">
						    <input type="radio" name="staffOrStu" value="staff" <%= staff?"checked":""%>>Staff
						    <input type="radio" name="staffOrStu" value="student" <%= !staff?"checked":""%>>Student
					</td>
				</tr>
				
 				<tr> 
					<td WIDTH="15%" colspan="2" CLASS="td-feld">
						&nbsp;
					</td>
					<td WIDTH="35%" colspan="2" CLASS="td-feld-inhalt">
				   <input class="button-submit" type="submit" name="save" value="Ausf&uuml;hren">&nbsp;
				   <input class="button-submit" type="button" name="button" value="Abbrechen" onClick="location.href='<%= response.encodeURL(userServletURL+"?action=showUser") %>';">
					</td>
				</tr>
				
				
				</tbody>
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

