# Introduction

This README explains how to run the GUI Customization sample in Kubernetes.
Before following the steps below, make sure you have built the images as explained in [README.md](README.md).

#  Configuring the sample in Kubernetes

## 1. Uploading JARs on a file server

Any file server reachable by Decision Center is suitable.

You can either use an existing one or follow the instructions [here](https://github.com/DecisionsDev/odm-docker-kubernetes/blob/vnext-release/contrib/file-server/README.md#setup-an-httpd-file-server) to deploy a httpd file server in a new pod.

Upload the **guicustomization-1.0.jar** file on the file server :
```
curl -T guicustomization/guicustomization-source/target/guicustomization-1.0.jar $FILESERVER_URL
```

## 2. Deploying ODM

To get access to the ODM material, you must have an IBM entitlement key to pull the images from the IBM Cloud Container registry.

This is what will be used in the next step of this tutorial.

#### a. Retrieve your entitled registry key

- Log in to [MyIBM Container Software Library](https://myibm.ibm.com/products-services/containerlibrary) with the IBMid and password that are associated with the entitled software.

- In the **Container Software and Entitlement Keys** tile, verify your entitlement on the **View library page**, and then go to *Entitlement keys* to retrieve the key.

#### b. Create a pull secret by running the kubectl create secret command

```bash
oc create secret docker-registry my-odm-docker-registry --docker-server=cp.icr.io \
    --docker-username=cp --docker-password="<ENTITLEMENT_KEY>" --docker-email=<USER_EMAIL>
```

Where:

- `<ENTITLEMENT_KEY>`: The entitlement key from the previous step. Make sure to enclose the key in double quotes.
- `<USER_EMAIL>`: The email address associated with your IBMid.

> **Note**
> The `cp.icr.io` value for the docker-server parameter is the only registry domain name that contains the images. You must set the docker-username to `cp` to use the entitlement key as the docker-password.

The my-odm-docker-registry secret name is already used for the `image.pullSecrets` parameter when you run a Helm install of your containers. The `image.repository` parameter is also set by default to `cp.icr.io/cp/cp4a/odm`.

#### c. Add the public IBM Helm charts repository

```bash
helm repo add ibm-helm https://raw.githubusercontent.com/IBM/charts/master/repo/ibm-helm
helm repo update
```

#### d. Check your access to the ODM chart

```bash
$ helm search repo ibm-odm-prod
NAME                    CHART VERSION APP VERSION DESCRIPTION
ibm-helm/ibm-odm-prod   24.0.0        9.0.0.0     IBM Operational Decision Manager
```

#### e. Create a secret to manage custom authentication/authorization

Create a secret to manage custom authentication/authorization using the [webSecurity.xml](./guicustomization-source/webSecurity.xml) and [group-security-configurations.xml](./guicustomization-source/group-security-configurations.xml) files :

```
kubectl create secret generic my-custom-auth-secret --from-file=webSecurity.xml --from-file=group-security-configurations.xml
```
#### f. Install an IBM Operational Decision Manager release

Create a file named `values.yaml`. This file will be used by the `helm install` command to specify the configuration parameters.

Add the lines below in `values.yaml` to let Decision Center download the customization JARs (replace `<FILESERVER_URL>` by the actual URL of the file server hosting the JARs):
```
customization:
  authSecretRef: my-custom-auth-secret
decisionCenter:
  downloadUrl:
    - <FILESERVER_URL>/guicustomization-1.0.jar
```

Add all the other parameters suitable to your platform in `values.yaml`. Check this [link](https://github.com/DecisionsDev/odm-docker-kubernetes/tree/master/platform) for help.

If you are on OCP, you can use this [values.yaml](./guicustomization-source/values.yaml) file by replacing `<FILESERVER_URL>` by the actual URL of the file server hosting the JARs.

```bash
helm install guicustomization-sample ibmcharts/ibm-odm-prod -f values.yaml
```

#  Using the Sample

**Log in** to the Business Console at **https://<HOST:PORT>/decisioncenter** using the credentials:  
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

#### To see the customization for a non-administrative user:

Log in to the Business console by using **Bea** as the username and password.

Open the **Library** tab. Click the **Loan Validation Service** box anywhere but the name, and then select the main branch.

Click the new button **My Button**. A dialog displays some metrics on the decision service. The dialog content is different from the content that is provided for the administrator.

Close the dialog and then click the new tab **My Tab**. The tab displays the same metrics on the decision service. The tab content is different from the content that is provided for the administrator.

Log out of the Business console.

Follow [Running this sample](https://www.ibm.com/docs/en/odm/9.0.0?topic=customization-gui-sample-details#descriptiveTopic1297785707571__rssamples.uss_rs_smp_tsauthoring.1028561__title__1) details to understand how to use some custom widgets by drilling in the LoanValidationService Decision Service.

![Business Console Custom GUI](images/custom_gui.png)
