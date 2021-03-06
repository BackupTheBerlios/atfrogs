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

/**
 * Class representing an OX resource. 
 */
public class OXResource {
	private String rid;
	
	/**
	 * Construct a new user with given user id. 
	 * Surname and firstname are assigned the empty String.
	 * 
	 * @param rid
	 */
	public OXResource(String rid){
		// TODO: check parameters
		this.rid = new String(rid);
	}
	
	/**
	 * Returns the resource id.
	 * 
	 * @return the resource id.
	 */
	public String getRid(){
		return new String(this.rid);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return new String("OXResource: " + this.rid + "(rid)");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		OXResource user;
		
		if(obj == this) return true;
		if(obj.getClass() != this.getClass()) return false;
		
		user = (OXResource) obj;
		
		return this.rid.equals(user.rid);
	}
}
