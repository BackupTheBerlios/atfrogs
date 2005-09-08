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
 * Created on 30.05.2005
 */
package atfrogs.ox.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;
import atfrogs.util.FilterANSI;

/**
 * Contains methods to execute ox shell scripts. 
 *
 */
public class OXScriptApi {
	/**
	 * This function executes the script with its parameters given through 
	 * the passed string. The console output is returned, ANSI color codes
	 * are filtered out .
	 * 
	 * @param script the command to execute.
	 * @return the script's filtered output.
	 */
	private List runScript(String script) 
		throws IOException{
        String line;        //holds the current outputline
        List result;        //the hole output of the script
	    
        //add the scriptcall to the output
        result = new Vector();
        result.add(script.trim());
        
        //create a process for executing the script
        Process p = Runtime.getRuntime().exec(script);
	        
        //read the output of the script
        BufferedReader input = 
	    new BufferedReader(new FilterANSI(new InputStreamReader(p.getInputStream())));
        while ((line = input.readLine()) != null) {
        	//get the current line and add to the vector
        	result.add(line.trim());
          }
        input.close();
		
		return result;
	}
	
	/**
	 * This method executes the adduser script with the passed parameter.
	 * 
	 * @param username
	 * @param passwd
	 * @param name
	 * @param sname
	 * @param maildomain
	 * @param oxtimezone
	 * @param lang
	 * @param mailenabled
	 * @param inetmail
	 * @param oxappointmentdays
	 * @param writeglobaladdress
	 * @param shell
	 * @param oxtaskdays
	 * @param script
	 * @return the script output
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List addUser(String username, String passwd, String name, 
			     String sname, String maildomain, String oxtimezone,
			     String lang, String mailenabled, String inetmail,
			     String oxappointmentdays, String writeglobaladdress, 
			     String shell, String oxtaskdays,
			     String script) 
	    throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if ((script == null)){
			throw new OXScriptApiException("invalid script parameter!");
		}
		
		//build execute string
		scriptCall = script + 
				" --username=\"" + username + "\"" +
		        " --passwd=\"" + passwd + "\"" +
				" --name=\"" + name + "\"" +
				" --sname=\"" + sname + "\"" +
				" --maildomain=\"" + maildomain + "\"" +
				" --ox_timezone=\"" + oxtimezone + "\"" + 
				" --lang=\"" + lang + "\"" +
				" --mail_enabled=\"" + mailenabled + "\"" +
				" --inetmail=\"" + inetmail + "\"" +
				" --ox_appointment_days=\"" + oxappointmentdays + "\"" +
				" --write_global_address=\"" + writeglobaladdress + "\"" +
				" --shell=\"" + shell + "\"" + 
				" --ox_task_days=\"" + oxtaskdays + "\"";
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;
	}
	
	/**
	 * This method executes the deluser script with the passed parameter.
	 * 
	 * @param uid uid of user to add.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List delUser(String uid, String script) 
			throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if ((!testIdString(uid)) || (script == null)){
			throw new OXScriptApiException(
			    "invalid script parameter");
		}
		
		//build execute string
		scriptCall = script + " --username=" + uid;
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "Error executing the script!");
		}
		
		return result;
	}

	/**
	 * This method executes the resetuserpas script with the passed parameter.
	 * 
	 * @param uid uid of user to add.
	 * @param newpas the new password
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List resetUserPassword(String uid, String newpas, String script) 
			throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if ((!testIdString(uid)) || (newpas == null) || (script == null)){
			throw new OXScriptApiException(
			    "invalid script parameter!");
		}
		
		//build execute string
		scriptCall = script + " --user=" + uid + " --newpas=" + newpas;
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;
	}

	/**
	 * This method executes the useraddtogroup script with the passed parameter.
	 * 
	 * @param uidList a list of uids, separated through whitespaces.
	 * @param staff if set to true, a staff memeber will be replicated, else a student.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List replicate (String uidList, boolean staff, String script) 
			throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if (script == null){
			throw new OXScriptApiException(
			    "invalid script parameter!");
		}
		
		//build execute string
		scriptCall = script + ((staff==true)?" --staff":" --student") + " --uid " + uidList;
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;
	}
	
	/**
	 * This method executes the useraddtogroup script with the passed parameter.
	 * 
	 * @param uid uid of user to add.
	 * @param group gid of group to add user to.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List addUserToGroup (String uid, String group, String script) 
			throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if ((!testIdString(uid)) || (!testIdString(group)) 
		    || (script == null)){
			throw new OXScriptApiException(
			    "invalid script parameter!");
		}
		
		//build execute string
		scriptCall = script + " --user=" + uid + " --group=" + group;
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
			return result;
	}
	
	/**
	 * This method executes the deluserfromgroup script with the passed
	 * parameter.
	 * 
	 * @param uid uid of user to remove.
	 * @param group gid of group to remove user from.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List delUserFromGroup(String uid, String group, String script) 
				throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if ((!testIdString(uid)) || (!testIdString(group)) 
		    || (script == null)){
			throw new OXScriptApiException(
			    "invalid script parameter!");
		}
		
		//build execute string
		scriptCall = script + " --user=" + uid + " --group=" + group;
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;
	}
	
	/**
	 * This method executes the addgroup script with the passed parameter.
	 * 
	 * @param newGroup gid of new group.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List addGroup(String newGroup, String script) 
				throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if (!testIdString(newGroup) || (script == null)){
			throw new OXScriptApiException(
			    "invalid script parameter!");
		}
		
		//build execute string
		scriptCall = script + " --group=" + newGroup;
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;
	}

	/**
	 * This method executes the delgroup script with the passed parameter.
	 * 
	 * @param group uid of group to delete.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List delGroup(String group, String script) 
	    throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if (!testIdString(group) || (script == null)){
			throw new OXScriptApiException(
			    "Ungueltige Skript-Parameter!");
		}
		
		//build execute string
		scriptCall = script + " --groupname=" + group;
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException("error executing the script!");
		}
		
		return result;
	}

	/**
	 * This method executes the addresourcegroup script with the passed
	 * parameter.
	 * 
	 * @param group name of group to create.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List addResGroup(String group, String script) 
	    throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if (!testIdString(group) || (script == null)){
			throw new OXScriptApiException(
			    "invalid script parameter!");
		}
		
		//build execute string
		scriptCall = script + " --name=" + group + " --av=TRUE";
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;
	}

	/**
	 * This method executes the addresourcegroup script with the passed
	 * parameter.
	 * 
	 * @param group name of group to create.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List delResGroup(String group, String script) 
	    throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if (!testIdString(group) || (script == null)){
			throw new OXScriptApiException(
			    "invalid script parameter!");
		}
		
		//build execute string
		scriptCall = script + " --name=" + group;

		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;		
	}

	/**
	 * This method executes the addresource script with the passed parameter.
	 * 
	 * @param res name of resource to create.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List addRes(String res, String script) throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if (!testIdString(res) || (script == null)){
			throw new OXScriptApiException(
			    "invalid script parameter");
		}
		
		//build execute string
		scriptCall = script + " --name=" + res + " --av=TRUE";
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
		        throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;
	}
	
	/**
	 * This method executes the delresource script with the passed parameter.
	 * 
	 * @param res name of resource to delete.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List delRes(String res, String script) 
	    throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if (!testIdString(res) || (script == null)){
			throw new OXScriptApiException(
			    "invalid script parameter!");
		}
		
		//build execute string
		scriptCall = script + " --name=" + res;
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;
	}
	
	/**
	 * This method executes the addresourcetogroup script with the passed
	 * parameter.
	 * 
	 * @param res the resource to add.
	 * @param resGroup the resourcegroup to add the resource to
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List addResToResGroup (String res, String resGroup, String script) 
			throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if ((!testIdString(res)) || (!testIdString(resGroup)) 
		    || (script == null)){
			throw new OXScriptApiException("Unguetige Skript-Parameter!");
		}
		
		//build execute string
		scriptCall = script + " --res=" + res + " --group=" + resGroup;
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;
	}
	
	/**
	 * This method executes the delresourcefromresourcegroup script with the
	 * passed parameter.
	 * 
	 * @param res the res to remove.
	 * @param resGroup group to remove the resource from.
	 * @param script the script to execute.
	 * @return the script's filtered output.
	 * @throws OXScriptApiException if the executen of the script fails
	 */
	public List delResFromResGroup(String res, String resGroup, String script) 
				throws OXScriptApiException{
		String scriptCall;  //execute string
		String lastLine;    //last line of script output
		List result;        //script result
		
		//test parameters
		if ((!testIdString(res)) || (!testIdString(resGroup)) 
		    || (script == null)){
			throw new OXScriptApiException(
			    "invalid script parameter!");
		}
		
		//build execute string
		scriptCall = script + " --resname=" + res + " --resgroup=" + resGroup;
		
		//run script
		try {
			result = runScript(scriptCall);
		} catch (IOException e){
			//return the exception string
			throw new OXScriptApiException(
			    "error executing the script!");
		}
		
		return result;
	}
	
	/**
	 * In order to test the gid/uid parameter for the scripts to avoid 
	 * invalid inputs.
	 * 
	 * @param id the group or user id.
	 * @return true iff the string is valid.
	 */
	private static boolean testIdString(String id){
		
		if ((id == null) || (id.equals("")))
			return false;

		/*the string should only contain the following character: 
		 * A-Z, a-z, 0-9 */
		for(int i = 0; i < id.length(); i++){
			char tmpChar = id.charAt(i);
			if ((!Character.isUnicodeIdentifierPart(tmpChar)) 
			    && (tmpChar != '-') && (tmpChar != '.'))
				return false;
		}
		
		return true;
	}
}
