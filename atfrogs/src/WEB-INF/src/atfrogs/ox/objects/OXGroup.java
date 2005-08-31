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
 * Created on 28.05.2005
 *
 */
package atfrogs.ox.objects;

import java.util.*;

/**
 * Class represents an OX group.
 */
public class OXGroup {
	private String gid;
	private List members; // of OXUser
	
	/**
	 * Construct a new group with given group id.
	 * 
	 * @param gid the group id.
	 */
	public OXGroup(String gid){
		// TODO: check parameter
		this.gid = new String(gid);
		this.members = new Vector();
	}
	
	/**
	 * Add list of OXUser objects to group. 
	 * 
	 * @param users the list of users.
	 */
	public void addUsers(List users){
		// TODO: check parameters
		this.members.addAll(users);
	}
	
	/**
	 * Add single OXUser object to group.
	 * 
	 * @param user the new user.
	 */
	public void addUser(OXUser user){
		this.members.add(user); // TODO: clone?
	}
	
	/**
	 * Returns the group id.
	 * 
	 * @return the group id.
	 */
	public String getGid(){
		return new String(this.gid);
	} 
	
	/**
	 * Returns the list of OXUser objects that are member of group.
	 * 
	 * @return the list of users.
	 */
	public List getMemberList(){
		return this.members; // TODO: clone
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new String("OXGroup: " + this.gid + "(gid), " +
				this.members.size() + "members");
	}
}
