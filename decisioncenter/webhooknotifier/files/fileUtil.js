/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5724-Y00 5724-Y17 5655-V84
* Copyright IBM Corp. 2009, 2025. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/
const deploymentContent = (event) => {
    "use strict";
    return "Deployment of \'" + event.content[0].ruleAppName
			+ "\' " 
			+ " in project \'" + event.project.name 
			+ "\' from \'" + event.decisionService.name
			+ " " + event.content[0].status
			+ " by \'" + event.author
			+ "\' see \'" + event.content[0].name
			+ "\'.\n";
};

const rule = (event) => {
    "use strict";
    return event.type + " " + event.content[0].name
		+ " by \'" + event.author + "\'"
		+ " in project \'" + event.project.name 
		+ "\' from \'" + event.decisionService.name
		+ "\'.\n";
};

const release = (event) => {
    "use strict";
    return event.type + " " + event.content[0].name
		+ " by \'" + event.author + "\'"
		+ " in decision service \'" + event.decisionService.name
		+ "\'.\n";
};

const activity = (event) => {
    "use strict";
    return event.type + " " + event.content[0].name
		+ " by \'" + event.author + "\'"
		+ " in decision service \'" + event.decisionService.name
		+ "\'.\n";
};


const ruleflow = (event) => {
    "use strict";
    return event.type + " " + event.content[0].name
        + " by \'" + event.author + "\'"
        + " in project \'" + event.project.name
        + "\' from \'" + event.decisionService.name
        + "\'.\n";
};


const snapshot = (event) => {
    "use strict";
    return event.type + " " + event.content[0].name
        + " by \'" + event.author + "\'"
        + " in project \'" + event.project.name
        + "\' from \'" + event.decisionService.name
        + "\'.\n";
};

const testsuite = (event) => {
    "use strict";
    return event.type + " " + event.content[0].name
        + " by \'" + event.author + "\'"
        + " in project \'" + event.project.name
        + "\' from \'" + event.decisionService.name
        + "\'.\n";
};

const branch = (event) => {
    "use strict";
    return event.type + " " + event.content[0].name
        + " by \'" + event.author + "\'"
        + " in project \'" + event.project.name
        + "\' from \'" + event.decisionService.name
        + "\'.\n";
};

const variableset = (event) => {
    "use strict";
    return event.type + " " + event.content[0].name
        + " by \'" + event.author + "\'"
        + " in project \'" + event.project.name
        + "\' from \'" + event.decisionService.name
        + "\'.\n";
};

const operation = (event) => {
    "use strict";
    return event.type + " " + event.content[0].name
        + " by \'" + event.author + "\'"
        + " in project \'" + event.project.name
        + "\' from \'" + event.decisionService.name
        + "\'.\n";
};

module.exports = {
    deploymentContent, rule, release, activity, ruleflow, testsuite, snapshot, branch, variableset, operation
};