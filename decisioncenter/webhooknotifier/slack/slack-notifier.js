/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5724-Y00 5724-Y17 5655-V84
* Copyright IBM Corp. 2009, 2025. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/
"use strict";

const express = require("express");
const bodyParser = require("body-parser");
const rp = require("request-promise");
const rule = require("./rule");
const ruleflow = require("./ruleflow");
const testsuite = require("./testsuite");
const snapshot = require("./snapshot");
const variableset = require("./variableset");
const branch = require("./branch");
const rulepackage = require("./rulepackage");
const operation = require("./operation");

const dgf = require("./dgf");
const deployment = require("./deployment");
const util = require("./util");
const fs = require("fs");

// sign with default (HMAC SHA256)
const jwt = require('jsonwebtoken');
const bearerToken = require('express-bearer-token');

// Constants
const PORT = 3000;        // The port to which DC events are sent (defined in the webhook URL)

// Read the Slack end points definition in the JSON file "endPoints.json"
var SLACK_END_POINTS = null;
fs.readFile("./endPoints.json", "utf-8", function(err, data) {
    SLACK_END_POINTS = JSON.parse(data);
});

const notifySlack = (event) => {
    "use strict";
    for (let slackEndPoint of SLACK_END_POINTS) {
        const slackOptions = {
            method: "POST",
            uri: slackEndPoint.endpoint,
            json: true
        };
        if (slackEndPoint.notify && slackEndPoint.token) {
            rp({
                method: "GET",
                uri: "https://slack.com/api/users.list?token=" + slackEndPoint.token,
                json: true
            }).then((usersInfo) => {
                notifySlackInternal(event, usersInfo, slackOptions, slackEndPoint.notify);
            }).catch((err) => {
                console.log(err)
            })
        } else {
            notifySlackInternal(event, null, slackOptions, slackEndPoint.notify);
        }
    }
};

const notifySlackInternal = (event, usersInfo, slackOptions, notify) => {
    "use strict";
    if (event.content && event.content.length > 0 &&
        event.content[0].name.startsWith('%odm_initialRelease')) {
        return;
    }
    console.log(JSON.stringify(event, null, 2));
    var eventKind ;
    if(util.isRuleEvent(event.type)){ eventKind = rule; }
    else if (util.isDgfEvent(event.type)) { eventKind = dgf; }
    else if (util.isDeploymentEvent(event.type)) { eventKind = deployment; }
    else if (util.isRuleflowEvent(event.type)){eventKind = ruleflow;}
    else if (util.isTestsuiteEvent(event.type)){eventKind = testsuite;}
    else if (util.isBranchEvent(event.type)){eventKind = branch;}
    else if (util.isSnapshotEvent(event.type)){eventKind = snapshot;}
    else if (util.isVariablesetEvent(event.type)){eventKind = variableset}
    else if (util.isRulepackageEvent(event.type)){eventKind = rulepackage}
    else if (util.isOperationEvent(event.type)){eventKind = operation}

    const attachment = eventKind.slackAttachments(event, usersInfo);
    if (attachment) {
        slackOptions.body = {
            attachments: [
                attachment
            ]
        };
        console.log(JSON.stringify(slackOptions.body, null, 2));
        if (notify) {
            rp(slackOptions)
                .then((data) => {
                    console.log(data)
                })
                .catch((err) => {
                    console.log(err)
                })
        }
    }
};

// start Node.js Express application
const app = express();

// increase if size problems for http payloads
app.use(bodyParser.json({limit: '10mb', extended: true}))
app.use(bodyParser.urlencoded({limit: '10mb', extended: true}))
app.use(bearerToken());

// listen to POST events on "slack" endpoint: notify Slack of event in req.body
app.post("/slack", (req, res) => {
    // do not verify if no token is provided
    if (req.token) {
        jwt.verify(req.token, 'dummyPrivateKey', (err) => {
            if (err) {
                console.log(err);
                res.status(401).send("Unauthorized");
            } else {
                console.log(JSON.stringify(req.body, null, 2));
                notifySlack(req.body);
				res.sendStatus(200);
            }
        });
    } else {
        notifySlack(req.body);
		res.sendStatus(200);
    }
});

// listen to GET events on "token.generate" endpoint: generate an access token
app.get("/token.generate", (req, res) => {
    jwt.sign({
        data: 'dummy'
    }, 'dummyPrivateKey', (err, token) => {
        res.status(200).send(token);
    });
});

// listen to GET events on "token.verify" endpoint: verify an access token
app.get("/token.verify", (req, res) => {
    jwt.verify(req.param('token'), 'dummyPrivateKey', (err) => {
        if (err) {
            console.log(err);
            res.status(401).send("Not a valid authentication token");
        } else {
			console.log("Valid authentication token");
			res.status(200).send("Valid authentication token")
        }
    });
});

app.listen(PORT, () => {
    console.log("Ready to receive events from ODM Decision Center");
});