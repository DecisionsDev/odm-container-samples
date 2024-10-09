/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-W88 5655-Y31 5724-X98 5724-Y15 5655-V82 
* Copyright IBM Corp. 1987, 2022. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package ilog.rules.studio.samples.bomdomainpopulate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/*
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.rules.decisioncenter.web.core.services.WebSessionService;

import ilog.rules.teamserver.ejb.service.dao.IlrElementDAO;
import ilog.rules.teamserver.model.IlrSessionEx;
*/

/**
 * Class dedicated to DB Connection. Properties for the connection are expected 
 * to be found in ./data/database.properties, but could probably be declared
 * through an extension point
 * 
 **/
public class DataBaseConnector {

	
	// database connection parameters are stored in a plugin file
	private static final String DATABASE_PARAMETERS = "/data/database.properties";
	// store DB connection values
	private static String driver;
	private static String database;
	private static String user;
	private static String password;
	
	// a connection is temporarily established to retrieve data (see openConnection and closeConnection
	private static Connection connection;
/*
    @Autowired
    private WebSessionService sessionService;
*/
	static {
		initDbParameters();
	}
	
	/**
	 * retrieves and stored database connection parameters located in the property file 
	 */
	private static void initDbParameters () {
		
		InputStream input = null;
		Properties properties = new Properties();
	    try {
	    	input = DataBaseConnector.class.getResourceAsStream(DATABASE_PARAMETERS);
	    	properties.load(input);
			driver = properties.getProperty("database.driver");			
			database = properties.getProperty("database.url");
			user = properties.getProperty("database.user");
			password = properties.getProperty("database.password");			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}


	/**
	 * Open database connection
	 */
	public static Connection openConnection() {
		try {
			Class<?> clazz = Class.forName(driver);
			connection = DriverManager.getConnection(database, user, password);
			return connection;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("A SQL Exception has occured: please check that the database is available");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Close the DB connection
	 *
	 */
	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
