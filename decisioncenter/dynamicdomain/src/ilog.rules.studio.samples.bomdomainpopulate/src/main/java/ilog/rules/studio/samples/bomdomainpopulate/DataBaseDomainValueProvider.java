/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-W88 5655-Y31 5724-X98 5724-Y15 5655-V82 
* Copyright IBM Corp. 1987, 2025. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package ilog.rules.studio.samples.bomdomainpopulate;

//import ilog.rules.brl.syntaxtree.IlrSyntaxTree.Node;
import ilog.rules.bom.IlrClass;
import ilog.rules.shared.bom.IlrBOMDomainValueProvider;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;


/**
 *
 * Implementation of the IlrBOMDomainValueProvider class. Relies on a DataBaseDomainHelper
 * instance to retrieve the item name, verbalization and translation of the static references
 * defined in the Database.
 */
public class DataBaseDomainValueProvider implements IlrBOMDomainValueProvider {

	// temporary reference of the fully qualified name of the BOM is set in getValues
	private String className = null;
	
	/**
	 * @return A collection of String instances used to name the domain items
	 */
	public Collection<String> getValues(IlrClass clazz) {
		this.className = clazz.getFullyQualifiedName();
		DataBaseDomainHelper helper = DataBaseDomainHelper.getDataBaseDomainHelper(className);
		helper.readValuesFromDataBase();
		return helper.getItemNames();
	}
	
	/**
     * @param valueName the element
     * @param locale the locale
     * @return the way the element will appear in the proposed list of values
	 *         ie the verbalization for the static reference item valueName, in the given locale
	 */
	public String getDisplayText(String valueName, Locale locale) {
		DataBaseDomainHelper helper = DataBaseDomainHelper.getDataBaseDomainHelper(className);
		String verbalization = helper.getVerbalization(valueName);
		return verbalization;
	}
	
	/**
	 * @return null in this implementation (translation property is deprecated as of JRules 6.0)
	 */
	public String getTranslation(String valueName) {
		return null;
	}

	/**
	 * @return null since no specific properties is found in the databasedomain
	 */
	public Map<Object,Object> getProperties(String valueName) {
		return null;
	}
	/**
	 * @return null since no specific documentation is found in the databasedomain
	 */

	public String getDocumentation(String valueName, Locale locale) {
		return null;
	}

	public void dispose() {
		
	}
	
	/**
	 * @return the BOM2XOM Mapping for the given named item
	 */
	public String getBOM2XOMMapping(String valueName) {
		DataBaseDomainHelper helper = DataBaseDomainHelper.getDataBaseDomainHelper(className);
		String translation = helper.getTranslation(valueName);
		return translation;
	}

	//public void prepare(Node arg0) {
	//}
}
