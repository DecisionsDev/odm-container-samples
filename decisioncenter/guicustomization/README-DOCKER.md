
### Introduction

This readme explains how to run the GUI Customization sample in Docker.

Doing so, you do not need to have ODM installed. Instead we are relying on the 'ODM for developer' container image.

Before following the steps below, make sure you have built the images as explained in [README.md](README.md).

### Running the sample 

   Run ODM and Notifiers  docker images
   ```bash
   docker-compose -f compose-odm-dev.yml up odm-with-gui-customization
   ```

###  Using the Sample

Access Decision Center using the URL **http://localhost:9060**

Click on the link **Decision Center Business Console**

Log in with odmAdmin/odmAdmin

To activate the Custom Value Editor, after login in Decision Center as an administrator :
- Go in the menu **Administration>Settings>Custom Settings**
- Register a new setting named **decisioncenter.web.core.extensions.entrypoints** keeping blank the **default value of the setting** field.

![Custom Settings](images/custom_settings_1.png)

- Set the value of **decisioncenter.web.core.extensions.entrypoints** to **extensions/AddTabEntryPoint,extensions/AddButtonEntryPoint,extensions/AddEditorButtonEntryPoint**

![Custom Settings](images/custom_settings_2.png)

Follow [Running this sample](https://www.ibm.com/docs/en/odm/9.0.0?topic=customization-gui-sample-details#descriptiveTopic1297785707571__rssamples.uss_rs_smp_tsauthoring.1028561__title__1) details to understand how to use some custom widgets by drilling in the LoanValidationService Decision Service.

![Business Console Custom GUI](images/custom_gui.png)

### Stopping the Sample

```bash
docker-compose -f compose-odm-dev.yml down
```



