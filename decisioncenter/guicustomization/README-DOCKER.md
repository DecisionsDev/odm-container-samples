
### Introduction

This readme explains how to run the GUI Customization sample in Docker.

Doing so, you do not need to have ODM installed. Instead we are relying on the 'ODM for developer' container image.

Before following the steps below, make sure you have built the images as explained in [README.md](README.md).

### Running the sample 

   Navigate to the source directory of the GUI Customization sample:
   ```
   cd decisioncenter/guicustomization/guicustomization-source
   ```
      
   Run ODM and Notifiers  docker images
   ```bash
   docker-compose -f compose-odm-dev.yml up odm-with-gui-customization &
   ```

###  Using the Sample

**Log in** to the Business Console at [http://localhost:9060/decisioncenter](http://localhost:9060/decisioncenter) using the credentials:  
   - **Username**: `Paul`  
   - **Password**: `Paul`

Load the [LoanValidationService.zip](./projects/LoanValidationService.zip) Decision Service.

To activate the Custom Value Editor, after login in Decision Center :
- Go in the menu **Administration>Settings>Custom Settings**
- Register a new setting named **decisioncenter.web.core.extensions.entrypoints** keeping blank the **default value of the setting** field.

![Custom Settings](images/custom_settings_1.png)

- Set the value of **decisioncenter.web.core.extensions.entrypoints** to **extensions/AddTabEntryPoint,extensions/AddButtonEntryPoint,extensions/AddEditorButtonEntryPoint**

![Custom Settings](images/custom_settings_2.png)

#### To see the customization for an administrator:

Log in to the Business console by using **Paul** as the username and password.

Open the **Library** tab. Click the **Loan Validation Service** box anywhere but the name, and select the **main** branch.

Click the new button **My Admin Button**. A dialog displays metrics on the decision service.

Click the new tab **My Admin Tab**. The tab displays the same metrics on the decision service.

Click the **Decision Artifacts** tab. Expand the **Loan Validation Scoring computation** package, and edit the rule **neverBankruptcy** (accept any default settings if prompted).

Click the button **My Info**. A dialog displays information on the rule.

Close the dialog and cancel the editing session. Click **main** in the breadcrumbs.

Click the **Decision Artifacts** tab and make sure that the operations are displayed. To display them, click **Types** and select **Operations**.

Expand the **Operations** folder under **Loan Validation Scoring** to edit the scoring operation.

Click the button **My Operation Info**. A dialog displays information on the operation.

Close the dialog, and cancel the editing session. Click **main** in the breadcrumbs.

Click the tab **Deployments** and edit the **test deployment configuration**.

Click the **Targets** tab and select the **Decision Service Execution server**. Save the test deployment configuration.

Click the name of the test deployment configuration.

Click the **Custom Deploy** button in the toolbar. A dialog shows the status of the deployment.

Close the dialog and log out of the Business console.

![Business Console Custom GUI Admin](images/custom_gui_admin.png)

#### To see the customization for a non-administrative user:

Log in to the Business console by using **Bea** as the username and password.

Open the **Library** tab. Click the **Loan Validation Service** box anywhere but the name, and then select the main branch.

Click the new button **My Button**. A dialog displays some metrics on the decision service. The dialog content is different from the content that is provided for the administrator.

Close the dialog and then click the new tab **My Tab**. The tab displays the same metrics on the decision service. The tab content is different from the content that is provided for the administrator.

Log out of the Business console.

![Business Console Custom GUI](images/custom_gui.png)

### Stopping the Sample

```bash
docker-compose -f compose-odm-dev.yml down
```



