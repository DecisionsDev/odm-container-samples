/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5655-Y31 5724-X98 5724-Y15 5655-V82 
* Copyright IBM Corp. 1987, 2022. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package trade;

/**
 */
public class Order {

	private String id;
	private String stock;
	private int amount;
	private String currency;
	private String action;
	
	public Order() {
	}

	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}
	
	/**
	 * @return Returns the currency.
	 */
	public String getCurrency() {
		return currency;
	}
	
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return Returns the stock.
	 */
	public String getStock() {
		return stock;
	}

	public Order(String id, String stock, int amount, String action, String currency) {
		super();
		this.id = id;
		this.action = action;
		this.stock = stock;
		this.amount = amount;
		this.currency = currency;
	}
	/**
	 * @return Returns the amount.
	 */
	public int getAmount() {
		return amount;
	}
}
