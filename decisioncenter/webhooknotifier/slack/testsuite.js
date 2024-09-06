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

const EVENT_TESTSUITE_CREATED = "TestsuiteCreated";
const EVENT_TESTSUITE_UPDATED = "TestsuiteUpdated";
const EVENT_TESTSUITE_DELETED = "TestsuiteDeleted";


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
    let testsuite = event.content[0];
    let title = showEmoji ? emoji(event) : "";
    const testsuiteLink = (event.details && event.details[0] && event.details[0].targetURL) ? event.details[0].targetURL : event.sourceLink;
    title += "  Testsuite <" + testsuiteLink + "|" + testsuite.name + ">";
    if (type === EVENT_TESTSUITE_CREATED) {
        title += " was created";
    } else if (type === EVENT_TESTSUITE_UPDATED) {
        if(event.details[0].updateType ==="RENAME") {
            title += " was renamed from '" + event.details[0].oldValue + "'";
        } else {
            title += " was updated";
        }    } else if (type === EVENT_TESTSUITE_DELETED) {
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
    return ":odm-testsuite:";
};

module.exports = {
    slackAttachments
};
