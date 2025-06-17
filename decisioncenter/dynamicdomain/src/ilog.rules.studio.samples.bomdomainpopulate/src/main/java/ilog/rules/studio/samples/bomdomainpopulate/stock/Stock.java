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

/**
 * A Representation of a stock. A stock is composed of an id, a name, and is attached
 * to a sector. In this sample, Stock objects are created when synchronizing with
 * the DataBase. It is the job of the StockModel class.
 */
public class Stock {
	private String id;
	private String name;
	private Sector sector;
	
	/**
	 * Creates a new Stock instance
	 * @param id 
	 * @param name
	 * @param sector
	 */
	public Stock(String id, String name, Sector sector) {
		this.id = id;
		this.name = name;
		this.sector = sector;
	}
	
	/**
	 * 
	 * @return the Id of this Stock
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * @return the Name of this Stock
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return the Sector of this Stock
	 */
	public Sector getSector() {
		return sector;
	}
}
