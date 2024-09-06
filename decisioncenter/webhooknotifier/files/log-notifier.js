/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5724-Y00 5724-Y17 5655-V84
* Copyright IBM Corp. 2009, 2024. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/
"use strict";
const express = require("express");
const bodyParser = require("body-parser");
const bearerToken = require('express-bearer-token');
const fs = require('fs'); 
const fileUtil = require("./fileUtil");

// Constants
const PORT = 3000;    //
// The port to which DC events are sent (defined in the webhook URL)

// select the file name and the displayed contents according to the event type.
const logInFile = (event) => {
	"use strict";
	var fileName = "default.txt";
	var content = JSON.stringify(event, null, 2);
	// create results folder if it does not exist
	if (!fs.existsSync('results')){
		fs.mkdirSync('results');
	}
	switch (event.type) {
		case 'RuleAppDeployment' : fileName = "deployments.txt";
	      content = fileUtil.deploymentContent(event);
		  break;
		case 'RuleCreated' : 
		case 'RuleUpdated':
		case 'RuleDeleted': fileName = "rules.txt";
	      content = fileUtil.rule(event);
		  break;
		case 'ReleaseCreated' :
		case 'ReleaseUpdated' :
		case 'ReleaseDeleted' : fileName = "releases.txt";
			content = fileUtil.release(event);
			break;
		case 'ActivityCreated' :
		case 'ActivityUpdated' :
		case 'ActivityDeleted' : fileName = "activities.txt";
			content = fileUtil.activity(event);
		break;
        case 'RuleflowCreated' :
        case 'RuleflowUpdated':
        case 'RuleflowDeleted': fileName = "ruleflows.txt";
            content = fileUtil.ruleflow(event);
            break;
	}
    fs.appendFile('results/' + fileName, content, function (err) {
    if (err) throw err;
    console.log('Saved!');
	})
};

// start Node.js Express application
const app = express();

app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
app.use(bearerToken());

// listen to POST events on "print" endpoint: write in a file the event in req.body
app.post("/print", (req, res) => {
            console.log(JSON.stringify(req.body, null, 2));
            logInFile(req.body);
 });

app.listen(PORT, '0.0.0.0', () => {
	console.log("Ready to receive events from ODM Decision Center");
});