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

import java.io.*;

/**
 * Filters substrings of starting with '[' and ending with 'm' out of a
 * character stream. These kind of tags are used with ANSI codes to colorize
 * console output.  
 *    
 */
public class FilterANSI extends FilterReader
{
	/**
	 * Constructs an ANSI filter for reader @a in.
	 *   
	 * @param in the reader to be filtered. 
	 */
	public FilterANSI( Reader in )
	{
		super( in );
	}

	/**
	 * Read next byte.
	 * 
	 * @return the next byte.
	 */
	public int read() throws IOException
	{
		char buf[] = new char[1];
		return read(buf, 0, 1) == -1 ? -1 : buf[0];
	}

	/**
	 * Read next @a len bytes and write them to array @a cbfuf starting
	 * starting at array index @a off. 
	 * 
	 */
	public int
	read( char[] cbuf, int off, int len ) throws IOException
	{
		int numchars = 0;
		
		while ( numchars == 0 )
		{
			numchars = in.read( cbuf, off, len );
			
			if ( numchars == -1 )  // EOF?
				return -1;
			
			int last = off;
			
			for( int i = off; i < off + numchars; i++ )
			{
				if ( !intag ) {
					if ( cbuf[i] == '[' )
						intag = true;
					else
						cbuf[last++] = cbuf[i];
				}
				else if (cbuf[i] == 'm')
					intag = false;
			}
			numchars = last - off;
		}
		return numchars;
	}
	
	private boolean intag = false;
}