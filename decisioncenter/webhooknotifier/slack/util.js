/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5724-Y00 5724-Y17 5655-V84
* Copyright IBM Corp. 2009, 2024. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/
const isDgfEvent = (eventType) => {
    "use strict";
    return eventType === "ReleaseCreated" ||
        eventType === "ReleaseUpdated" ||
        eventType === "ReleaseDeleted" ||
        eventType === "ActivityCreated" ||
        eventType === "ActivityUpdated" ||
        eventType === "ActivityDeleted";
};

const isDeploymentEvent = (eventType) => {
    "use strict";
    return eventType === "RuleAppDeployment"||
        eventType === "DeploymentReportDeleted";
};

const isRuleEvent = (eventType) => {
    "use strict";
    return  eventType === "RuleCreated"||
        eventType === "RuleUpdated"||
        eventType === "RuleDeleted";
};

const isRuleflowEvent = (eventType) => {
    "use strict";
    return eventType === "RuleflowCreated"||
        eventType === "RuleflowUpdated"||
        eventType === "RuleflowDeleted";
};

const  isTestsuiteEvent = (eventType) => {
    "use strict";
    return eventType === "TestsuiteCreated"||
        eventType === "TestsuiteUpdated"||
        eventType === "TestsuiteDeleted";
};

const  isBranchEvent = (eventType) => {
    "use strict";
    return eventType === "BranchCreated"||
        eventType === "BranchUpdated"||
        eventType === "BranchDeleted";
};

const  isSnapshotEvent = (eventType) => {
    "use strict";
    return eventType === "SnapshotCreated"||
        eventType === "SnapshotUpdated"||
        eventType === "SnapshotDeleted";
};

const  isVariablesetEvent = (eventType) => {
    "use strict";
    return eventType === "VariablesetCreated"||
        eventType === "VariablesetUpdated"||
        eventType === "VariablesetDeleted";
};

const isRulepackageEvent = (eventType) =>{
    "use strict";
    return eventType === "RulePackageCreated"||
        eventType === "RulePackageUpdated"||
        eventType === "RulePackageDeleted";
};
const isOperationEvent = (eventType) =>{
    "use strict";
    return eventType === "OperationCreated"||
        eventType === "OperationUpdated"||
        eventType === "OperationDeleted";
};

/*
const  isEvent = (eventType) => {
    "use strict";
    return eventType === "Created"||
        eventType === "Updated"||
        eventType === "Deleted";
};
*/

const userId = (usersInfo, key) => {
    "use strict";
    if (usersInfo && usersInfo.members) {
        const member = usersInfo.members.filter((member) => {
            return member.profile.email === key;
        });
        if (member.length > 0) {
            return member[0].id;
        }
    }
    return null;
};

const userRealName = (usersInfo, key, normalized) => {
    "use strict";
    if (usersInfo && usersInfo.members) {
        const member = usersInfo.members.filter((member) => {
            return member.profile.email === key;
        });
        if (member.length > 0) {
            return normalized ? member[0].profile.real_name_normalized : member[0].profile.real_name;
        }
    }
    return key
};

const slackUserId = (usersInfo, key) => {
    "use strict";
    const id = userId(usersInfo, key, true);
    if (id === null) {
        return userRealName(usersInfo, key, true);
    } else {
        return "<@" + id + ">";
    }
};

module.exports = {
    isDgfEvent, isDeploymentEvent, isRuleEvent, isRuleflowEvent, isBranchEvent, isTestsuiteEvent, isSnapshotEvent, isVariablesetEvent, isRulepackageEvent, isOperationEvent, userId, userRealName, slackUserId
};