/*
 * Licensed Materials - Property of IBM                           
 * 5725-B69 5655-Y17                                              
 * Copyright IBM Corp. 2013, 2020. All Rights Reserved            
 * US Government Users Restricted Rights - Use, duplication or    
 * disclosure restricted by GSA ADP Schedule Contract with        
 * IBM Corp.                                                      
 */

define([
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/dom",
    "dojo/dom-construct",
    "dojo/dom-attr",
    "dojo/on",
    "dijit/focus",
    "com/ibm/bdsl/web/editor/Utils",
    "com/ibm/bdsl/web/valueeditors/ValueEditorBase"
], function (declare, lang, array, dom, domConstruct, domAttr, on, focusUtil, utils, ValueEditorBase) {

    return declare(ValueEditorBase, {

        _eventHandlers: null,
        serviceUrl: null,
        offerList: null,
        offerDetail: null,

        constructor: function() {
            this._eventHandlers = [];
            var path = window.contextPath || window.config.contextPath; // Enterprise Console || Business Console context path variable
            this.serviceUrl = path + '/servlet/SampleValueEditorServletName';
        },

        getDomNode: function () {
            this.inherited(arguments);

            var divEditor = domConstruct.create('div', {'class': 'popupWindow folderSelector'});
            this.offerList = domConstruct.create('select', {id: 'offerList', autocomplete: 'true'}, divEditor);
            this.offerDetail = domConstruct.create('input', {id: 'offerDetail', type: 'text', size: '43%', maxlength: '40'}, divEditor);
            var buttonsDiv = domConstruct.create('div', { align: 'center', style: 'padding-top: 3px' }, divEditor);
            var btnOk = domConstruct.create('button', {id: 'offerOK', width: '100%', innerHTML: 'OK'}, buttonsDiv);
            var btnCancel = domConstruct.create('button', {id: 'offerCancel', width: '100%', innerHTML: 'Cancel'}, buttonsDiv);

            this._eventHandlers.push(on(this.offerList, 'change', lang.hitch(this, function() {
                domAttr.set(this.offerDetail, 'value', domAttr.get(this.offerList[this.offerList.selectedIndex], 'title'));
            })));

            this._eventHandlers.push(on(btnOk, 'click', lang.hitch(this, function() {
                var dropdownValue = domAttr.get(this.offerList[this.offerList.selectedIndex], 'decoratedName');
                this.setValue(dropdownValue);
                this.commit();
            })));

            this._eventHandlers.push(on(btnCancel, 'click', lang.hitch(this, function() {
                this.cancel();
            })));

            return divEditor;
        },

        startEdit: function () {
            this.inherited(arguments);
            var initialText = this.getValue();
            utils.doAjaxPost({
                url: this.serviceUrl,
                content: { offer: 'true' },
                load: lang.hitch(this, function(jsonData) {
                    if (jsonData !== null && jsonData !== undefined  && jsonData instanceof Array) {
                        array.forEach(jsonData, lang.hitch(this, function(offer, index) {
                            domConstruct.create('option', {title: offer.description, decoratedName: offer.decoratedName, innerHTML: offer.value}, this.offerList);
                            if (initialText === offer.decoratedName) {
                                this.offerList.selectedIndex = index;
                                domAttr.set(this.offerDetail, 'value', offer.description);
                            }
                        }));
                    }
                })
            }, this.editor);
            focusUtil.focus(dom.byId("offerList"));
        },

        dispose: function () {
            this.inherited(arguments);
            array.forEach(this._eventHandlers, function(handle) {
                handle.remove();
            });
            this._eventHandlers = [];
            this.offerList = null;
            this.offerDetail = null;
            focusUtil.focus(this.editor.domNode);
        }

    });

});
