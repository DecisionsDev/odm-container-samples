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

import ilog.rules.teamserver.web.components.intelliruleeditor.IlrClientScriptFileValueEditorProvider;

/**
 *  The value editor class defines where to find the java script file to build the editor.
 */

public class OfferValueEditorProvider implements IlrClientScriptFileValueEditorProvider {

	public String getClientScriptModulePath() {
		return "/js/custom/valueeditors";
	}

	public String getClientScriptModuleName() {
		return "custom.valueeditors";
	}

	public String getClientScriptClassName() {
		return "custom.valueeditors.OfferValueEditor";
	}
}
