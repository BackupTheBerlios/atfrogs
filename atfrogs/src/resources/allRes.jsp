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
	request.setAttribute("extraTitle", "Resources");
	request.setAttribute("extraMeta", /* add content String */"");
	request.setAttribute("extraBody", /* add content String */"");
%>

<%  // retrieve objects from servlet
	List resIdList = (List)request.getAttribute("resIdList");
%>

<%@ include file="/include/jsp/header.inc.jsp" %>

<!-- categorie=group -->
<form action="<%= response.encodeURL(resourceServletURL) %>" method="post">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tbody><tr>
	<td colspan="3" height="8"><img src="<%= baseDir%>/include/img/dummy.png" height="8" width="1"></td>
</tr>
<tr>
	<td width="25">&nbsp;</td>
	<td>
	&nbsp;
	</td>
	<td width="25">&nbsp;</td>
</tr>
<tr>
	<td colspan="3" height="8"><img src="<%= baseDir%>/include/img/dummy.png" height="8" width="1"></td>
</tr>
<tr>
	<td width="25"><img src="<%= baseDir%>/include/img/dummy.png" height="1" width="25"></td>
	<td>
         
		<!-- tabs start -->
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
			<td class="reiter_d">
				&nbsp;&nbsp;
				<a class="reiter_d" href="<%= response.encodeURL(resourceServletURL+"?action=allResGroups") %>">
				<%= messageBundle!=null?messageBundle.getString("AllRes_JSP.TabResGrp"):"Resourcegroups"%>
				</a>
				&nbsp;
			</td>			
			<td background="<%= baseDir%>/include/img/shade.png" width="7">
				&nbsp;
			</td>
			<td class="reiter_a">
				&nbsp;&nbsp;
				<%= messageBundle!=null?messageBundle.getString("AllRes_JSP.TabRes"):"Resources"%>
				&nbsp;
			</td>    		
			<td background="<%= baseDir%>/include/img/shade.png" width="7">
				&nbsp;
			</td>
		</tr></tbody>
		</table>
		<!-- tabs end -->	
	
	</td>
	<td width="25">&nbsp;</td>
</tr>
<tr>
	<td width="25"><img src="<%= baseDir%>/include/img/dummy.png" height="1" width="25"></td>
	<td>
	
	<!-- rahmen start -->
	<table border="0" cellpadding="0" cellspacing="0" width="100%"><tbody>
	<tr>
    	<td class="td-back-lc" colspan="3" height="5"><img src="<%= baseDir%>/include/img/dummy.png" height="5" width="1"></td>
    </tr>
    <tr>
    	<td class="td-back-lc" width="10"><img src="<%= baseDir%>/include/img/dummy.png" height="1" width="10"></td>
		<td>
		
      	<!-- tab title start -->
      	<table border="0" cellpadding="0" cellspacing="0" width="100%"><tbody>
      	<tr>
        	<td>
         	<table border="0" cellpadding="0" cellspacing="0" width="100%"><tbody>
         	<tr>
         		<td class="td-title-lc" align="left" width="20%">&nbsp;</td>
         		<td class="td-title-lc" align="center" width="60%">&nbsp;</td>
				<td class="td-title-lc" align="right" nowrap="nowrap">
					<a href="<%= response.encodeURL(resourceServletURL+"?action=new") %>"><img src="<%= baseDir%>/include/img/item_new.png" alt="
					<%= messageBundle!=null?messageBundle.getString("AllRes_JSP.NewIcon"):"New resource or resourcegroup"%>
					" title="
					<%= messageBundle!=null?messageBundle.getString("AllRes_JSP.NewIcon"):"New resource or resourcegroup"%>
					" border="0">&nbsp;&nbsp;&nbsp;</a>
					&nbsp;&nbsp;&nbsp;<img src="<%= baseDir%>/include/img/hox.png">&nbsp;</td>
			</tr>
			</tbody></table>
        	</td>
		</tr>
		
		<!-- individual tags start -->
		<tr>
			<td class="td-back-lc" height="10"><img src="<%= baseDir%>/include/img/dummy.png" height="10" width="1"></td>
		</tr>
		<tr>
			<td>
			<table cellpadding="5" cellspacing="0" width="100%"><tbody>
			<tr>
				<td colspan="3" class="td-box-brown">&nbsp;</td>
			</tr>
			<tr>
				<td class="list-table-header-left" width="1%">&nbsp;</td>
				<td class="list-table-header-right" nowrap="nowrap" width="94%">
				<%= messageBundle!=null?messageBundle.getString("AllRes_JSP.ResName"):"Resoucename"%>
				&nbsp;&nbsp;&nbsp;</td>
			</tr>

			<% // list resources
				if (resIdList != null)
				for(int i=0; i<resIdList.size(); i++){
				Object listObj = resIdList.get(i);
				if(listObj == null){
					%>null<%
				}else{
				String uid = (String)listObj;
			%>

			<tr>
				<td class="list-table-start-ws"><a
				href="<%=response.encodeURL(resourceServletURL)%>?action=delRes&res=<%=
				uid %>"><img width="11" height="11" src="<%= baseDir%>/include/img/item_delete.png" border="0"></a></td>
				<td class="list-table-end-ws">
				<!-- <a href="<%=response.encodeURL(resourceServletURL)%>?action=delRes&res=<%= uid %>"> -->
				<%= uid %>
				<!-- </a> -->
				&nbsp;</td>
			</tr>
			
			<% }} %>

			</tbody>
			</table>
			</td>
		</tr> 
		<!-- individual tags end -->
		</tbody></table>
		<!-- tab title end -->
		</td>
		<td class="td-back-lc" width="10"><img src="<%= baseDir%>/include/img/dummy.png" height="1" width="10"></td>
	</tr>
    <tr>
		<td colspan="3" class="td-back-lc" height="10"><img src="<%= baseDir%>/include/img/dummy.png" height="10" width="1"></td>
	</tr></tbody>
	</table>
	<!-- rahmen end -->
	</td>
	<td width="25">&nbsp;</td>
</tr>
<tr>
	<td colspan="3" height="8"><img src="<%= baseDir%>/include/img/dummy.png" height="8" width="1"></td>
</tr></tbody>
</table>
</form>

<%@ include file="/include/jsp/footer.inc.jsp" %>
