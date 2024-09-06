/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5724-Y00 5724-Y17 5655-V84
* Copyright IBM Corp. 2009, 2024. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/
const dateFormat = require("dateformat");
const util = require("./util");

// Constants

const EVENT_RULEAPP_DEPLOYMENT = "RuleAppDeployment";
const EVENT_RULEAPP_DEPLOYMENT_REPORT_DELETED = "DeploymentReportDeleted";

const slackAttachments = (event, usersInfo) => {
    "use strict";
    return {
        "fallback": slackFallback(event),
        "color": "#7CD197",
        "pretext": slackPretext(event, true),
        "text": slackText(event),
        "footer": slackFooter(event, usersInfo),
        "mrkdwn_in": ["pretext", "text"]
    }
};

const slackText = (event) => {
    "use strict";
    const type = event.type;
    let title = "";
    if (type === EVENT_RULEAPP_DEPLOYMENT) {
        const reportName = event.content[0].name;
        const reportLink = (event.details && event.details[0] && event.details[0].targetURL) ? event.details[0].targetURL : event.sourceLink;
        title += "\n Decision Service: " + event.decisionService.name;
        title += "\n Deployment report: <" + reportLink + "|" + reportName + ">";
    }else if(type === EVENT_RULEAPP_DEPLOYMENT_REPORT_DELETED){
        const reportName = event.content[0].name;
        const reportLink = (event.details && event.details[0] && event.details[0].targetURL) ? event.details[0].targetURL : event.sourceLink;
        title += "\n Decision Service: " + event.decisionService.name;
        title += "\n Deployment report: <" + reportLink + "|" + reportName + "> has been deleted";
    }
    return title;
};

const slackFallback = (event) => {
    "use strict";
    return slackPretext(event, false);
};

const slackPretext = (event, showEmoji) => {
    "use strict";
    const type = event.type;
    let title = showEmoji ? emoji(event) : "";
    if (type === EVENT_RULEAPP_DEPLOYMENT) {
        title += " *Ruleapp "+ event.content[0].ruleAppName + " deployed*";
        title += "  - Status: " + status(event.content[0].status);
    }
    return title;
};

const slackFooter = (event, usersInfo) => {
    "use strict";
    let footer = event.sourceName;
    if (event.author) {
        footer += " | " + util.userRealName(usersInfo, event.author, true);
    }
    return footer + " | " + dateFormat(new Date(event.date), "mmm d, yyyy h:MM:ss TT");
};

const emoji = (event) => {
    "use strict";
    const type = event.type;
    if (type === EVENT_RULEAPP_DEPLOYMENT) {
        return ":odm-deployment:";
    }
    return "";
};

const status = (internalStatus) => {
    "use strict";
    return internalStatus ? internalStatus.toLowerCase() : "";
};

module.exports = {
    slackAttachments
};

