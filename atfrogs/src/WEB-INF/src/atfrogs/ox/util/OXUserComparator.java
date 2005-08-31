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
 * Created on 03.06.2005
 *
 */
package atfrogs.ox.util;

import java.util.Comparator;
import atfrogs.ox.objects.*;

/**
 *
 */
public class OXUserComparator implements Comparator {
	public final static int UID = 0;
	public final static int SURNAME_FIRSTNAME = 1;
	public final static int FIRSTNAME_SURNAME = 2;
	
	private final static int MAX_CRITERIA = FIRSTNAME_SURNAME;
	
	private int comparisonCriteria; // note: must match last enum id from above
	
	/**
	 * Default constructor which construct object that compares 
	 * user objects by uid.
	 */
	public OXUserComparator() {
		this.comparisonCriteria = OXUserComparator.UID;
	}
	
	/**
	 * Constructor which construct object that compares 
	 * user objects by given given criteria.
	 */
	public OXUserComparator(int comparisonCriteria) 
	throws IllegalArgumentException{
		if(comparisonCriteria < 0 || comparisonCriteria > MAX_CRITERIA)
			throw new IllegalArgumentException("Invalid comaprison criteria " + 
												comparisonCriteria);
		
		this.comparisonCriteria = comparisonCriteria;
	}	

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		OXUser u1, u2;
		String string1 = null, string2 = null;
			
		// note: following operations may throw a ClassCastException
		u1 = (OXUser) o1;
		u2 = (OXUser) o2;
		
		switch(this.comparisonCriteria){
		case SURNAME_FIRSTNAME:
			string1 = (u1.getSurname()+u1.getFirstname()).toLowerCase();
			string2 = (u2.getSurname()+u2.getFirstname()).toLowerCase();
			break;			
		case FIRSTNAME_SURNAME:
			string1 = (u1.getFirstname()+u1.getSurname()).toLowerCase();
			string2 = (u2.getFirstname()+u2.getSurname()).toLowerCase();
			break;			
		default:
			string1 = (u1.getUid()).toLowerCase();
			string2 = (u2.getUid()).toLowerCase();
			break;
		}
		
		return string1.compareTo(string2);
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		OXUserComparator c;
		
		if(this == obj) return true;
		if(obj.getClass() != this.getClass()) return false;
		
		c = (OXUserComparator)obj;
		
		return this.comparisonCriteria == c.comparisonCriteria;
	}
}

