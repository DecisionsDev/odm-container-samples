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

const EVENT_VARIABLESET_CREATED = "VariablesetCreated";
const EVENT_VARIABLESET_UPDATED = "VariablesetUpdated";
const EVENT_VARIABLESET_DELETED = "VariablesetDeleted";

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
    return "";
};

const slackFallback = (event) => {
    "use strict";
    return slackPretext(event, false);
};
const slackPretext = (event, showEmoji) => {
    "use strict";
    const type = event.type;
    let variableset = event.content[0];
    let title = showEmoji ? emoji(event) : "";
    const variablesetLink = (event.details && event.details[0] && event.details[0].targetURL) ? event.details[0].targetURL : event.sourceLink;
    title += "  Variableset <" + variablesetLink + "|" + variableset.name + ">";
    if (type === EVENT_VARIABLESET_CREATED) {
        title += " was created";
    } else if (type === EVENT_VARIABLESET_UPDATED) {
        if(event.details[0].updateType ==="RENAME") {
            title += " was renamed from '" + event.details[0].oldValue + "'";
        } else {
            title += " was updated";
        }
    } else if (type === EVENT_VARIABLESET_DELETED) {
        title += " was deleted";
    }
    return title + " in project \'" + event.project.name
        + "\' from decision service \'" + event.decisionService.name + " \' ";
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
    return ":odm-variableset:";
};

module.exports = {
    slackAttachments
};
