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
	request.setAttribute("extraTitle", "User");
	request.setAttribute("extraMeta", /* add content String */"");
	request.setAttribute("extraBody", /* add content String */"");
%>

<%@ include file="/include/jsp/header.inc.jsp" %>

<form ACTION="<%= response.encodeURL(userServletURL) %>" METHOD="POST">
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
				<a class="reiter_d" href="<%= response.encodeURL(userServletURL+"?action=showUser") %>">
				<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.TabUser"):"Users"%>
				</a>
				&nbsp;
			</td>
			<td background="<%= baseDir%>/include/img/shade.png" width="7">
				&nbsp;
			</td>
			<td class="reiter_a">
				&nbsp;&nbsp;
				<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.TabNewUser"):"New User"%>
				&nbsp;
			</td>
			<td background="<%= baseDir%>/include/img/shade.png" width="7">
				&nbsp;
			</td>
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
					<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.NewText"):"Create new user"%>
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
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Uid"):"Uid"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="uid" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="1" value="<%=request.getAttribute("uid")==null?"":request.getAttribute("uid")%>">
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Passwd"):"Password"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="passwd" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="2" value="<%=request.getAttribute("passwd")==null?"":request.getAttribute("passwd")%>">
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Firstname"):"First name"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="firstname" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="3" value="<%=request.getAttribute("firstname")==null?"":request.getAttribute("firstname")%>">
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Name"):"Name"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="name" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="4" value="<%=request.getAttribute("name")==null?"":request.getAttribute("name")%>">
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Time"):"Timezone"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="time" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="6" value="<%=request.getAttribute("time")==null?"":request.getAttribute("time")%>">
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Lang"):"Language"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="lang" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="7" value="<%=request.getAttribute("lang")==null?"":request.getAttribute("lang")%>">
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Mailenabled"):"E-Mail enabled"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="mailenable" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="8" value="<%=request.getAttribute("mailenable")==null?"":request.getAttribute("mailenable")%>">
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Inetmail"):"Inetmail"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
                        		<select name="inetmail" size="1" tabindex="9">
                                	<option <%=((request.getAttribute("inetmail")!=null) && (request.getAttribute("inetmail").equals("TRUE")))?"selected":""%>
                                	 value="TRUE">TRUE</option>
                                	<option <%=((request.getAttribute("inetmail")!=null) && (request.getAttribute("inetmail").equals("FALSE")))?"selected":""%>
                                	 value="FALSE">FALSE</option>
	                        	</select>
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Appdays"):"OX appointment days"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="appdays" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="10" value="<%=request.getAttribute("appdays")==null?"":request.getAttribute("appdays")%>">
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Globaladr"):"Write global address"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
                        		<select name="globaladr" size="1" tabindex="11">
                                	<option <%=((request.getAttribute("globaladr")!=null) && (request.getAttribute("globaladr").equals("TRUE")))?"selected":""%>
                                	 value="TRUE">TRUE</option>
                                	<option <%=((request.getAttribute("globaladr")!=null) && (request.getAttribute("globaladr").equals("FALSE")))?"selected":""%>
                                	 value="FALSE">FALSE</option>
	                        	</select>
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Shell"):"Shell"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="shell" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="12" value="<%=request.getAttribute("shell")==null?"":request.getAttribute("shell")%>">
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Maildomain"):"E-Mail domain"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="maildomain" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="17" value="<%=request.getAttribute("maildomain")==null?"":request.getAttribute("maildomain")%>">
							</td>
						</tr>
						<tr>
							<td class="td-feld-inhalt" nowrap>
								&nbsp;
								<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.Taskdays"):"OX task days"%>
							</td>
							<td class="td-feld-inhalt" width="99%">
								<input type="text" size="30" class="small_input_border" name="taskdays" <%=request.getAttribute("errorMsg")!=null?"style=\"background-color:red\"":""%> tabindex="18" value="<%=request.getAttribute("taskdays")==null?"":request.getAttribute("taskdays")%>">
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
					<td width="90%">&nbsp;</td>
				    <td align="right"><INPUT TYPE="SUBMIT" CLASS="button-submit" NAME="act" VALUE="<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.DoNew"):"New"%>"></td>
			        <td align="right"><INPUT TYPE="button" CLASS="button-submit" NAME="act" VALUE="<%= messageBundle!=null?messageBundle.getString("NewUser_JSP.Form.DoCancel"):"Cancel"%>" onClick="location.href='<%= response.encodeURL(userServletURL) %>';"></td>
				</tr>
			    </table>
				<!-- rahmen end -->
	
			</td>
			<td width="25">&nbsp;</td>
		</tr>
		</table>
</form>

<%@ include file="/include/jsp/footer.inc.jsp" %>