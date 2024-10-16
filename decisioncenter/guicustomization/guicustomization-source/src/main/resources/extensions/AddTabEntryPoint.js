define(["dojo/_base/declare",
    	"dojo/_base/lang",
    	"dijit/registry",
    	"dojo/dom",
        "com/ibm/rules/decisioncenter/extensions/ExtensionMixin",
        "extensions/MyAdminWidget",
        "extensions/MyWidget"
], function(declare, lang, registry, dom, ExtensionMixin, MyAdminWidget, MyWidget) {
	
	// Entry point to add a new tab aside Decision Artifact, Tests, Simulations, ... tabs
		
	return declare([ExtensionMixin], {

		/* override */ inBranchView: function(data) {
			// Adds a new tab in a context of a branch
			
			this._inView(data);
		},
		
		/* override */ inReleaseView: function(data) {
			// Adds a new tab in a context of a release
			
			this._inView(data);
		},
		
		/* override */ inActivityView: function(data) {
			// Adds a new tab in a context of an activity
			
			this._inView(data);
		},
		
		_inView: function(data) {
			// Loads CSS file to style the content of the tab
			this.loadCss('/extensions/css/mytab.css');
			
			// Add a tab only if the user belongs to AdminGroup group
			if (this.array_contains(data.profile.userGroups, "AdminGroup")) {
				// Adds a new tab with the id 'MyTab', the title 'My Tab' 
				// and invokes the function '_loadContent' when the tab is selected
				this.addTab('MyTab', { title: 'My Admin Tab' }, lang.hitch(this, this._loadAdminContent));
			} else {
				this.addTab('MyTab', { title: 'My Tab' }, lang.hitch(this, this._loadContent));
			}
		},
		
		_loadAdminContent: function () {
			// Triggered when the tag is selected
			
			// Remove previously created widget
			var myWidget = registry.byId('myAdminWidget');
			if (myWidget != null) {
				myWidget.destroy();
			}
			
			// On success, add the widget as the content of the tab
			var onSuccess = function(data) {
				new MyAdminWidget({
					id:'myAdminWidget',
					data: data
				}).placeAt(dom.byId('MyTab'));
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
		
		_loadContent: function () {
			// Triggered when the tag is selected
			
			// Remove previously created widget
			var myWidget = registry.byId('myWidget');
			if (myWidget != null) {
				myWidget.destroy();
			}
			
			// On success, add the widget as the content of the tab
			var onSuccess = function(data) {
				new MyWidget({
					id:'myWidget',
					data: data
				}).placeAt(dom.byId('MyTab'));
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
