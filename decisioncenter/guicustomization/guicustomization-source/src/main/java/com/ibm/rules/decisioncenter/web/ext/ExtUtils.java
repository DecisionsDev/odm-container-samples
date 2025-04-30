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

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ilog.rules.teamserver.model.IlrSession;

public abstract class ExtUtils {
    /** Get the session from the request
	*/
	public static IlrSession retrieveSession() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		IlrSession session = (IlrSession) request.getSession().getAttribute("session");
		if (session == null) {
			throw new IllegalStateException("No session available. Please make sure you have logged in to Decision Center");
		}
		return session;
	}
	
}
