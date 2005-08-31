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

import java.util.List;
import java.util.Vector;

/**
 * Class representing an OX resource group
 */
public class OXResourceGroup {
	private String gid;
	private List resources; // of OXResource
	
	/**
	 * Construct a new resource group with given group id.
	 * 
	 * @param gid the group id.
	 */
	public OXResourceGroup(String gid){
		// TODO: check parameter
		this.gid = new String(gid);
		this.resources = new Vector();
	}
	
	/**
	 * Add list of OXResource objects to resource group. 
	 * 
	 * @param resources the list of resources.
	 */
	public void addResources(List resources){
		// TODO: check parameters
		this.resources.addAll(resources);
	}
	
	/**
	 * Add single OXResource object to resource group.
	 * 
	 * @param resource the new resource.
	 */
	public void addResource(OXResource resource){
		this.resources.add(resource); // TODO: clone?
	}
	
	/**
	 * Returns the resource group id.
	 * 
	 * @return the resource group id.
	 */
	public String getGid(){
		return new String(this.gid);
	} 
	
	/**
	 * Returns the list of OXResource objects that are member of 
	 * resource group.
	 * 
	 * @return the list of resources.
	 */
	public List getMemberList(){
		return this.resources; // TODO: clone
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new String("OXResGroup: " + this.gid + "(gid), " +
				this.resources.size() + "resources");
	}
}
