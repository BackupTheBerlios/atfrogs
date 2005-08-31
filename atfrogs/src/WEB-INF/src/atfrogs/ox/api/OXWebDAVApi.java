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
 * Created on 29.05.2005
 */
package atfrogs.ox.api;

import java.io.*; 
import java.util.*;

import atfrogs.ox.objects.*;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.jdom.input.SAXBuilder;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.*;
import org.apache.commons.httpclient.methods.*;

/**
 * API to get information about groups/ resources etc. from open-xchange 
 * server through webdav interface.
 *  
 * All lists returned, containing Strings or OX-objects are unsorted. 
 * You may use the Collections.sort method by means of our Comparator classes
 * in atfrogs.ox.util to sort the list after retrieving. 
 * 
 * You shouldn't use this methods directly, use OXApi instead. 
 */
public class OXWebDAVApi {
	
	private HttpClient httpClient;
	
	private String ox_namespace = "http://www.open-xchange.org";
	private String ox_ns_prefix = "ox";
	
	private String ServerUrl;
	private String username;
	private String password;
	
	private int lastStatus; // last http status code
	
	private boolean debug = false;
	
	/**
	 * Construct a webdav api object to connect to an open-xchange server webdav
	 * servlet with given url username and password. The user must be a 
	 * registered OX user.
	 * 
	 * @param serverUrl url of ox-server.
	 * @param username the username.
	 * @param password the password.
	 */
	public OXWebDAVApi(String serverUrl, String username, String password) {
		this.ServerUrl = new String(serverUrl);
		this.username = new String(username);
		this.password = new String (password);
		
		this.lastStatus = -1;
		
		this.httpClient = new HttpClient();
		this.httpClient.getState().setCredentials(new AuthScope(null, -1), 
				new UsernamePasswordCredentials(this.username, this.password));		
	}
	
