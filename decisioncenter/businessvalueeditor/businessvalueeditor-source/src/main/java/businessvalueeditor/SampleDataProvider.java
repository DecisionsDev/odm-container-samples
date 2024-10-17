/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5724-Y00 5724-Y17 5655-V84
* Copyright IBM Corp. 2009, 2024. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package businessvalueeditor;


/**
 * Data provider class for the sample: defines the data to be shown and their description
 */
public class SampleDataProvider  {

    final static String[][] dataInfos = new String[][] {
            {"AADCVFD", "Free access to the park"},
            {"ADFGFVB", "Fidelity points"},
            {"BAGFDFF", "Free transportation miles"},
            {"BFGHFDF", "Special rebate for customer with a referral"},
            {"DFARSDG", "Small gift for customer with NO referral"},
            {"DFTYGFD", "Free Offer from California office"},
            {"GDFDSFB", "Free Offer from Texas office"},
            {"GNFDFSS", "Free Offer from Nevada office"},
            {"VDFDSFF", "Huge rebate for customer with a referral"},
            {"XSDFFSG", "Invitation for annual special event"},
            {"ZEREZRE", "Tickets for the opera"}
    };

    public int getCount() {
	return dataInfos.length;
    }
    public String getValue(int i) {
	if (i >=0 && i < getCount())
	    return dataInfos[i][0];
	return "";
    }
    public String getDescription(int i) {
	if (i >=0 && i < getCount())
	    return dataInfos[i][1];
	return "";
    }
}
