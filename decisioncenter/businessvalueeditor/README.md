# Custom Value Editor

The aim of this tutorial is to ecplain how to deploy the [Custom value editor sample](https://www.ibm.com/docs/en/odm/9.0.0?topic=center-custom-value-editor) in a kubernetes context

## Prerequisites

Get the  [decision-center-client-api.zip](../README.md#build-the-java-samples) file and setup the [Http file server](../README.md#setup-an-httpd-file-server)

## Build the customvalueeditor.jar file

To build the customvalueeditor.jar file, you will first have to retrieve the [decision-center-client-api.zip](../README.md#build-the-java-samples) file.
Create a project in your favorite java development environment and import the java source file from [customvalueditor.zip](./customvalueditor.zip).
Build and export the customvalueeditor.jar file.
Using Eclipse, it's the right-click menu Export>Java>JAR file

## Upload the customvalueditor.zip file

Create the businessvalueeditor.zip :

```bash
zip -r businessvalueeditor.zip customvalueeditor.jar js/ web.xml
```

Upload customvalueditor.zip on the httpd file server

```bash
curl -T businessvalueeditor.zip http://<fileserver-url>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>201 Created</title>
</head><body>
<h1>Created</h1>
<p>Resource /distrib-liberty.zip has been created.</p>
</body></html>
```

## Instanciate the ODM Helm Chart

Follow the explanation on how to deploy the ODM Helm Chart according to your [platform](https://github.com/DecisionsDev/odm-docker-kubernetes/tree/master/platform)

Add to your values.yaml the parameter allowing to download businessvalueeditor.zip

<!-- markdown-link-check-disable -->
```
decisionCenter:
  downloadUrl:
  - http://fileserver-apache.sample.svc.cluster.local:80/businessvalueeditor.zip
```
<!-- markdown-link-check-enable -->

Or if it's using the command line : **--set decisionCenter.downloadUrl={"http://fileserver-apache.sample.svc.cluster.local:80/businessvalueeditor.zip"}**

## Add the custom setting property in Decision Center

To activate the Business value editor, after login in Decision Center as an administrator, go in the menu "Administration>Settings>Custom Settings"
Register a new setting named **decisioncenter.web.core.intelliruleEditor.sample.myeditor.editor** with the value **businessvalueeditor.OfferValueEditorProvider**

![Custom Settings](images/custom_settings.png)

## Test the sample

Load the [ValueEditorService.zip](./ValueEditorService.zip) Decision Service.
Follow [Running this sample](https://www.ibm.com/docs/en/odm/9.0.0?topic=editor-custom-value-sample-details#businessconsolecustomvalueeditorsampledetails__rssamples.uss_rs_smp_tsauthoring.1025134__title__1) details to understand how to use the custom value editor.

![Custom Value Editor](images/custom_value_editor.png)
