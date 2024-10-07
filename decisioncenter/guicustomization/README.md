# GUI customization

The aim of this tutorial is to ecplain how to deploy the [GUI customization sample](https://www.ibm.com/docs/en/odm/9.0.0?topic=center-gui-customization) in a kubernetes context

## Prerequisites

Get the  [decision-center-client-api.zip](../README.md#build-the-java-samples) file and setup the [Http file server](../README.md#setup-an-httpd-file-server)

## Build the guicustomization.jar file

To build the guicustomization.jar file, you will first have to retrieve the [decision-center-client-api.zip](../README.md#build-the-java-samples) file.
Create a project in your favorite java development environment and import the java source file from [guicustomization.zip](./guicustomization.zip).
Build and export the guicustomization.jar file.
Using Eclipse, it's the right-click menu Export>Java>JAR file

## Upload the guicustomization.jar file

Upload guicustomization.jar on the httpd file server

```bash
curl -T guicustomization.jar http://<fileserver-url>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>201 Created</title>
</head><body>
<h1>Created</h1>
<p>Resource /guicustomization.jar has been created.</p>
</body></html>
```

## Instanciate the ODM Helm Chart

Follow the explanation on how to deploy the ODM Helm Chart according to your [platform](https://github.com/DecisionsDev/odm-docker-kubernetes/tree/master/platform)

Add to your values.yaml the parameter allowing to download guicustomization.jar

```
decisionCenter:
  downloadUrl:
  - http://fileserver-apache.sample.svc.cluster.local:80/guicustomization.jar
```

Or if it's using the command line : **--set decisionCenter.downloadUrl={"http://fileserver-apache.sample.svc.cluster.local:80/guicustomization.jar"}**

## Add the custom setting property in Decision Center

To activate the Business value editor, after login in Decision Center as an administrator, go in the menu "Administration>Settings>Custom Settings"
Register a new setting named **decisioncenter.web.core.extensions.entrypoints** with the value **extensions/AddTabEntryPoint,extensions/AddButtonEntryPoint,extensions/AddEditorButtonEntryPoint**

## Test the sample

Load the [LoanValidationService.zip](./LoanValidationService.zip) Decision Service.
Follow [Running this sample](https://www.ibm.com/docs/en/odm/9.0.0?topic=customization-gui-sample-details#descriptiveTopic1297785707571__rssamples.uss_rs_smp_tsauthoring.1028561__title__1) details to understand how to use some custom widgets by drilling in the LoanValidationService Decision Service.


