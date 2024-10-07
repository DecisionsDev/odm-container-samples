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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import ilog.rules.studio.samples.bomdomainpopulate.DataBaseConnector;

/**
 * Represents a model of sectors and sotcks. The model is updated and synchronized 
 * with the DB contents. The model is used a a support for the StockValueInfo, StockValueChecker
 * and StockValueProvider classes.
 *
 */
public class StockModel {
	// initialize attribute is set to false in case of DataBase connection issues
	private boolean initialized = false;
	
	// database column names where the stock information is located are hardcoded here.
	private static final String SECTOR_COLUMN = "NAME";
	private static final String STOCKID_COLUMN = "STOCK_ID";
	private static final String STOCKNAME_COLUMN = "STOCK_NAME";
	
	// Use hashmaps to store model information
	private HashMap<String, Sector> sectors = new HashMap<String, Sector>();
	private HashMap<Object, Stock> allstocks = new HashMap<Object, Stock>();
	
	/**
	 * Updates the current model. Retrieves information from the DataBase and
	 * creates corresponding Stocks/Sectors instances. 
	 *
	 */
	public void update() {
		initialized = false;
		sectors.clear();
		allstocks.clear();
		Connection connection = DataBaseConnector.openConnection();
		if (connection != null) {
			try {
				// get the hardcoded query
				String query = getRequest();
				Statement statement = connection.createStatement();
				ResultSet set = statement.executeQuery(query);
				// explore the result set
				while (set.next()) {
					String sectorName = set.getString(SECTOR_COLUMN);
					String stockid = set.getString(STOCKID_COLUMN);
					String stockName = set.getString(STOCKNAME_COLUMN);
					// add sector and stock thanks to these information
					addSectorAndStock(sectorName, stockid, stockName);
					
				}
				// model has been correctly initialized
				initialized = true;
			} catch (SQLException e) {
				System.out.println("An SQL Exception has occured: please check that the DataBase is available");				//reset initialized stage
				initialized = false;
			}
			DataBaseConnector.closeConnection();
		}
		else
			initialized = false;
	}

	/**
	 * Adds the given sector and stock to the model
	 * @param sectorName
	 * @param stockId
	 * @param stockName
	 */
	private void addSectorAndStock(String sectorName, String stockId, String stockName) {
		Sector newSector = getOrCreateSector(sectorName);
		Stock stock = new Stock(stockId, stockName, newSector);
		newSector.addStock(stock);
		allstocks.put(stockName, stock);
		
	}
	
	/**
	 * Gets or creates the Sector with the specified name
	 * @param name
	 * @return 
	 */
	private Sector getOrCreateSector(String name) {
		Sector obj = sectors.get(name);
		if (obj == null) {
			obj = new Sector(name);
			sectors.put(name,obj);
		}
		return obj; 
	}
	
	/**
	 * @return The hardcoded request used in this sample
	 */
	private String getRequest() {
		String req = "SELECT * FROM BOMDOMAINSAMPLE.SECTOR, BOMDOMAINSAMPLE.STOCK WHERE BOMDOMAINSAMPLE.SECTOR.ID=BOMDOMAINSAMPLE.STOCK.SECTOR_ID";
		return req;
	}
	/**
	 * 
	 * @return the sectors of the stock model
	 */
	public Sector[] getSectors() {
		return sectors.values().toArray(new Sector[sectors.size()]);
	}
	
	/**
	 * 
	 * @return all the stocks inserted in the model
	 */
	public Stock[] getAllStocks() {
		return allstocks.values().toArray(new Stock[allstocks.size()]);
	}

	/**
	 * @param value
	 * @return true if the stock id is contained in the model
	 */
	public boolean containsStock(Object value) {
		return allstocks.containsKey(value);
	}

	/**
	 * Used in @StockValue
	 * @param value
	 * @return
	 */
	public Stock getStock(String name) {
		Object o = allstocks.get(name);
		return (Stock) o;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
}
