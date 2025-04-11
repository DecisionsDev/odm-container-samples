/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-W88 5655-Y31 5724-X98 5724-Y15 5655-V82 
* Copyright IBM Corp. 1987, 2025. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package ilog.rules.studio.samples.bomdomainpopulate.stock;

import ilog.rules.brl.translation.codegen.IlrValueTranslator;
import ilog.rules.vocabulary.model.IlrConcept;
import ilog.rules.vocabulary.model.IlrVocabulary;

/**
 * The StockValueTranslator translates the String of the Text Editors
 * into another String, at the IRL Level. This new String should be compliant with
 * the actual type of the BOM attribute where the valueinfo has been set.
 */
public class StockValueTranslator implements IlrValueTranslator {

	
	private StockModel model;

	/**
	 * @param model
	 */
	public StockValueTranslator(StockModel model) {
		
		this.model = model;
	}

	/**
	 * @param value the String
	 * @return the IRL String representation of the value
	 */
	public String translateValue(String value, IlrConcept concept, IlrVocabulary vocabulary) {
		if (!model.isInitialized())
			return null;
		Stock stock = model.getStock(value);
		if (stock == null)
			return null;
		else
			return "\"" + stock.getId() + "\"";
	}

	/**
	 *  @return whether wrapping the value is allowed or not. Default is false
	 */
	public boolean allowValueWrapping() {
		return false;
	}

}
