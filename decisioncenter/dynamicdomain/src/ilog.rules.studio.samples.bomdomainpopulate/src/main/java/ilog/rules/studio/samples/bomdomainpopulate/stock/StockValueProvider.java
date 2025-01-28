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

import java.util.Locale;

import ilog.rules.brl.syntaxtree.IlrSyntaxTree.Node;
import ilog.rules.brl.value.info.IlrValueProvider;

/**
 * StockValueProvider. Mostly delegates its implementation to the StockModel
 * accessors
 */
public class StockValueProvider implements IlrValueProvider {

	private StockModel model;

	/**
	 * Constructor 
	 * @param model the model for this provider
	 * 
	 */
	public StockValueProvider(StockModel model) {
		super();
		this.model = model;
	}

	/* 
	 * This function is called before any other function of the class. It is
	 * the perfect place for the model update. Note that this update operation
	 * should not be time consuming, or it would break the completion reactivity.
	 */
	public void prepare(Node node) {
		//reset the model...
		model.update();
	}

	/**
	 * @return the sectors of the stock model. This represents the root entries of the
	 * tree completor (activated with CTRL+Shift+Space in the text editor)
	 */
	public Object[] getValues() {
		return model.getSectors();
	}

	/* 
	 * @param parent A Sector instance, in the sample context
	 * @return the stocks for the given sector. 
	 */
	public Object[] getChildren(Object parent) {
		if (parent instanceof Sector) {
			return ((Sector) parent).getStocks();
		} else
			return null;
	}

	/* 
	 * @param element only Stock instances have a parent Sector
	 * @return the Sector for the given stock
	 */
	public Object getParent(Object element) {
		if (element instanceof Stock)
			return ((Stock)element).getSector();
		else
			return null;
	}

	/* 
	 * @param element can be eiter a Stock or a Sector instance
	 * @return true if the passed object is a Sector instance
	 */
	public boolean hasChildren(Object element) {
		return (element instanceof Sector);
	}

	/* 
	 * This function returns the string displayes in the completion popup menu
	 * for the given element, in the specified locale. In the sample context,
	 * the name of the Sector or the Stock instance is returned.
	 */
	public String getDisplayText(Object element, Locale locale) {
		if (element instanceof Sector)
			return ((Sector)element).getName();
		else if (element instanceof Stock)
			return ((Stock)element).getName() ;
		else
			return null;
	}

	/* 
	 * This function returns the actual text that will be inserted in the Text body.
	 * It should typically be a business-friendly String. In the sample context,
	 * the quoted name of the Stock is returned, whatever the locale.
	 */
	public String getText(Object element, Locale locale) {
		if (element instanceof Sector) {
			// sectors are not translatable
			return null;
		}
		else if (element instanceof Stock)
			return "\"" + ((Stock)element).getName() + "\"" ;
		else
			return null;
	}

	/* 
	 * This implementation does not need a dispose action
	 */
	public void dispose() {
	}

	public boolean isExclusive() {
	  return false;
	}

}