	/**
	 * Execute propfind method with given request body and return retrieved
	 * response body.
	 * 
	 * @param request the request body.
	 * @return the response body.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	public Document doPropFind(Document request) throws OXWebDAVApiException{
		Document doc = null;
		XMLOutputter xml_out;
		PropfindMethod propfind;
		File f_tmp;
		File f_response;
		FileOutputStream fos;
		FileInputStream fis;
		ByteArrayInputStream bais;
		ByteArrayOutputStream baos;
		byte[] responseBody;
		byte[] requestBody;

		
		try{
			baos = new ByteArrayOutputStream(); 
			
			XMLOutputter xo = new XMLOutputter();
			xo.output(request, baos);
			
			propfind = new PropfindMethod(this.ServerUrl);
			propfind.setDoAuthentication(true);
			
			bais = new ByteArrayInputStream(baos.toByteArray());
			if(this.debug){
				System.out.println("Webdav request:");
				System.out.println(baos.toString());
			}
			propfind.setRequestEntity(new InputStreamRequestEntity(bais)); 
			
			this.lastStatus = this.httpClient.executeMethod(propfind);
			
			if(this.debug){
				System.out.println("Webdav response:");
				//	 TODO: use propfind.getResponseBodyAsStream();
				System.out.println(propfind.getResponseBodyAsString());
			}
			
			SAXBuilder saxb = new SAXBuilder();
			doc = saxb.build(propfind.getResponseBodyAsStream());
		}catch (Exception exc){
			if(this.debug) exc.printStackTrace();
			System.out.println("NOTE: Error often due to wrong authentification. \n " +
					"Check username and password of user specified in web.xml and assert " +
					"that the user is in at least one group!");			
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return doc;		
	}
	
	/**
	 * Returns the xml request body to retrieve groups with given search pattern
	 * by means of webdav propfind.
	 * 
	 * @param group the group search pattern.
	 * @return the response body.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	private Document groupRequest(String group) throws OXWebDAVApiException{
		Document doc;
		Element e_propfind, e_prop, e_group;
		
		try{
			doc = new Document(new Element("propfind", 
					Namespace.getNamespace("D", "DAV:")));
			e_propfind = doc.getRootElement();
			e_propfind.addNamespaceDeclaration(
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			
			e_prop = new Element("prop", Namespace.getNamespace("D", "DAV:"));
			e_propfind.addContent(e_prop);
			
			e_group = new Element("group", 
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_group.addContent(group);
			e_prop.addContent(e_group);
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return doc;
	}
		
	/**
	 * Returns the xml request body to retrieve all groups by means of 
	 * webdav propfind.
	 * 
	 * @return the response body.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	private Document allGroupsRequest() throws OXWebDAVApiException{
		return this.groupRequest("*");
	}
	
	/**
	 * Returns the xml request body to retrieve the passed resources by means 
	 * of webdav propfind.
	 * 
	 * @param resId the resources to find.
	 * @return the response body.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	private Document resourceRequest(String resId)  throws OXWebDAVApiException{
		Document doc;
		Element e_propfind, e_prop, e_resource;
		
		try{
			doc = new Document(new Element("propfind", 
					Namespace.getNamespace("D", "DAV:")));
			e_propfind = doc.getRootElement();
			e_propfind.addNamespaceDeclaration(
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			
			e_prop = new Element("prop", Namespace.getNamespace("D", "DAV:"));
			e_propfind.addContent(e_prop);
			
			e_resource = new Element("resource", 
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_resource.addContent(resId);
			e_prop.addContent(e_resource);
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return doc;
	}
	
	/**
	 * Returns the xml request body to retrieve all resources by means of webdav propfind.
	 * 
	 * @return the response body.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	private Document allResourcesRequest()  throws OXWebDAVApiException{
		return this.resourceRequest("*");
	}
	
	/**
	 * Returns the xml request body to retrieve resource groups by means of webdav propfind.
	 * 
	 * @param resGroupId the resourcegroups to get.
	 * @return the response body.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	private Document resourcegroupRequest(String resGroupId) throws OXWebDAVApiException{
		Document doc;
		Element e_propfind, e_prop, e_resourcegroup;
		
		try{
			doc = new Document(new Element("propfind", Namespace.getNamespace("D", "DAV:")));
			e_propfind = doc.getRootElement();
			e_propfind.addNamespaceDeclaration(
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			
			e_prop = new Element("prop", Namespace.getNamespace("D", "DAV:"));
			e_propfind.addContent(e_prop);

			/* BEGIN: This must be here, else no resource group will be listed */
			e_resourcegroup = new Element("group", 
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_resourcegroup.addContent("*");
			e_prop.addContent(e_resourcegroup);
			/* END (seems to be a bug)*/
			
			e_resourcegroup = new Element("resourcegroup", 
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_resourcegroup.addContent(resGroupId);
			e_prop.addContent(e_resourcegroup);
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return doc;
	}
	
	/**
	 * Returns the xml request body to retrieve all resource groups by means 
	 * of webdav propfind.
	 * 
	 * @return the response body.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	private Document allResourcegroupsRequest() throws OXWebDAVApiException {
		return this.resourcegroupRequest("*");
	}

	/**
	 * Returns a list of all resource ids of resource group members of resource 
	 * group with given gid.
	 * You should not pass a pattern as gid. If done so you'll get the
	 * list of uids of the group being the first search result.
	 * 
	 * @param resGid the gid of resource group requested.
	 * @return the list of resource ids.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	public List getResourceIdsOfResGroup(String resGid) throws OXWebDAVApiException {
		Document doc         = null; //for request and response
		List resStringList   = null; //for the resources of the group (as strings)
		List resGroupList    = null; // holds the resource groups
		List resElementList  = null; //for the xml-element of the resourcegroup
		Element e_multistatus, e_response, e_propstat, e_prop;
		Element e_resourcegroups, e_resourcegroup, e_resgroupid;
		Element e_resourcegroupmembers, e_res;
		
		resStringList = new Vector();
		
		try{
			doc = this.resourcegroupRequest(resGid);
			doc = this.doPropFind(doc);	
			
			e_multistatus = doc.getRootElement();
			e_response = e_multistatus.getChild("response", 
					Namespace.getNamespace("D", "DAV:"));
			e_propstat = e_response.getChild("propstat", 
					Namespace.getNamespace("D", "DAV:"));
			e_prop = e_propstat.getChild("prop", 
					Namespace.getNamespace("D", "DAV:"));
			e_resourcegroups = e_prop.getChild("resourcegroups",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			
			resGroupList = e_resourcegroups.getChildren();
			
			if(resGroupList == null)
				throw new OXWebDAVApiException("no resource groups in response");
			
			e_resourcegroup = null;
			for(int i=0; i<resGroupList.size(); i++){
				Element e_resourcegroup_tmp;
				
				e_resourcegroup_tmp = (Element)resGroupList.get(i);
				e_resgroupid = e_resourcegroup_tmp.getChild(
						"uid",Namespace.getNamespace(this.ox_ns_prefix, 
								this.ox_namespace));
				
				if(e_resgroupid.getValue().trim().equals(resGid)){
					/* found group */
					e_resourcegroup = e_resourcegroup_tmp;
					break;
				}
			}
			if(e_resourcegroup == null)
				throw new OXWebDAVApiException("no resource group with id " 
						+ resGid + " found" );
			
			e_resourcegroupmembers = e_resourcegroup.getChild(
					"resourcegroupmembers",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			//get all children (all groupmembers)
			resElementList = e_resourcegroupmembers.getChildren();
			
			//TODO perhaps check here the response file and edit this
			
			//add the content (string) to the return list
			if (resElementList != null){
				for(int i=0; i<resElementList.size(); i++){
					e_res = (Element)resElementList.get(i);
					resStringList.add(e_res.getValue());
				}
			}
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return resStringList;
	}
	
	
	
	
	/**
	 * Returns a list of all resource group ids.
	 * 
	 * @return the list of resource group ids.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	public List getResGroupIdList() throws OXWebDAVApiException{
		Document doc; // hold request and response
		Element e_multistatus, e_response, e_propstat, 
		e_prop, e_resourcegroups, e_resourcegroup, e_uid;
		List resGroupList;
		Vector resGroupIdList;
		
		resGroupIdList = new Vector();

		try{
			doc = this.allResourcegroupsRequest();
			doc = this.doPropFind(doc);	
			
			e_multistatus = doc.getRootElement();
			e_response = e_multistatus.getChild("response", Namespace.getNamespace("D", "DAV:"));
			e_propstat = e_response.getChild("propstat", Namespace.getNamespace("D", "DAV:"));
			e_prop = e_propstat.getChild("prop", Namespace.getNamespace("D", "DAV:"));
			e_resourcegroups = e_prop.getChild("resourcegroups",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			
			resGroupList = e_resourcegroups.getChildren();
			
			for(int i=0; i<resGroupList.size(); i++){
				e_resourcegroup = (Element)resGroupList.get(i);
				e_uid = e_resourcegroup.getChild("uid",
						Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
				resGroupIdList.add(new String(e_uid.getValue()));
			}
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return resGroupIdList;
	}
	
	/**
	 * Returns a list of all OXResource objects of resources in resource group
	 * with given gid.
	 * You should not pass a pattern as gid. If done so you'll get the
	 * list of OXResource objects of the resource group being the first search result.
	 * 
	 * @param gid the gid of resource group requested.
	 * @return the list of OXUser objects.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	public List getResourcesOfResGroup(String gid) throws OXWebDAVApiException{
		List resourceIdList; // of String
		List resourceList;     // of OXResource
		ListIterator iterator;
		
		if(gid == null) return null;
		
		try{
			resourceIdList = this.getResourceIdsOfResGroup(gid);
			if(resourceIdList == null) return null;
			
			resourceList = new Vector();
			
			iterator = resourceIdList.listIterator();
			
			while(iterator.hasNext()){
				String uid;
				OXResource resource;
				
				uid = (String)iterator.next();	
				resource = this.getOXResource(uid);
				resourceList.add(resource);
			}
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return resourceList; // note: may be empty
	}

	/**
	 * This function returns a list containing all resources as objects.
	 * 
	 * @return A list containing all resources.
	 * @throws OXWebDAVApiException
	 */
	public List getResIdList() throws OXWebDAVApiException{
		Document doc; // hold request and response
		Element e_multistatus, e_response, e_propstat, 
		e_prop, e_resources, e_resource, e_uid;
		List resList;
		Vector resIdList;
		
		resIdList = new Vector();

		try{
			doc = this.allResourcesRequest();
			doc = this.doPropFind(doc);	
			
			e_multistatus = doc.getRootElement();
			e_response = e_multistatus.getChild("response", 
					Namespace.getNamespace("D", "DAV:"));
			e_propstat = e_response.getChild("propstat", 
					Namespace.getNamespace("D", "DAV:"));
			e_prop = e_propstat.getChild("prop", 
					Namespace.getNamespace("D", "DAV:"));
			e_resources = e_prop.getChild("resources",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			
			resList = e_resources.getChildren();
			
			for(int i=0; i<resList.size(); i++){
				e_resource = (Element)resList.get(i);
				e_uid = e_resource.getChild("uid",
						Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
				resIdList.add(new String(e_uid.getValue()));
			}
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return resIdList;
	}
	
	/**
	 * Returns the xml request body to retrieve users with given search pattern
	 * by means of webdav propfind.
	 * 
	 * @param userUid the uid search pattern.
	 * @return the response body.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	private Document userRequest(String userUid) throws OXWebDAVApiException{
		Document doc;
		Element e_propfind, e_prop, e_user;
		
		try{
			doc = new Document(new Element("propfind", 
					Namespace.getNamespace("D", "DAV:")));
			e_propfind = doc.getRootElement();
			e_propfind.addNamespaceDeclaration(
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			
			e_prop = new Element("prop", 
					Namespace.getNamespace("D", "DAV:"));
			e_propfind.addContent(e_prop);
			
			e_user = new Element("user", 
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_user.addContent(userUid);
			e_prop.addContent(e_user);
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return doc;
	}
	
	/**
	 * Returns a list of all group ids.
	 * 
	 * @return the list of group ids.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	public List getGroupIdList() throws OXWebDAVApiException{
		Document doc; // hold request and response
		Element e_multistatus, e_response, e_propstat, 
		e_prop, e_groups, e_group, e_uid;
		List groupList;
		Vector groupIdList;
		
		groupIdList = new Vector();
		
		try{
			doc = this.allGroupsRequest();
			doc = this.doPropFind(doc);	
			
			e_multistatus = doc.getRootElement();
			e_response = e_multistatus.getChild("response", 
					Namespace.getNamespace("D", "DAV:"));
			e_propstat = e_response.getChild("propstat", 
					Namespace.getNamespace("D", "DAV:"));
			e_prop = e_propstat.getChild("prop", 
					Namespace.getNamespace("D", "DAV:"));
			e_groups = e_prop.getChild("groups",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			
			groupList = e_groups.getChildren();
			
			for(int i=0; i<groupList.size(); i++){
				e_group = (Element)groupList.get(i);
				e_uid = e_group.getChild("uid",
						Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
				groupIdList.add(new String(e_uid.getValue()));
			}
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return groupIdList;
	}
	
	/**
	 * Returns a list of all uids of group member of group with given gid.
	 * You should not pass a pattern as gid. If done so you'll get the
	 * list of uids of the group being the first search result.
	 * 
	 * @param gid the gid of group requested.
	 * @return the list of uids.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	public List getMemberIdsOfGroup(String gid) throws OXWebDAVApiException{
		Document doc; // hold request and response
		Element e_multistatus, e_response, e_propstat, 
		e_prop, e_groups, e_group, e_members, e_memberuid,
		e_users, e_user, e_forename, e_surename;
		List memberUidsList;
		List memberList; //uid
		
		try{
			
			doc = this.groupRequest(gid);
			doc = this.doPropFind(doc);
			
			//first get the members of the group
			e_multistatus = doc.getRootElement();
			e_response = e_multistatus.getChild("response", 
					Namespace.getNamespace("D", "DAV:"));
			e_propstat = e_response.getChild("propstat", 
					Namespace.getNamespace("D", "DAV:"));
			e_prop = e_propstat.getChild("prop", 
					Namespace.getNamespace("D", "DAV:"));
			e_groups = e_prop.getChild("groups",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_group = e_groups.getChild("group",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_members = e_group.getChild("members",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			
			memberUidsList = e_members.getChildren();
			memberList = new Vector();
			
			//now get the userdata for all uids
			for(int i=0; i<memberUidsList.size(); i++){
				e_memberuid = (Element)memberUidsList.get(i);
				memberList.add(e_memberuid.getValue());
			}
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return memberList;
	}
	
	/**
	 * Returns the OXResource object for the resource with given resource id.
	 * You must pass a pattern as uid.
	 * 
	 * @param rid the resource id.
	 * @return the OXResource object.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	public OXResource getOXResource(String rid) throws OXWebDAVApiException{
		Document doc;  // holds request and response
		Element e_multistatus, e_response, e_propstat, e_prop, e_memberuid;
		Element e_users, e_user, e_forename, e_surename, e_mail;
		
		try{
			return new OXResource(rid);
		}catch(Exception e){
			e.printStackTrace();
			throw new OXWebDAVApiException(
					"error constructing OXResource object for resource with id "
					+rid);
		}
	}
	
	/**
	 * Returns the OXuser object for the user with given uid.
	 * You should not pass a pattern as uid. If done so you'll get the
	 * object of the user being the first search result.
	 * 
	 * @param uid the uid.
	 * @return the OXUser object.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	public OXUser getOXUser(String uid) throws OXWebDAVApiException{
		Document doc;  // holds request and response
		Element e_multistatus, e_response, e_propstat, e_prop, e_memberuid;
		Element e_users, e_user, e_forename, e_surename, e_mail;
		
		try{
			doc = this.userRequest(uid);
			doc = this.doPropFind(doc);
			
			e_multistatus = doc.getRootElement();
			e_response = e_multistatus.getChild("response", 
					Namespace.getNamespace("D", "DAV:"));
			e_propstat = e_response.getChild("propstat", 
					Namespace.getNamespace("D", "DAV:"));
			e_prop = e_propstat.getChild("prop", 
					Namespace.getNamespace("D", "DAV:"));
			e_users = e_prop.getChild("users",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_user = e_users.getChild("user",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_forename = e_user.getChild("forename", 
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_surename = e_user.getChild("surename", 
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			e_mail= e_user.getChild("mail", 
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return new OXUser(uid, e_surename.getValue(), e_forename.getValue(), 
				e_mail.getValue());		
	}
	
	/**
	 * Returns a list of all OXUser objects of group members of group with 
	 * given gid. You should not pass a pattern as gid. If done so you'll get
	 * the list of OXUser objects of the group being the first search result.
	 * 
	 * @param gid the gid of group requested.
	 * @return the list of OXUser objects.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	public List getMembersOfGroup(String gid) throws OXWebDAVApiException{
		List memberIdList; // of String
		List userList;     // of OXUser
		ListIterator iterator;
		
		if(gid == null) return null;
		
		try{
			memberIdList = this.getMemberIdsOfGroup(gid);
			if(memberIdList == null) return null;
			
			userList = new Vector();
			
			iterator = memberIdList.listIterator();
			
			while(iterator.hasNext()){
				String uid;
				OXUser user;
				
				uid = (String)iterator.next();	
				user = this.getOXUser(uid);
				userList.add(user);
			}
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return userList; // note: may be empty
	}
	
	/**
	 * Get List of OXUsers with uid that fits search pattern.
	 * 
	 * @param pattern the search pattern.
	 * @return the list of OXUser objects.
	 * @throws OXWebDAVApiException if an error occurs during operation.
	 */
	public List getUsersByPattern(String pattern) throws OXWebDAVApiException{
		Document doc;
		Element e_multistatus, e_response, e_propstat, 
		e_prop, e_users, e_user, e_uid, e_forename, e_surename, e_mail;
		List userList;
		Vector memberList;
		
		try{
			doc = this.userRequest(pattern);
			doc = this.doPropFind(doc);
			
			memberList = new Vector();
			
			e_multistatus = doc.getRootElement();
			e_response = e_multistatus.getChild("response", 
					Namespace.getNamespace("D", "DAV:"));
			e_propstat = e_response.getChild("propstat", 
					Namespace.getNamespace("D", "DAV:"));
			e_prop = e_propstat.getChild("prop", 
					Namespace.getNamespace("D", "DAV:"));
			e_users = e_prop.getChild("users",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));			
			
			userList = e_users.getChildren();
			
			// @todo: assert != null
			
			for(int i=0; i<userList.size(); i++){
				e_user = (Element)userList.get(i);
				e_uid = e_user.getChild("uid",
						Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
				e_forename = e_user.getChild("forename", 
						Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
				e_surename = e_user.getChild("surename", 
						Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
				e_mail = e_user.getChild("mail", 
						Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
				memberList.add(
						new OXUser(e_uid.getValue(), e_surename.getValue(), 
								e_forename.getValue(), e_mail.getValue()));
			}
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return memberList;
	}

	/**
	 * This function returns a list containing all users as objects.
	 * 
	 * @return A list containing all user.
	 * @throws OXWebDAVApiException
	 */
	public List getUserIdList() throws OXWebDAVApiException{
		return this.getUsersByPattern("*");
	}
	
	/**
	 * This function gets the resources, found through the given pattern.
	 * 
	 * @param pattern the search pattern
	 * @return a list of strings representing the resources
	 * @throws OXWebDAVApiException
	 */
	public List getResourcesByPattern(String pattern) throws OXWebDAVApiException{
		Document doc; // hold request and response
		Element e_multistatus, e_response, e_propstat, 
		e_prop, e_resources, e_resource, e_uid;
		List resList;
		Vector resIdList;
		
		resIdList = new Vector();

		try{
			doc = this.resourceRequest(pattern);
			doc = this.doPropFind(doc);	
			
			e_multistatus = doc.getRootElement();
			e_response = e_multistatus.getChild("response", 
					Namespace.getNamespace("D", "DAV:"));
			e_propstat = e_response.getChild("propstat", 
					Namespace.getNamespace("D", "DAV:"));
			e_prop = e_propstat.getChild("prop", 
					Namespace.getNamespace("D", "DAV:"));
			e_resources = e_prop.getChild("resources",
					Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
			
			resList = e_resources.getChildren();
			
			for(int i=0; i<resList.size(); i++){
				e_resource = (Element)resList.get(i);
				e_uid = e_resource.getChild("uid",
						Namespace.getNamespace(this.ox_ns_prefix, this.ox_namespace));
				resIdList.add(new OXResource(e_uid.getValue()));
			}
		}catch(Exception exc){
			exc.printStackTrace();
			throw new OXWebDAVApiException(exc.getMessage(), exc);
		}
		
		return resIdList;
	}

	/**
	 * Enable debug output to stdout.
	 * 
	 * @param debug The debug to set.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Get status of last http request.
	 * 
	 * @return the lastStatus.
	 */
	public int getLastStatus() {
		return this.lastStatus;
	}
	
	/**
	 * 
	 */
	private class PropfindMethod extends EntityEnclosingMethod{
		
		/**
		 * 
		 *
		 */
		public PropfindMethod() {
			super();
		}
		
		/**
		 * 
		 * @param uri
		 */
		public PropfindMethod(String uri){
			super(uri);
		}
		
		/**
		 * 
		 */
		public String getName(){
			return "PROPFIND";
		}
	}

	/**
	 * Returns the server url.
	 * 
	 * @return the serverUrl.
	 */
	public String getServerUrl() {
		return ServerUrl;
	}
}
