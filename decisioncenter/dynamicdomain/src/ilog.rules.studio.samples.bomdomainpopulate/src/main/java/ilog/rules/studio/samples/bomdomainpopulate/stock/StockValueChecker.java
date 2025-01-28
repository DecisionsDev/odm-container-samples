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

import ilog.rules.brl.syntaxtree.IlrSyntaxTree.Node;
import ilog.rules.brl.value.info.IlrValueChecker;
import ilog.rules.brl.value.info.IlrValueError;

/**
 * this class checks that the value used against the stock id is valid.
 * It uses the StockModel and is able to correctly answer only if the model
 * is correctly initialized.
 */
public class StockValueChecker implements IlrValueChecker {

	private StockModel model;
	private long lastUpdateTime = System.currentTimeMillis();
	private static final long UPDATE_TIME = 1000;

	/**
	 * @param model
	 */
	public StockValueChecker(StockModel model) {
		this.model = model;
	}

	/* 
	 * @return true if the given value is valid, false otherwise
	 */
	public boolean check(Object value, Node node, IlrValueError valueError) {
		long currentTime = System.currentTimeMillis();
		if (!model.isInitialized() || ((currentTime - lastUpdateTime) > UPDATE_TIME)) {
			model.update();
			lastUpdateTime = currentTime;
		}
		return model.containsStock(value);
	}

}
