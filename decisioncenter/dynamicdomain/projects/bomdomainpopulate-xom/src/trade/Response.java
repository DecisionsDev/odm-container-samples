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


public class Response {
	private String status;
	private String message;
	
	
	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toString() {
		return "status: " + this.status +", message: " + this.message;
	}
}
