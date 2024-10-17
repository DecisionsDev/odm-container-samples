
### Introduction

This readme explains how to run the GUI Customization sample in Docker.

Doing so, you do not need to have ODM installed. Instead we are relying on the 'ODM for developer' container image.

Before following the steps below, make sure you have built the images as explained in [README.md](README.md).

### Running the sample 

   Run ODM and Notifiers  docker images
   ```bash
   docker-compose -f compose-odm-dev.yml up
   ```

###  Using the Sample

To activate the GUI customization, after login in Decision Center as an administrator, go in the menu "Administration>Settings>Custom Settings"
Register a new setting named **decisioncenter.web.core.extensions.entrypoints** with the value **extensions/AddTabEntryPoint,extensions/AddButtonEntryPoint,extensions/AddEditorButtonEntryPoint**

![Custom Settings](images/custom_settings.png)

Follow [Running this sample](https://www.ibm.com/docs/en/odm/9.0.0?topic=customization-gui-sample-details#descriptiveTopic1297785707571__rssamples.uss_rs_smp_tsauthoring.1028561__title__1) details to understand how to use some custom widgets by drilling in the LoanValidationService Decision Service.

![Business Console Custom GUI](images/custom_gui.png)

### Stopping the Sample

```bash
docker-compose -f compose-odm-dev.yml down
```



