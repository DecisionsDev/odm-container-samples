/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-W88 5655-Y31 5724-X98 5724-Y15 5655-V82 
* Copyright IBM Corp. 1987, 2022. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package ilog.rules.studio.samples.bomdomainpopulate.stock;

import ilog.rules.brl.translation.codegen.IlrValueTranslator;
import ilog.rules.brl.value.info.IlrValueChecker;
import ilog.rules.brl.value.info.IlrValueInfo;
import ilog.rules.brl.value.info.IlrValueProvider;

import java.util.Locale;

/**
 * Main entry point for the valueinfo concepts: value provider, value checker,
 * value translator:
 *  - value provider is responsible for filling the completion popup menu (CTRL+Space or CTRL+Shift+Space)
 *  - value checker checks that the current value exists among the stocks listed in the model
 *  - value translator translates the stock name into its id String 
 */
public class StockValueInfo implements IlrValueInfo {

	private StockValueProvider valueprovider;
	private StockValueChecker valuechecker;
	private StockValueTranslator valuetranslator;
	
	// the stock model is created with the valueinfo 
	private StockModel model = new StockModel();
	
	/**
	 * Default Constructor
	 */
	public StockValueInfo() {
		super();
	}
	
	/**
	 * Whatever the locale, the display name should be "stock"
	 */
	public String getDisplayName(Locale locale) {
		return "Stock";
	}

	/**
	 * return the key for a string descriptor
	 */
	public String getValueDescriptor() {
	    return "ilog.rules.brl.value.descriptor.IlrStringValueDescriptor";
	}
	
	/**
	 * no specific value editor, return null
	 */
	public String getValueEditor() {
		return null;
	}
	/**
	 * @return a StockValueProvider instance
	 */
	public IlrValueProvider getValueProvider() {
		if (valueprovider == null)
			this.valueprovider = new StockValueProvider(model);
		return valueprovider;
	}
	/**
	 * @return a StockValueChecker instance
	 */
	public IlrValueChecker getValueChecker() {
		if (valuechecker == null)
			this.valuechecker = new StockValueChecker(model);
		return valuechecker;
	}

	/**
	 * @return a StockValueTranslator instance
	 */
	public IlrValueTranslator getValueTranslator(String target) {
		if (valuetranslator == null)
			this.valuetranslator = new StockValueTranslator(model);
		return valuetranslator;
	}

}
