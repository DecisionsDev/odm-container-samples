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

// Decision Governance Framework Constants

const EVENT_RELEASE_CREATED = "ReleaseCreated";
const EVENT_RELEASE_UPDATED = "ReleaseUpdated";
const EVENT_RELEASE_DELETED = "ReleaseDeleted";
const EVENT_ACTIVITY_CREATED = "ActivityCreated";
const EVENT_ACTIVITY_UPDATED = "ActivityUpdated";
const EVENT_ACTIVITY_DELETED = "ActivityDeleted";

const TYPE_RELEASE = "Release";
const TYPE_CHANGE_ACTIVITY = "ChangeActivity";
const TYPE_VALIDATION_ACTIVITY = "ValidationActivity";

// generate Slack attachments for a Slack message
const slackAttachments = (event, usersInfo) => {
    "use strict";
    return {
        "text": slackText(event, usersInfo),
        "fallback": slackFallback(event),
        "color": "#7CD197",
        "pretext": slackPretext(event),
        "fields": displayAttachementFields(event) ? slackAttachmentFields(event, usersInfo) : null,
        "footer": slackFooter(event, usersInfo),
        "mrkdwn_in": ["pretext", "text", "title"]
    }
};

const slackFallback = (event) => {
    "use strict";
    return slackTitle(event, false, false);
};

const slackTitle = (event, showEmoji, showStatus) => {
    "use strict";
    const eventType = event.type;
    const name = event.content[0].name;

    let title = (showEmoji ? emoji(event) : "");
    if (eventType === EVENT_RELEASE_CREATED) {
        title += " Release " + name + " created";
    } else if (eventType === EVENT_RELEASE_UPDATED) {
        title += " Release " + name + " updated";
    } else if (eventType === EVENT_RELEASE_DELETED) {
        title += " Release " + name + " deleted";
    } else if (eventType === EVENT_ACTIVITY_CREATED) {
        title += " Activity " + name + " created";
    } else if (eventType === EVENT_ACTIVITY_UPDATED) {
        title += " Activity " + name + " updated";
    } else if (eventType === EVENT_ACTIVITY_DELETED) {
        title += " Activity " + name + " deleted";
    }
    return (showStatus ? (title + " - _" + status(event.content[0].status) + "_") : title);
};

// The text shown below the title, and before the attachments
const slackPretext = (event) => {
    "use strict";
    const eventType = event.type;

    let title = emoji(event);
    if (eventType === EVENT_RELEASE_CREATED || eventType === EVENT_RELEASE_UPDATED || eventType === EVENT_RELEASE_DELETED) {
        title += " *Release";
    } else if (eventType === EVENT_ACTIVITY_CREATED|| eventType === EVENT_ACTIVITY_UPDATED || eventType === EVENT_ACTIVITY_DELETED) {
        title += " *Activity";
    }
    let link = (event.details && event.details[0].targetURL) ? event.details[0].targetURL : event.sourceLink;
    title += " <" + link + "|" + event.content[0].name + ">";
    if (eventType === EVENT_RELEASE_CREATED || eventType === EVENT_ACTIVITY_CREATED) {
        title += " created*";
    } else if (eventType === EVENT_RELEASE_UPDATED|| eventType === EVENT_ACTIVITY_UPDATED) {
        title += " updated*";
    } else if (eventType === EVENT_RELEASE_DELETED|| eventType === EVENT_ACTIVITY_DELETED) {
        title += " deleted*";
    }
    return title + " _(" + status(event.content[0].status) + ")_";
};

const slackText = (event, usersInfo) => {
    "use strict";
    if (event.type === EVENT_RELEASE_UPDATED || event.type === EVENT_ACTIVITY_UPDATED) {
        return slackUpdateText(event, usersInfo);
    } else {
        if (event.content[0].documentation) {
            return "_" + event.content[0].documentation + "_";
        }
    }
    return null;
};

