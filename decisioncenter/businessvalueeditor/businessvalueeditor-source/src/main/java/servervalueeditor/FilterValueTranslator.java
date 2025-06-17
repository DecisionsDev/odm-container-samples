/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5724-Y00 5724-Y17 5655-V84
* Copyright IBM Corp. 2009, 2025. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package servervalueeditor;

import ilog.rules.brl.translation.codegen.IlrValueTranslator;
import ilog.rules.vocabulary.model.IlrConcept;
import ilog.rules.vocabulary.model.IlrVocabulary;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class FilterValueTranslator implements IlrValueTranslator {
	private static Logger log = Logger.getLogger(FilterValueTranslator.class.getName());

    /**
     *
     */
    public FilterValueTranslator() {
        super();
	log.log(Level.INFO, "INIT FilterValue Translator");
    }

    /*
     * @see ilog.rules.brl.translation.codegen.IlrValueTranslator#translateValue(java.lang.String, ilog.rules.vocabulary.model.IlrConcept, ilog.rules.vocabulary.model.IlrVocabulary)
     */
    public String translateValue(String value, IlrConcept concept,
            IlrVocabulary vocabulary) {
	log.log(Level.INFO, "translateValue >>" + value + " << end");
        return "sample.PromotionalOffer.getOffer(\""+value+"\")";
    }

    public boolean allowValueWrapping() {
        return false;
    }

}


