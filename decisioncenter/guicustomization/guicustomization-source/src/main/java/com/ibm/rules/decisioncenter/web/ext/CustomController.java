/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5724-Y00 5724-Y17 5655-V84
* Copyright IBM Corp. 2009, 2025. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/
package com.ibm.rules.decisioncenter.web.ext;

import ilog.rules.teamserver.brm.IlrBaseline;
import ilog.rules.teamserver.brm.IlrBrmPackage;
import ilog.rules.teamserver.brm.IlrReportStatusKind;
import ilog.rules.teamserver.brm.IlrRule;
import ilog.rules.teamserver.brm.IlrRulePackage;
import ilog.rules.teamserver.brm.IlrServer;
import ilog.rules.teamserver.dsm.IlrDSDeploymentReport;
import ilog.rules.teamserver.dsm.IlrDeployment;
import ilog.rules.teamserver.dsm.IlrDeploymentError;
import ilog.rules.teamserver.dsm.IlrDsmPackage;
import ilog.rules.teamserver.model.IlrApplicationException;
import ilog.rules.teamserver.model.IlrArchiveOutput;
import ilog.rules.teamserver.model.IlrDefaultSearchCriteria;
import ilog.rules.teamserver.model.IlrElementHandle;
import ilog.rules.teamserver.model.IlrObjectNotFoundException;
import ilog.rules.teamserver.model.IlrSession;
import ilog.rules.teamserver.validation.IlrValidationPackage;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

/**
 * Class to server HTTP requests used by the extension point
 *
 */
@Controller
public class CustomController {

	@RequestMapping(value = "/ext/custom/data", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> data(@RequestParam(value="branchId", required=false) String branchId) throws IlrApplicationException {
		IlrSession session = ExtUtils.retrieveSession();
		IlrBrmPackage brmPck = session.getBrmPackage();
		IlrDsmPackage dsmPck = session.getDsmPackage();
		IlrValidationPackage valPck = session.getValidationPackage();
		
		IlrBaseline baseline = null;
		if (branchId != null) {
			IlrElementHandle elementHandle = session.stringToElementHandle(branchId);
			baseline = (IlrBaseline) session.getElementDetailsForThisHandle(elementHandle);
		}
		
		Map<String, Object> model = Maps.newHashMap();

		IlrDefaultSearchCriteria query = new IlrDefaultSearchCriteria(dsmPck.getDeployment());
		query.setBaseline(baseline);
		
		model.put("numberofdeploymentconfigurations", session.findElementHandles(query).size());
		
		query.setEClass(dsmPck.getOperation());
		List<IlrElementHandle> operations = session.findElementHandles(query);
		model.put("numberofoperations", operations.size());
		
		query.setEClass(brmPck.getBusinessRule());
		List<Object> rules = session.findElements(query);
		buildRuleReport(model, rules);

		model.put("numberofrules", rules.size());
		
		query.setEClass(brmPck.getDecisionTable());
		model.put("numberofdecisiontables", session.findElementHandles(query).size());

		query.setEClass(brmPck.getRuleflow());
		model.put("numberofruleflows", session.findElementHandles(query).size());

		
		query.setEClass(valPck.getTestSuite());
		model.put("numberoftestsuites", session.findElementHandles(query).size());
		
		query.setEClass(valPck.getSimulationConfiguration());
		model.put("numberofsimulations", session.findElementHandles(query).size());
		
		return model;
	}
	


	private void buildRuleReport(Map<String, Object> model, List<Object> rules) {
		String report = "<h2>Rules count : " + rules.size() + "</h2>\n";
		Iterator<Object> it = rules.iterator();
		IlrRule rule = null;
		IlrRulePackage rulePackage = null;
		report += "<ul>";
		while (it.hasNext()) {
			rule = (IlrRule)it.next();
			try {
				if (rulePackage != rule.getRulePackage()) {
					if (rulePackage != null) {
						report += "</ul> </li>";
					}
					rulePackage = rule.getRulePackage();
					report += "<li> Package " + rulePackage.getName() + ":";
                    report += "<ul>";
				}
			} catch (IlrObjectNotFoundException e) {;
				rulePackage = null;
			}
		
			report += "<li>" +  rule.getName() + "</li>";
		}
		if (rulePackage != null) {
			report += "</ul> </li>";
		}
		report += "</ul>";
		model.put("report", report);		
	}



	@RequestMapping(value = "/ext/custom/deployment", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deployment(
			@RequestParam(value="branchId", required=true) String branchId,
			@RequestParam(value="deploymentConfigurationId", required=true) String deploymentConfigurationId,
			@RequestParam(value="targetServerIds", required=true) List<String> targetServerIds) throws IlrApplicationException {
		IlrSession session = ExtUtils.retrieveSession();
		
		IlrBaseline baseline = (IlrBaseline) session.getElementDetailsForThisHandle(session.stringToElementHandle(branchId));
		session.setWorkingBaseline(baseline);
		
		List<IlrServer> servers = new ArrayList<IlrServer>(targetServerIds.size());
		for (String targetServerId : targetServerIds) {
			IlrServer server = (IlrServer) session.getElementDetails(session.stringToElementHandle(targetServerId));
			if (server != null) {
				servers.add(server);
			}
		}
		
		// get the exact version of the deployment configuration, not the last version
		IlrDeployment deployment = (IlrDeployment) session.getElementDetailsForThisHandle(session.stringToElementHandle(deploymentConfigurationId));

		// Do some pre computation
		System.out.println(MessageFormat.format("Do some stuffs BEFORE deploying with the configuration deployment ''{0}''", deployment.getName()));
		
		String deploymentBaseline = "CustomDeploymentBaseline" + System.currentTimeMillis();
		IlrArchiveOutput archive = null;
		String exceptionMessage = null;
		try {
			archive = session.deployDSRuleAppArchive(deployment, servers, deploymentBaseline, false, null);
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		
		// Do some post computation
		System.out.println(MessageFormat.format("Do some stuffs AFTER the deployment occured with the configuration deployment ''{0}''", deployment.getName()));
		
		Map<String, Object> model = Maps.newHashMap();
		
		if (exceptionMessage != null) {
			model.put("hasError", true);
			model.put("errorMsg", exceptionMessage);
		} else {
			IlrDSDeploymentReport report = (IlrDSDeploymentReport) session.getElementDetailsForThisHandle((IlrElementHandle) archive.getAttribute(IlrArchiveOutput.DEPLOYMENT_REPORT_ATTRIBUTE));
			model.put("deploymentReportName", report.getName());
			model.put("deploymentReportStatus", report.getStatus().toString());
			model.put("hasError", report.getStatus() != IlrReportStatusKind.COMPLETED_LITERAL);
			if(report.getStatus()== IlrReportStatusKind.COMPLETED_LITERAL){
				model.put("errorMsg", "There are no error.");
			}
			else{
				List<IlrDeploymentError> errors = report.getErrors();
				List<IlrDeploymentError> realErrors  = new ArrayList<IlrDeploymentError>();
				for (IlrDeploymentError error : errors) {
					if(error != null && error.getMessage() != null){
						String msg = new String(error.getMessage(), StandardCharsets.UTF_8);
						if(msg.startsWith("!ERROR!")){
							realErrors.add(error);
						}
					}
				}
				model.put("errorMsg", realErrors.isEmpty() ? " There are no error." : "There are " + realErrors.size() + " errors.");
			}
		}
		
		return model;
	}
}