const slackUpdateText = (event, usersInfo) => {
    "use strict";
    const author = util.slackUserId(usersInfo, event.author);
    let text = "";
    for (let detail of event.details) {
        switch (detail.updateType) {
            case "CANCEL":
                text = "Canceled by _" + author + "_";
                break;
            case "APPROVE":
                text = "Approved by _" + author + "_";
                break;
            case "REJECT":
                text = "Rejected by _" + author + "_";
                break;
            case "AUTHOR_COMPLETE":
                text = "Author _" + author + "_ finished working";
                break;
            case "AUTHOR_RESUME":
                text = "Author _" + author + "_ resumed working";
                break;
            case "RENAME":
                text = "Renamed from _" + detail.oldValue + "_ to _" + detail.newValue + "_ by _" + author + "_";
                break;
            case "UPDATE_STATUS":
                text = "Status changed from _" + status(detail.oldValue) + "_ to _" + status(detail.newValue) + "_";
                break;
            case "UPDATE_DUE_DATE":
                text = "Due date changed from _" + dateFormat(new Date(detail.oldValue)) + "_ to _" + dateFormat(new Date(detail.newValue)) + "_";
                break;
            case "UPDATE_GOAL":
                text = "Description of the goal updated by _" + author + "_";
                break;
            case "ASSIGN_OWNER":
                text = "Owner changed " + " to _" + detail.newValue + "_";
                break;
            case "ADD_AUTHOR":
                text = "Author _" + util.slackUserId(usersInfo, detail.newValue) + "_ added";
                break;
            case "REMOVE_AUTHOR":
                text = "Author _" + util.slackUserId(usersInfo, detail.oldValue) + "_ removed";
                break;
            case "ADD_APPROVER":
                text = "Approver _" + util.slackUserId(usersInfo, detail.newValue) + "_ added";
                break;
            case "REMOVE_APPROVER":
                text = "Approver _" + util.slackUserId(usersInfo, detail.oldValue) + "_ removed";
                break;
            case "TESTER_COMPLETE":
                text = "Tester _" + author + "_ finished working";
                break;
            case "TESTER_RESUME":
                text = "Tester _" + author + "_ resumed working";
                break;
            case "ADD_TESTER":
                text = "Tester _" + util.slackUserId(usersInfo, detail.newValue) + "_ added";
                break;
            case "REMOVE_TESTER":
                text = "Tester _" + util.slackUserId(usersInfo, detail.oldValue) + "_ removed";
                break;
            case "UPDATE_TEST_PLAN":
                text = "Test plan was updated: " + util.slackUserId(usersInfo, detail.newValue);
                break;
            default:
                text = null;
        }
    }

    return text;
};

const displayAttachementFields = (event) => {
    "use strict";
    return event.type === EVENT_RELEASE_CREATED || event.type === EVENT_ACTIVITY_CREATED
        || event.type === EVENT_RELEASE_DELETED || event.type === EVENT_ACTIVITY_DELETED;
};

const slackAttachmentFields = (event, usersInfo) => {
    "use strict";
    let ownerId = util.slackUserId(usersInfo, event.content[0].owner);
    if (ownerId === null) {
        ownerId = event.content[0].owner;
    }
    return [
        {
            "title": "Due date",
            "value": dateFormat(new Date(event.content[0].targetDate), "mediumDate"),
            "short": true
        },
        {
            "title": "Owner",
            "value": ownerId,
            "short": true
        },
        slackAttachmentParticipantsField(event, usersInfo, "Approvers"),
        slackAttachmentParticipantsField(event, usersInfo,
            event.content[0].kind === TYPE_CHANGE_ACTIVITY ? "Authors" : "Testers")
    ];
};

const slackAttachmentParticipantsField = (event, usersInfo, participantType) => {
    "use strict";
    let participants = null;
    if (participantType === "Approvers" && event.content[0].approvers) {
        participants = event.content[0].approvers;
    } else if (participantType === "Authors" && event.content[0].authors) {
        participants = event.content[0].authors;
    } else if (participantType === "Testers" && event.content[0].authors) {
        participants = event.content[0].authors;
    }

    if (null !== participants) {
        let value = "";
        for (let i in participants) {
            if (i > 0) {
                value = value.concat("\n");
            }
            let userId = util.slackUserId(usersInfo, participants[i].name);
            if (userId === null) {
                value = value.concat(participants[i].name);
            } else {
                value = value.concat(userId);
            }
        }
        if ("" !== value) {
            return {
                "title": participantType,
                "value": value,
                "short": true
            };
        }
    }
    return null;
};

const slackFooter = (event, usersInfo) => {
    "use strict";
    let footer = event.sourceName;
    if (event.details && event.details[0] && !event.details[0].auto) {
        // details.auto = true means the event was triggered automatically by Decision Center
        footer += " | " + util.userRealName(usersInfo, event.author, true);
    }
    return footer + " | " + dateFormat(new Date(event.date), "mmm d, yyyy h:MM:ss TT");
};

const emoji = (event) => {
    "use strict";
    const type = event.content[0].kind;
    if (type === TYPE_RELEASE) {
        return ":odm-release:";
    } else if (type === TYPE_CHANGE_ACTIVITY) {
        return ":odm-changeactivity:";
    } else if (type === TYPE_VALIDATION_ACTIVITY) {
        return ":odm-validationactivity:";
    }
    return "";
};

const status = (internalStatus) => {
    "use strict";
    if (internalStatus === "InProgress") {
        return "In progress";
    } else if (internalStatus === "ReadyForApproval") {
        return "Ready for approval";
    } else if (internalStatus === "Complete") {
        return "Complete";
    } else if (internalStatus === "Canceled") {
        return "Canceled";
    }
    return "";
};

module.exports = {
    slackAttachments
};

