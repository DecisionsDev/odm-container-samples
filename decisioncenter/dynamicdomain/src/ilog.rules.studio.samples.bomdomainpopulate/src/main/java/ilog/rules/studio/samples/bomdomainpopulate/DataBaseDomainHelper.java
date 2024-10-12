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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * This helper class combines connectivity properties for a database
 * along with specific settings for BOM dynamic domains.
 * A static function provides a helper for a dedicated fully qualified class, which is
 * is responsible for providing the db values.
 */
public class DataBaseDomainHelper {
	
	private static final String SCHEMA_PARAMETERS = "/data/mapping.properties";	
	// various suffixes that are used in the schema mapping
	private static final String TABLE_SUFFIX = ".table";
	private static final String ITEMNAME_SUFFIX= ".itemname";
	private static final String ITEMVERBALISATION_SUFFIX = ".itemverbalization";
	private static final String ITEMTRANSLATION_SUFFIX = ".itemtranslation";
	
	// Hashmap storing database configs for a specific class
	// key = fully qualified name of the domain class
	private static HashMap<String, Object> classdomains = new HashMap<String, Object>();
	// properties of the schema mapping of each domain class. 
	private static Properties domainTableProperties = null;
	
	
	// static initialisation is done once: compute db settings and schema mappings
	static {
		initColumnMappings();
	}

	/**
	 *  Representation of a static reference: name, verbalization and translation (b2x)
	 *  Instances of this class are stored in each databasedomain helper
	 */
	public static class Item {
		private String name;
		private String verbalization;
		private String code;
		
		public Item(String name, String verbalization, String code) {
			this.name = name;
			this.verbalization = verbalization;
			this.code = code;
		}
		
		public String getName() {
			return name;
		}
		
		public String getVerbalization() {
			// default text is same as name
			if (verbalization == null) return name;
			return verbalization;
		}
		
		public String getTranslation() {
			// default translation is item between double quotes
			if (code == null) return "\"" + name + "\"";
			return code;
		}
		
	}	
	
	// an instance of helper is attached to each domain class
	private String className;
	// store the domain items in a List
	private List<Item> domainItems = new ArrayList<Item>();


	
	/**
	 * private Constructor: use the factory to get such an instance
	 * @param fullyQualifiedName the name of the class
	 */
	private DataBaseDomainHelper(String fullyQualifiedName){
		this.className = fullyQualifiedName;
	}
	
	
	/**
	 * read the database values for the class name given attribute
	 *
	 */
	public void readValuesFromDataBase() {
		// first open global DB connection
		Connection connection = DataBaseConnector.openConnection();
		if (connection != null) {
			try {
				// retrieve table name for this particular class
				String tableName = DataBaseDomainHelper.domainTableProperties.getProperty(className + TABLE_SUFFIX);
				// default query is very simple: adjust it here if necessary
				String query = "SELECT * FROM " + tableName;
				Statement statement = connection.createStatement();
				ResultSet set = statement.executeQuery(query);
				// retrieve column names of interest for the domains: name , verbalization and translation (b2x) columns
				String nameColumn = DataBaseDomainHelper.domainTableProperties.getProperty(className + ITEMNAME_SUFFIX);
				String verbalizationColumn = DataBaseDomainHelper.domainTableProperties.getProperty(className + ITEMVERBALISATION_SUFFIX);
				String translationColumn = DataBaseDomainHelper.domainTableProperties.getProperty(className + ITEMTRANSLATION_SUFFIX);
				// clear the previous values now that the query has been correctly executed.
				domainItems.clear();
				while (set.next()) {
					String name = set.getString(nameColumn);
					String verbalization = set.getString(verbalizationColumn);
					String translation = set.getString(translationColumn);
					domainItems.add(new Item(name,verbalization,translation));
				}
			} catch (SQLException e) {
				BomdomainpopulatePlugin.log(e);
				e.printStackTrace();
			}
		}		
		// finally close global connection
		DataBaseConnector.closeConnection();
	}

	/**
	 * @return the list of names for the items of the current helper
	 */
	public Collection<String> getItemNames() {
		ArrayList<String> names = new ArrayList<String>();
		Iterator<Item> it = domainItems.iterator();
		while (it.hasNext()) {
			Item element = it.next();
			names.add(element.getName());
		}
		return names;
	}
	
	/**
	 * Gets the verbalization for a given domain item name
	 * @param valueName the name of the static ref attribute
	 * @return the verbalization string
	 */
	public String getVerbalization(String valueName) {
		Iterator<Item> it = domainItems.iterator();
		while (it.hasNext()) {
			Item element = it.next();
			if (valueName.equalsIgnoreCase(element.getName()))
				return element.getVerbalization();
		}
		// should never happen
		return null;
	}

	
	/**
	 * @param valueName
	 * @return
	 */
	public String getTranslation(String valueName) {
		Iterator<Item> it = domainItems.iterator();
		while (it.hasNext()) {
			Item element = it.next();
			if (valueName.equalsIgnoreCase(element.getName()))
				return element.getTranslation();
		}
		// should never happen
		return null;
	}	
	
	/**
	 * initialize the DB column mapping for each domain class through the property file:
	 * ./data/mapping.properties
	 */
	private static void initColumnMappings() {
		InputStream input = null;
		domainTableProperties = new Properties();
	    try {
	    	input = DataBaseDomainHelper.class.getResourceAsStream(SCHEMA_PARAMETERS);
	    	domainTableProperties.load(input);
		} catch (FileNotFoundException e) {
			BomdomainpopulatePlugin.log(e);
			e.printStackTrace();
		} catch (IOException e) {
			BomdomainpopulatePlugin.log(e);
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
	 * Main entry point for the DataBaseDomainHelper class.
	 * Looks for an existing databasedomain helper of a given class, and creates it
	 * if it does not exist
	 * @param className the Fully qualified name of the static reference domain
	 * @return a helper class dedicated to the domain in argument
	 */
	public static DataBaseDomainHelper getDataBaseDomainHelper(String className){
		Object helper = classdomains.get(className);
		if (helper == null) {
			helper = new DataBaseDomainHelper(className);
			classdomains.put(className, helper);
		}
		return (DataBaseDomainHelper) helper;
	}
}
