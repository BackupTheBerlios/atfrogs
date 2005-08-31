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
package atfrogs.ox.api;

import java.util.*;
import atfrogs.ox.objects.*;

/**
 * API to get information about groups/ resources etc. from open-xchange
 * server.
 *  
 * All lists returned, containing Strings or OX-objects are unsorted. 
 * You may use the Collections.sort method by means of our Comparator classes
 * in atfrogs.ox.util to sort the list after retrieving. 
 */
public class OXApi {
	private String ServerUrl;
	private String username;
	private String password;
	
	private OXWebDAVApi webdavApi;
	
	private boolean debug = false;
	
	/**
	 * Construct an api object to connect to an open-xchange server with
	 * given url username and password. The user must be a registered 
	 * OX user.
	 * 
	 * @param serverUrl url of ox-server.
	 * @param username the username.
	 * @param password the password.
	 */
	public OXApi(String serverUrl, String username, String password){
		this.ServerUrl = new String(serverUrl);
		this.username = new String(username);
		this.password = new String (password);
		
		this.webdavApi = new OXWebDAVApi(serverUrl, username, password);
	}
	
	
	/**
	 * Returns list containing all group ids as String.
	 * 
	 * @return the list of group ids.
	 * @throws OXApiException if an error occurs during operation.
	 */
	public List getGroupIdList() throws OXApiException{ 
		List gidList;
		
		gidList = this.webdavApi.getGroupIdList();
		return gidList;
	}
	
	/**
	 * Returns list containing all resource group ids as String.
	 * 
	 * @return the list of resource group ids.
	 * @throws OXApiException if an error occurs during operation.
	 */
	public List getResGroupIdList() throws OXApiException{ 
		List resGidList;
		
		resGidList = this.webdavApi.getResGroupIdList();
		Collections.sort(resGidList);
		return resGidList;
	}	
	
	/**
	 * Returns list containing all resource ids as String.
	 * 
	 * @return the list of resource ids.
	 * @throws OXApiException if an error occurs during operation.
	 */
	public List getResIdList() throws OXApiException{ 
		List resIdList;
		
		resIdList = this.webdavApi.getResIdList();
		Collections.sort(resIdList);
		return resIdList;
	}	
	
	/**
	 * To search for OX users with a given search pattern.
	 * 
	 * @param pattern the search pattern.
	 * @return the list of users.
	 * @throws OXApiException if an error occurs during operation.
	 */
	public List getUsersByPattern(String pattern) throws OXApiException{
		return this.webdavApi.getUsersByPattern(pattern);
	}
	
	/**
	 * To search for OX resources with a given search pattern.
	 * 
	 * @param pattern the search pattern.
	 * @return the list of resources(strings).
	 * @throws OXApiException if an error occurs during operation.
	 */
	public List getResourcesByPattern(String pattern) throws OXApiException{
		return this.webdavApi.getResourcesByPattern(pattern);
	}
	
	/**
	 * Returns lists containing all OX users in group with given gid.
	 * 
	 * @param gid the group.
	 * @return the list of users.
	 */
	public List getGroupMembers(String gid) throws OXApiException{
		return this.webdavApi.getMembersOfGroup(gid);
	}
	
	/**
	 * Returns list containing uids all users in group with given gid.
	 * 
	 * @param gid the group.
	 * @return list containing all uids.
	 * @throws OXApiException if an error occurs during operation.
	 */
	public List getGroupMemberIds(String gid) throws OXApiException{
		return this.webdavApi.getMemberIdsOfGroup(gid);
	}
	
	/**
	 * Get union set of all members in groups with given ids.
	 * 
	 * @param gids array of gids.
	 * @return the list of users.
	 * @throws OXApiException if an error occurs during operation.
	 */
	public List getGroupsMembers(String[] gids) throws OXApiException{
		Set userSet;
		List oxUserList;
		
		userSet = new TreeSet(); // to avoid duplicate entries
		oxUserList = new Vector();
		
		for(int i=0; i<gids.length; i++){
			userSet.addAll(this.getGroupMemberIds(gids[i]));
		}
		
		
		for(Iterator i=userSet.iterator(); i.hasNext();){
			OXUser user;
			
			user = this.webdavApi.getOXUser((String)i.next());
			oxUserList.add(user);
		}
		
		return oxUserList;
	}
	
	/**
	 * Get union set of all resources in resourcegroups with given ids.
	 * 
	 * @param resgids array of resgids.
	 * @return the list of resources.
	 * @throws OXApiException if an error occurs during operation.
	 */
	public List getResGroupsResources(String[] resgids) throws OXApiException{
		Set resSet;
		List resList;
		
		resSet = new TreeSet(); // to avoid duplicate entries
		resList = new Vector();
		
		for(int i=0; i<resgids.length; i++){
			resSet.addAll(this.webdavApi.getResourceIdsOfResGroup(resgids[i]));
		}
		
		for(Iterator i=resSet.iterator(); i.hasNext();){
			OXResource resource;
			
			resource = this.webdavApi.getOXResource((String)i.next());
			resList.add(resource);
		}
		
		return resList;
	}
	
	/**
	 * Returns the OXGroup with given gid.
	 * 
	 * @param gid the gid.
	 * @return the OxGroup object.
	 * @throws OXApiException if an error occurs during operation.
	 */
	public OXGroup getGroup(String gid) throws OXApiException{
		OXGroup newGroup;
		List groupMembers;
		
		newGroup = new OXGroup(gid);
		groupMembers = this.webdavApi.getMembersOfGroup(gid);
		
		// TODO: assert groupMembers != null;
		
		newGroup.addUsers(groupMembers);
		
		return newGroup;
	}
	
	/**
	 * Returns the OXResourceGroup with given resGid.
	 * 
	 * @param resGid the resGid.
	 * @return the OxResourceGroup object.
	 * @throws OXApiException if an error occurs during operation.
	 */
	public OXResourceGroup getResGroup(String resGid) throws OXApiException{
		OXResourceGroup newResGroup;
		List resGroupMembers;
		
		newResGroup = new OXResourceGroup(resGid);
		resGroupMembers = this.webdavApi.getResourcesOfResGroup(resGid);
		
		// TODO: assert resGroupMembers != null;
		
		newResGroup.addResources(resGroupMembers);
		
		return newResGroup;
	}
	
	/**
	 * Returns list containing all user as String.
	 * 
	 * @return the list of user ids.
	 * @throws OXApiException if an error occurs during operation.
	 */
	public List getUserIdList() throws OXApiException{
		List uidList;
		
		uidList = this.webdavApi.getUserIdList();
		return uidList;
	}
	
	public OXUser getUser(String uid) throws OXApiException{
		return this.webdavApi.getOXUser(uid);
	}
	
	/**
	 * Enable debug output to stdout.
	 * 
	 * @param debug The debug to set.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
		if(this.webdavApi != null)
			this.webdavApi.setDebug(debug);
	}
}
