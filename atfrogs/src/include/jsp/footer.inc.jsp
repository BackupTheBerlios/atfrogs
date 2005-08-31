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

<%
List stdOutList = (List)request.getAttribute("stdOutList");
%>

<% if(stdOutList!=null) {%>
<HR />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tbody>
<tr><td align=center><%= messageBundle!=null?messageBundle.getString("Footer_JSP.CaptionConsole"):"Console Output"%></td></tr>
<tr><td align=center>
<textarea rows="7" cols="85" wrap="virtual" class="small_input_border"  
name="konsoleOut"><%
for(int i=0; i<stdOutList.size(); i++){
	String line = (String)stdOutList.get(i);
	%><%= line + "\n"%><%
}
%></textarea>
</td></tr></tbody>
</table>
<% } %>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tbody>
<tr><td align=center>
<% 
	// will be replaced with actual version by deploy script
	String version="";
%>

<small>Version: ATFrogs <%= version+"" %></small>
</td></tr></tbody>
</table>

</BODY>
</HTML>