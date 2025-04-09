/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5724-Y00 5724-Y17 5655-V84
* Copyright IBM Corp. 2009, 2025. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package businessvalueeditor;

import ilog.rules.brl.brldf.IlrBRLDefinition;
import ilog.rules.brl.value.descriptor.IlrValueDescriptor;
import ilog.rules.brl.value.descriptor.IlrValueDescriptorFactory;
import ilog.rules.vocabulary.model.IlrConcept;
import ilog.rules.vocabulary.model.IlrVocabulary;
//ODM 9.5 onwards
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* ODM 9.0 and earlier
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
*/

import java.io.IOException;
import java.util.Locale;

import com.ibm.bdsl.web.editor.IntelliTextEditorEnvironment;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class SampleValueEditorServlet extends HttpServlet {
    /**
     * Servlet called to build the data shown in the editor.
     */
	private static final long serialVersionUID = 1L;
    private SampleDataProvider dataProvider = null;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String textResponse = "";
	if (request.getParameter("offer") != null) {
	    textResponse = processOffer(request);
	} 
	writeJSONResponse(textResponse, request, response, false);
    }

    private void writeJSONResponse(String jsonResponse, HttpServletRequest request,
				   HttpServletResponse response, boolean useCompressionWhenAvailable) throws IOException {
	if (jsonResponse != null && !"".equals(jsonResponse)) {
	    response.setContentType("application/json");
	    response.setLocale(request.getLocale());
	    response.getWriter().write(jsonResponse);
	}
    }

    private String processOffer(HttpServletRequest servletRequest)  throws IOException {
	// build the data to be shown by the editor
	if (dataProvider == null) dataProvider = new SampleDataProvider();
	IntelliTextEditorEnvironment environment = getIntelliTextEditorEnvironment(servletRequest);
	Locale vocabularyLocale = environment.getDefinition().getLocale();
	IlrBRLDefinition brlDefinition = environment.getDefinition();
	IlrVocabulary voc = environment.getParser().getVocabulary(brlDefinition, vocabularyLocale);
	    
	IlrConcept concept = voc.getConcept("sample.PromotionalOffer");
	IlrValueDescriptor valueDescriptor = getValueDescriptor(concept, voc, brlDefinition.getClassLoader());

	JSONArray data = new JSONArray();
	for (int i = 0; i < dataProvider.getCount(); i++) {
	    String offerDecoratedName = dataProvider.getValue(i);
	    if (valueDescriptor != null) {
		offerDecoratedName = valueDescriptor.getLocalizedText(dataProvider.getValue(i), brlDefinition);
	    }
	    JSONObject object = new JSONObject();
	    object.put("value", dataProvider.getValue(i));
	    object.put("description", dataProvider.getDescription(i));
	    object.put("decoratedName", offerDecoratedName);
	    data.add(object);
	}
	return data.serialize();
    }
	private IlrValueDescriptor getValueDescriptor(IlrConcept concept,
																								IlrVocabulary vocabulary,
																								ClassLoader classLoader) {
		if (concept == null || vocabulary == null)
			return null;
		IlrValueDescriptor res = null;
		String key = (String) concept.getProperty("valueDescriptor");
		if (key != null) 
			res = IlrValueDescriptorFactory.findValueDescriptor(key, concept, classLoader, vocabulary);    
		return res;
	}

	public static IntelliTextEditorEnvironment getIntelliTextEditorEnvironment(HttpServletRequest servletRequest) {
        IntelliTextEditorEnvironment environment = (IntelliTextEditorEnvironment) servletRequest.getSession().getAttribute("intelliRuleEditorEnvironment");
        return environment;
    }
}
