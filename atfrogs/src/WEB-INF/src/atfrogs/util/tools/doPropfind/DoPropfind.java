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
 * Created on 26.06.2005
 *
 */
package atfrogs.util.tools.doPropfind;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;

import atfrogs.ox.api.OXWebDAVApi;
import atfrogs.ox.api.OXWebDAVApiException;

/**
 * This class provides a command line tool to send a request to ox server by
 * means of the WebDAV API.
 * The program expects the path the xml file holding the request and the path
 * to the file where the response shall be written to as parameters.
 */
public class DoPropfind {
	
	/**
	 * To avoid instantiation
	 */
	private DoPropfind() {
		super();
	}
	
	public static void main(String[] args) {
		InputStream inputStream   = null; //for reading a file
		OutputStream outputStream = null; //for writing a file
		
		SAXBuilder saxBuilder = null; 
		XMLOutputter xmlOutputter  = null; //jdom xml outputter
		
		Document request, response;
		
		OXWebDAVApi webdavApi;
		
		String inputFile          = null; //input file name
		String outputFile         = null; //output file name
		
		try{		
			//test parameter
			if(args == null || args.length != 2) {
				printUsage();
				System.exit(1);
			}
			
			//get the filenames and create streams
			inputFile  = args[0];
			outputFile = args[1];
			
			System.out.print("parsing input file " + inputFile + "...");
			
			inputStream  = new FileInputStream(inputFile);
			saxBuilder = new SAXBuilder();//true);
			request = saxBuilder.build(inputStream);
			inputStream.close();
			
			System.out.println(" done");
			
			outputStream = new FileOutputStream(new File(outputFile));
			
			webdavApi = new OXWebDAVApi(
					"http:// /servlet/webdav.groupuser", 
					"user", 
					"pass");
			System.out.print("sending propfind request to "+webdavApi.getServerUrl()+" ...");
			response = webdavApi.doPropFind(request);
			System.out.println(" done (http status " + webdavApi.getLastStatus() + ")");
			
			System.out.print("writing output file " + outputFile + "...");
			xmlOutputter = new org.jdom.output.XMLOutputter();
			xmlOutputter.output(response, outputStream);
			outputStream.close();
			System.out.println(" done");
			/*} catch (DoPropfindException e){
			 System.out.println(e.getMessage());
			 System.exit(1);*/
		}catch (OXWebDAVApiException e){
			System.out.println(e.getMessage());
			System.exit(1);
		}catch (IOException e){
			System.out.println(e.getMessage());
			System.exit(1);
		}catch (JDOMException e){
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (Exception e){
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Prints the usage of the tool to console.
	 */
	private static void printUsage(){
		System.out.println("Usage of this tool:");
		System.out.println("<toolname> xml-input-file xml-output-file");
	}
}

/**
 * 
 */
class DoPropfindException extends Exception{
	/**
	 * 
	 */
	public DoPropfindException() {
		super();
	}
	
	/**
	 * @param message the message.
	 */
	public DoPropfindException(String message) {
		super(message);
	}
	
	/**
	 * @param cause the cause.
	 */
	public DoPropfindException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @param message the message.
	 * @param cause the cause.
	 */
	public DoPropfindException(String message, Throwable cause) {
		super(message, cause);
	}
	
}