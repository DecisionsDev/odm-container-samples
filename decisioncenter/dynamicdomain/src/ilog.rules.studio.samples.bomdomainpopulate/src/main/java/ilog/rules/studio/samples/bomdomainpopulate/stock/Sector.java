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

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a stock sector. A sector is composed of a name and of a list of stocks
 * The StockModel creates and updates sectors according to the database contents
 */
public class Sector {
	private String name;
	private List<Stock> stocks = new ArrayList<Stock>();
	
	/**
	 * Creates a new Sector with the given name
	 * @param name The name of the sector
	 */
	public Sector(String name) {
		this.name = name;
	}
	
	/**
	 * Add a stock to this sector
	 * @param s
	 */
	public void addStock(Stock s) {
		stocks.add(s);
	}
	/**
	 * 
	 * @return An arrays of stock objects for this sector
	 */
	public Stock[] getStocks() {
		return stocks.toArray(new Stock[stocks.size()]);
	}
	
	/**
	 * 
	 * @return The name of this sector
	 */
	public String getName() {
		return name;
	}
}
