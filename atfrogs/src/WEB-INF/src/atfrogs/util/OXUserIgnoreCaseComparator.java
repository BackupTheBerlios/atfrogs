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
 * Created on 10.06.2005
 */
package atfrogs.util;

import java.util.Comparator;
import atfrogs.ox.objects.*;

/* TODO is this class actually necessary? A class with equal functionality
*       is located in atfrogs.ox.util.

/**
 *
 * For comparing two OXUser-objects.
 */
public class OXUserIgnoreCaseComparator implements Comparator{
	public OXUserIgnoreCaseComparator(){
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		OXUser user1, user2;
		String name1, name2;
			
		// note: following operations may throw a ClassCastException
		user1 = (OXUser) o1;
		user2 = (OXUser) o2;
		
		name1 = user1.getName().toLowerCase();
		name2 = user2.getName().toLowerCase();

		return name1.compareTo(name2);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		
		if(this == obj) return true;
		if(obj.getClass() != this.getClass()) return false;
		
		return true;
	}
	
}
