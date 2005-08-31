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
 * Created on 02.06.2005
 *
 */
package atfrogs.ox.api;

/**
 *
 */
public class OXApiException extends Exception {

	/**
	 * 
	 */
	public OXApiException() {
		super();
	}

	/**
	 * @param message the message.
	 */
	public OXApiException(String message) {
		super(message);
	}

	/**
	 * @param cause the cause.
	 */
	public OXApiException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message the message.
	 * @param cause the cause.
	 */
	public OXApiException(String message, Throwable cause) {
		super(message, cause);
	}

}
