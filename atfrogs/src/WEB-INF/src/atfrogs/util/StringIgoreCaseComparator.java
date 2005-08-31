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
 */
package atfrogs.util;

import java.util.Comparator;


/**
 * For String comparison ignorig case.
 */
public class StringIgoreCaseComparator implements Comparator {
	
	/**
	 * Default constructor which construct object that compares 
	 * two strings ignoring case.
	 */
	public StringIgoreCaseComparator() {
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		String s1, s2;
		String string1 = null, string2 = null;
			
		// note: following operations may throw a ClassCastException
		s1 = (String) o1;
		s2 = (String) o2;
		
		string1 = s1.toLowerCase();
		string2 = s2.toLowerCase();

		return string1.compareTo(string2);
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		String s;
		
		if(this == obj) return true;
		if(obj.getClass() != this.getClass()) return false;
		
		return true;
	}
}
