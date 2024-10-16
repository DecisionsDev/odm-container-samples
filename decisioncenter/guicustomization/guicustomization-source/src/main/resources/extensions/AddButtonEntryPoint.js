define(["dojo/_base/declare",
    	"dojo/_base/lang",
    	"dijit/registry",
    	"dojo/dom",
    	"dijit/Dialog",
        "com/ibm/rules/decisioncenter/extensions/ExtensionMixin",
        "extensions/MyAdminWidget",
        "extensions/MyWidget"
], function(declare, lang, registry, dom, Dialog, ExtensionMixin, MyAdminWidget, MyWidget) {
	
	// Entry point to add a new button in the toolbar of the view
	
	return declare([ExtensionMixin], {
		
		/* override */ inBranchView: function(data) {
			// Adds a new button in a context of a branch
			
			this._inView(data);
		},
		
		/* override */ inReleaseView: function(data) {
			// Adds a new button in a context of a release
			
			this._inView(data);
		},
		
		/* override */ inActivityView: function(data) {
			// Adds a new button in a context of an activity
			
			this._inView(data);
		},
		
		_inView: function(data) {
			// Adds a new button
			
			// Loads CSS file to style the button
			this.loadCss('/extensions/css/mybutton.css');
			
			// Add a button only if the user belongs to AdminGroup group
			if (this.array_contains(data.profile.userGroups, "AdminGroup")) {
			// Adds a new button with the id 'MyButton', the label 'My Button' 
			// and invokes the function '_onClick' when the button is clicked
				this.addToolbarButton('MyButton', 'My Admin Button', lang.hitch(this, this._onAdminClick));
			  
			}  else {
				this.addToolbarButton('MyButton', 'My Button', lang.hitch(this, this._onClick));
            }
		},
		
		_onClick: function () {
			// Triggered when the button is clicked.
			
			var onSuccess = function(data) {
				// Display a dialog that display a few metrics
				var w = new MyWidget({
					id:'myDialogWidget',
					data: data
				});
				
				
				var d = new Dialog({
					id: 'MetricDialog',
					title: "Metrics",
					content: w,
					onHide: function() {
						// To prevent destroying the dialog before the animation ends
						setTimeout(lang.hitch(this, 'destroyRecursive'), 0);
					}
				});

				d.show();
			};
			
			// On error, display an error in the console
			var onError = function(err) {
				console.log(err);
			};
			var params = {
				branchId: viewConfig.baselineId	
			};
			
			// Send an HTTP request to get data from the server
			this.sendRequest('/ext/custom/data', 
					{ onSuccess : onSuccess, onError: onError, parameters: params},
					'GET');
		},
		
		_onAdminClick: function () {
			// Triggered when the button is clicked.
			
			var onSuccess = function(data) {
				// Display a dialog that display a few metrics
				var w = new MyAdminWidget({
					id:'myDialogWidget',
					data: data
				});
				
				
				var d = new Dialog({
					id: 'MetricDialog',
					title: "Admin Metrics",
					content: w,
					onHide: function() {
						// To prevent destroying the dialog before the animation ends
						setTimeout(lang.hitch(this, 'destroyRecursive'), 0);
					}
				});

				d.show();
			};
			
			// On error, display an error in the console
			var onError = function(err) {
				console.log(err);
			};
			var params = {
				branchId: viewConfig.baselineId	
			};
			
			// Send an HTTP request to get data from the server
			this.sendRequest('/ext/custom/data', 
					{ onSuccess : onSuccess, onError: onError, parameters: params},
					'GET');
		},

		array_contains: function (array, element) {
			if (!lang.isArray(array)) {
				return false;
			}
			var count = array.length;
			for (var i = 0; i < count; i++) {
				if (array[i] ==  element) {
					return true;
				}
			}
			return false;
		},

	});
});
