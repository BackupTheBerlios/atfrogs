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
<%@ page import="atfrogs.util.*" %>

<%
	// here you may add additional content to meta, title and body tags:
	request.setAttribute("extraTitle", "Resources");
	request.setAttribute("extraMeta", /* add content String */"");
	request.setAttribute("extraBody", /* add content String */"");
%>

<%  // retrieve objects from servlet
	OXResourceGroup resGroup    = (OXResourceGroup)request.getAttribute("resGroup");
	List allResGroupIds         = (List)request.getAttribute("allResGroupIds");
	List searchResultList       = (List)request.getAttribute("searchResultList");
	List lastExtractedResGroups = (List)request.getAttribute("lastExtractedResGroups");
%>

<%@ include file="/include/jsp/header.inc.jsp" %>


<form action="<%= response.encodeURL(resourceServletURL) %>" method="post">

<input name="action" value="resGroupDetail" class="input-search" type="hidden">
<input name="resgroup" value="<%= resGroup.getGid() %>" class="input-search" type="hidden">

<%//test for passed lastSearch and lastExtract
String lastSearch  = (String)request.getAttribute("lastSearch");
String lastExtract = (String)request.getAttribute("lastExtract"); // @todo: change to String[]

if (lastSearch != null){%>
	<input name="lastSearch" value="<%=lastSearch%>" class="input-search" type="hidden">
<%} else if (lastExtract != null){%>
	<input name="lastExtract" value="<%=lastExtract%>" class="input-search" type="hidden">
<%}%>

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
				<a class="reiter_d" href="<%= response.encodeURL(resourceServletURL+"?action=allResGroups") %>">
				<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.TabResGrps"):"Resourcegroups"%>
				</a>
				&nbsp;
			</td>			
			<td background="<%= baseDir%>/include/img/shade.png" width="7">
				&nbsp;
			</td>
			<td class="reiter_d">
				&nbsp;&nbsp;
				<a class="reiter_d" href="<%= response.encodeURL(resourceServletURL+"?action=allRes") %>">
				<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.TabRes"):"Resources"%>
				</a>
				&nbsp;
			</td>			
			<td background="<%= baseDir%>/include/img/shade.png" width="7">
				&nbsp;
			</td>
			<td class="reiter_a">
				&nbsp;&nbsp;
				<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.TabDetails"):"Resourcegroupdetails"%>
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
						&nbsp;<%= resGroup.getGid() %>
					</td>
					<td class="td-title-lc" align="right" nowrap="nowrap" width="40%">
						<a href="<%= response.encodeURL(resourceServletURL+"?action=new") %>">
						<img src="<%= baseDir%>/include/img/item_new.png" alt="
						<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.NewIcon"):"New resource or resourcegroup"%>
						" title="
						<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.NewIcon"):"New resource or resourcegroup"%>
						" border="0">
						</a>
						&nbsp;&nbsp;&nbsp;
						<a href="<%= response.encodeURL(resourceServletURL+"?action=delResGroup&resgroup=" + resGroup.getGid()) %>">
						<img src="<%= baseDir%>/include/img/item_delete.png" BORDER="0" alt="
						<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.DeleteIcon"):"Delete resourcegroup"%>
						" title="
						<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.DeleteIcon"):"Delete resourcegroup"%>
						">
						</a>
						&nbsp;&nbsp;&nbsp;
						<img src="<%= baseDir%>/include/img/hox.png">
						&nbsp;
					</td>
				</tr></tbody>
				</table>
				<!-- tab title end -->
				
				<!-- PARTICIPANTS START -->
				<table border="0" cellpadding="0" cellspacing="0" width="100%"><tbody>
				<tr>
					<td class="td-box-brown" colspan="3">
						&nbsp;
						<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Res"):"Resources"%>
					</td>
				</tr>
				<tr>
					<td colspan="3" class="td-feld-inhalt" height="10">
						<img src="<%= baseDir%>/include/img/dummy.png" height="1" width="1">
					</td>
				</tr>
				<tr>
					<td class="td-feld-inhalt" width="10">
						<img src="<%= baseDir%>/include/img/dummy.png" height="1" width="1">
					</td>
					<td class="td-feld-inhalt">
						<table class="lc" border="0" cellpadding="0" cellspacing="2"><tbody>
						<tr>
							<td class="td-feld-inhalt">
								<b>
								<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.DoSearch"):"Search"%>
								&nbsp;</b>
							</td>
							<td class="td-feld-inhalt">
								<b>
								<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.ResGrp"):"Resourcegroups:"%>
								&nbsp;</b>
							</td>
							<td class="td-feld-inhalt">
								<b>
								<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.Action"):"Action:"%>
								&nbsp;</b>
							</td>
							<td class="td-feld-inhalt">
								<b>
								<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.Res"):"Resources:"%>
								&nbsp;</b>
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt">
								<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.DataSource"):"Data source:"%>
								&nbsp;<br>
								<select name="datenquelle" class="select-normal">
									<option value="system" selected="selected">
										<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.SysRes"):"System Resources"%>
									</option>
								</select>
							</td>
							<td class="td-feld-inhalt" rowspan="2">
								<select name="resgroupselect" class="select-multi-pg" multiple="multiple">
									<%
									if(allResGroupIds != null)
									for(int i=0; i<allResGroupIds.size(); i++){
										String resGid = (String)allResGroupIds.get(i);
										%><option value="<%= resGid %>" class="participants-groups" 
										<% if(lastExtract!=null &&  lastExtractedResGroups!= null && lastExtractedResGroups.contains(resGid)) out.print("selected"); %>>
										<%= resGid %>
										</option>									
									<%
									}
									%>
								</select>
							</td>
							<td class="td-feld-inhalt">
							</td>
							<td class="td-feld-inhalt" rowspan="5">
								<select name="selectedresults" class="select-multi-pr" multiple="multiple">
								<% 	List memberList = resGroup.getMemberList();
								   	Collections.sort(memberList, new OXResourceComparator(OXResourceComparator.RID));
									if(memberList!=null)
									for(int i=0; i<memberList.size(); i++){
										OXResource res = (OXResource)memberList.get(i);
										if(res == null){
											%>nix<br/><%
										}else{%>
											<option value="<%=res.getRid()%>" class="participants-users">
											<%=res.getRid()%>
											</option>			
										<%}
									}
								%>
								</select>
							</td>
						</tr>
 						<tr>
							<td class="td-feld-inhalt">
								<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.SearchText"):"Searchpattern:"%>
								&nbsp;<br>
								<input name="searchpattern" value="" class="input-search" type="text">
							</td>
						</tr>
 						<tr>
 							<td class="td-feld-inhalt">
								<input name="action-search" value="
								<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.DoSearch"):"Search"%>
								" class="button-search-move" type="submit">
							</td>
							<td class="td-feld-inhalt">
								<input name="action-extract" value="
								<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.DoExtract"):"Extract"%>
								" class="button-search-move" type="submit">
							</td>
						</tr>
 						<tr>
							<td class="td-feld-inhalt" colspan="2">
								<select name="searchresults" class="select-multi-psr" multiple="multiple">
								<% 
									if(searchResultList!=null)
									for(int i=0; i<searchResultList.size(); i++){
										OXResource res = (OXResource)searchResultList.get(i);
										if(res == null){
											%>nix<br/><%
										}else{%>
											<option value="<%=res.getRid()%>" class="participants-users">
											<%=res.getRid() %>
											</option>			
										<%}
									}
								%>
								</select>							
							</td>
							<td class="td-feld-inhalt">
								<input name="action-add" value="
								<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.DoAdd"):"Add &gt;"%>
								" class="button-search-move" type="submit">
								&nbsp;
								<br><br>
								<br><br>
								<input name="action-remove" value="
								<%= messageBundle!=null?messageBundle.getString("ResGrpDetails_JSP.Form.DoRemove"):"Remove &alt;"%>
								" class="button-search-move" type="submit">&nbsp;
							</td>
						</tr></tbody>
						</table>
					</td>
					<td class="td-feld-inhalt" width="10">
					</td>
				</tr>
				<tr>
					<td colspan="3" class="td-feld-inhalt">
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