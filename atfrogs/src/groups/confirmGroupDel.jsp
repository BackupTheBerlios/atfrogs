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

<%
	// here you may add additional content to meta, title and body tags:
	request.setAttribute("extraTitle", "Groups");
	request.setAttribute("extraMeta", /* add content String */"");
	request.setAttribute("extraBody", /* add content String */"");
%>

<%@ include file="/include/jsp/header.inc.jsp" %>

<%  // retrieve objects from servlet
	String gid = request.getParameter("group");
%>

<form ACTION="<%= response.encodeURL(groupServletURL) %>" METHOD="POST">
<input name="action" value="neu" type="hidden">
<table border="0" width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td colspan="3" height="6"><spacer type="block" height="6"></td>
</tr>
<tr>
	<td width="25">&nbsp;</td>
	<td>
	&nbsp;
	</td>
	<td width="25">&nbsp;</td>
</tr>
<tr>
	<td colspan="3" ><img src="<%= baseDir%>/include/img/dummy.png" width="25" height="10"></td>
</tr>
<tr>
	<td width="25"></td>
	<td>
	
		<!-- tabs start -->
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="reiter_d">
				&nbsp;&nbsp;
				<a class="reiter_d" href="<%= response.encodeURL(groupServletURL+"?action=allGroups") %>">
				<%= messageBundle!=null?messageBundle.getString("ConfirmGroupDel_JSP.TabGrp"):"Groups"%>
				</a>
				&nbsp;
			</td>
    		<td background="<%= baseDir%>/include/img/shade.png" width="7">
				&nbsp;
			</td>
			<td class="reiter_a">
				&nbsp;&nbsp;
				<%= messageBundle!=null?messageBundle.getString("ConfirmGroupDel_JSP.TabDelGroup"):"Delete group"%>
				&nbsp;
			</td>
			<td width="7" background="<%= baseDir%>/include/img/shade.png">&nbsp;</td>  
		</tr>
		</table>
		<!-- tabs end -->
		
		<!-- rahmen start -->
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
			<td width="10" class="td-back-lc">&nbsp;</td>
			<td class="td-back-lc" height="8"><img src="<%= baseDir%>/include/img/dummy.png" width="1" height="5">
			
				<!-- individual tags start -->
       			<table border="0" cellspacing="0" cellpadding="0" class="lc" width="100%">
				<tr>
					<td class="td-box-brown" colspan="2">&nbsp;
					<%= messageBundle!=null?messageBundle.getString("ConfirmGroupDel_JSP.Form.DelGroupHeadline"):"Delete group"%>
					</td>
				</tr>
				<tr>
					<td colspan="3" class="td-back-lc">
						<img src="<%= baseDir%>/include/img/dummy.png" width="1" height="50">
					</td>
				</tr>
				</table>
				<table border="0" cellspacing="0" cellpadding="0" width="100%" boder="0"> 
				<tr>
					<td width="100%" align="left" valign="top" class="td-feld-inhalt">
						<table border="0" cellspacing="5" cellpadding="0" class="lc" width="100%"> 
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("ConfirmGroupDel_JSP.CaptionGroup"):"Group"%>
								<%= gid %>: <%= messageBundle!=null?messageBundle.getString("ConfirmGroupDel_JSP.DelGroupText"):"Actually delete group?"%>
							</td>
						</tr>
						</table>	
					</td>
					<td width="10" class="td-back-lc">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3" class="td-back-lc">
						<img src="<%= baseDir%>/include/img/dummy.png" width="1" height="50">
					</td>
				</tr>
				</table>

				<!-- Button insert and abort -->
				<table border="0" cellspacing="0" cellpadding="2" width="100%">
				<tr>
					<td widht="5" colspan="3"><img src="<%= baseDir%>/include/img/dummy.png" width="1" height="5"></td>
				</tr>
				<tr>	
					<td width="90%">&nbsp;
				   <input class="button-submit" type="button" name="button" value="<%= messageBundle!=null?messageBundle.getString("ConfirmGroupDel_JSP.Form.DoDelete"):"Delete"%>" onClick="location.href='<%= response.encodeURL(groupServletURL+"?action=delGroup&group="+gid) %>&confirm=true';">
				   <input class="button-submit" type="button" name="button" value="<%= messageBundle!=null?messageBundle.getString("ConfirmGroupDel_JSP.Form.DoCancel"):"Cancel"%>" onClick="location.href='<%= response.encodeURL(groupServletURL+"?action=groupDetail&group="+gid)%>';">				   
					</td>
				</tr>
			    </table>
				<!-- rahmen end -->
	
			</td>
			<td width="25">&nbsp;</td>
		</tr>
		</table>


<%@ include file="/include/jsp/footer.inc.jsp" %>