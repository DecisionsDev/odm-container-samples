define(["dojo/_base/declare",
    	"dojo/_base/lang",
    	"dijit/registry",
    	"dojo/_base/array",
    	"dojo/dom",
        "dojo/dom-construct",
    	"dijit/Dialog",
        "com/ibm/rules/decisioncenter/extensions/ExtensionMixin"
], function(declare, lang, registry, array, dom, domConstruct, Dialog, ExtensionMixin) {
	
	// Entry point to add a new button in the toolbar of the view
		
	return declare([ExtensionMixin], {
		
		/* override */ inBusinessArtifactView: function(data) {
			// Adds a new button in a context of a rule or a decision table
			
			// Loads CSS file to style the button
			this.loadCss('/extensions/css/mybutton.css');
			
			// If data.mode is 'createRule', we are creating a new business rule artifact
			if (data.mode != 'createRule') {
				// Adds a new button with the id 'MyEditorButton', the label 'My Info' 
				// and invokes the function '_onClick' when the button is clicked
				this.addToolbarButton('MyEditorButton', 'My Info', lang.hitch(this, function() { this._onRuleClick(data) }));
			}
		},
		
		/* override */ inDecisionOperationView: function(data) {
			// Adds a new button in a context of a decision operation
			
			// Loads CSS file to style the button
			this.loadCss('/extensions/css/mybutton.css');
			
			// If elementId is null, we are creating a new decision operation
			if (data.elementId != null) {
				// Adds a new button with the id 'MyEditorButton', the label 'My Info' 
				// and invokes the function '_onClick' when the button is clicked
				this.addToolbarButton('MyEditorButton', 'My Operation Info', lang.hitch(this, function() { this._onOperationClick(data) }));
			}
		},
		
		/* override */ inDeploymentConfigurationView: function(data) {
			// Adds a new button in a context of a deployment configuration
			
			// Loads CSS file to style the button
			this.loadCss('/extensions/css/mybutton.css');
			
			// If elementId is null, we are creating a new deployment configuration
			if (data.elementId != null) {
				// Adds a new button with the id 'CustomDeploy', the label 'Custom Deploy' 
				// and invokes the function '_onDeployClick' when the button is clicked
				this.addToolbarButton('CustomDeploy', 'Custom Deploy', lang.hitch(this, function() { this._onDeployClick(data) }));
			}
		},
		
		_onRuleClick: function (data) {
			// Triggered when the button is clicked on a rule or a decision table.
			
			// Display a dialog that display the id of the element
			var d = new Dialog({
				id: 'AboutElement',
				title: "About " + data.artifactName + "?",
				content: "My id is " + data.elementId,
				onHide: function() {
					// To prevent destroying the dialog before the animation ends
					setTimeout(lang.hitch(this, 'destroyRecursive'), 0);
				}
			});
			d.show();
		},
		
		_onOperationClick: function (data) {
			// Triggered when the button is clicked on a decision Operation
			
			// Display a dialog that display the id of the element
			var d = new Dialog({
				id: 'AboutElement',
				title: "About the operation " + data.operation.operationName + "?",
				content: "My id is " + data.elementId,
				onHide: function() {
					// To prevent destroying the dialog before the animation ends
					setTimeout(lang.hitch(this, 'destroyRecursive'), 0);
				}
			});
			d.show();
		},
		
		_onDeployClick: function (data) {
			// Triggered when the button is clicked.
			
			// Display a dialog that displays some information of the result of a deployment
			var d = new Dialog({
				id: "DeploymentStatusDialog",
				title: "Deployment Status",
				content: "Deploying...",
				onHide: function() {
					// To prevent destroying the dialog before the animation ends
					setTimeout(lang.hitch(this, 'destroyRecursive'), 0);
				}
			});
			d.show();
			
			// On success, add some information of the result of a deployment
			var onSuccess = function(data) {
				var content = 
					"<ul>" +
					"	<li>Report name: " + data.deploymentReportName + "</li>" +
					"	<li>Report status: " + data.deploymentReportStatus + "</li>" +
					(data.hasError ? 
					"	<li>Error message: " + data.errorMsg + "</li>" : "<li>Error message: No error</li>") + 
					"</ul>";
				d.setContent(content);
			};
			
			// On error, display an error in the console
			var onError = function(err) {
				console.log(err);
			};
			
			// Prepare data to send to the server
			var filteredTargetServers = array.filter(data.targets, function(item) {
				return item.active;
			});
			
			if (filteredTargetServers.length == 0) {
				d.setContent("<div>You must, at least, define one server.</>");
				return;
			}
			
			var params = {
					branchId: data.branchId,
					deploymentConfigurationId: data.elementId,
					targetServerIds: array.map(filteredTargetServers, function(target, index) {
											return target.id;
									})
			};
			
			// Send an HTTP request to get data from the server
			this.sendRequest('/ext/custom/deployment', 
					{ onSuccess : onSuccess, onError: onError, parameters: params},
					'GET');
		}

	});
});
