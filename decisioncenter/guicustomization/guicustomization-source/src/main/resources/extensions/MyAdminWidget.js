define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dijit/_WidgetBase",
	"dijit/_TemplatedMixin",
	"dojo/text!./templates/MyAdminWidget.html"
], function (declare, lang, _WidgetBase, _TemplatedMixin, template) {
	
	// Widget to serve as an example to display a few metrics
	
	return declare([_WidgetBase, _TemplatedMixin], {
		parseOnLoad: true,
		templateString: template,
		
		postMixInProperties: function () {
			this.inherited(arguments);
			lang.mixin(this, this.data);
		}
	});
});
