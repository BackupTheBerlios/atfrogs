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
 * Class represents an OX user.
 */
public class OXUser {
	private String uid;
	private String surname;
	private String firstname;
	private String mail;
	
	/**
	 * Construct a new user with given user id. 
	 * Surname and firstname are assigned the empty String.
	 * 
	 * @param uid
	 */
	public OXUser(String uid){
		// TODO: check parameters
		this.uid = new String(uid);
		this.surname = ""; // null?
		this.firstname = ""; // null?
	}
	
	/**
	 * Construct a new user with given user id, surname and firstname.
	 * 
	 * @param uid the user id.
	 * @param surname the surname
	 * @param firstname the firstname.
	 */
	public OXUser(String uid, String surname, String firstname){
		// TODO: check parameters
		this(uid);
		this.setSurname(surname);
		this.setfirstname(firstname);
	}

	/**
	 * Construct a new user with given user id, surname, firstname and mail.
	 * 
	 * @param uid the user id.
	 * @param surname the surname
	 * @param firstname the firstname.
	 * @param mail the mail.
	 */
	public OXUser(String uid, String surname, String firstname, String mail){
		// TODO: check parameters
		this(uid);
		this.setSurname(surname);
		this.setfirstname(firstname);
		this.setMail(mail);
	}
	
	/**
	 * Sets the mail. 
	 */
	public void setMail(String mail){
		this.mail = mail;
	}
	
	/**
	 * @return Return the mail.
	 */
	public String getMail(){
		return this.mail;
	}
	
	/**
	 * Assign surname to user.
	 * 
	 * @param surname the surname.
	 */
	public void setSurname(String surname){
		// TODO: check parameter
		this.surname = new String(surname);
	}
	
	/**
	 * Assign firstname to user.
	 * 
	 * @param firstname the firstname.
	 */
	public void setfirstname(String firstname){
		// TODO: check parameter
		this.firstname = new String(firstname);
	}
	
	/**
	 * Returns the user id.
	 * 
	 * @return the user id.
	 */
	public String getUid(){
		return new String(this.uid);
	}
	
	/**
	 * Returns the user's surname.
	 * 
	 * @return the surname.
	 */
	public String getSurname(){
		return new String(this.surname);
	}
	
	/**
	 * Returns the user's firstname.
	 * 
	 * @return the firstname.
	 */
	public String getFirstname(){
		return new String(this.firstname);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return new String("OXUser: " + this.uid + "(uid)," + 
				this.surname + "(surname)," + this.firstname +"(firstname)");
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		OXUser user;
		
		if(obj == this) return true;
		if(obj.getClass() != this.getClass()) return false;
		
		user = (OXUser) obj;
		
		return this.uid.equals(user.uid) && 
			this.firstname.equals(user.firstname) &&
			this.surname.equals(user.surname);
	}
	
	/**
	 * @return Returns the full name.
	 */
	public String getName(){
		return this.surname + " " + this.firstname;
	}
}
