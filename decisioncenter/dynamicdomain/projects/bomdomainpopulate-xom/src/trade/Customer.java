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
public class Customer {
	
	public Customer(String currency, String name) {
		super();
		this.currency = currency;
		this.name = name;
	}

	public Customer() {
	}	
		
	private String currency;
	private String name;
	
	
	/**
	 * @return Returns the currency.
	 */
	public String getCurrency() {
		return currency;
	}
	
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
}
