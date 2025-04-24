# Introduction

This README explains how to run the BOM dynamic domain sample in Kubernetes.

#  Configuring the sample in Kubernetes
## 1. Build the sample code 

The instructions below enable to build the JAR using a Docker container featuring Maven and a JDK version 21. 

Run the command below in the `decisioncenter/dynamicdomain/src/ilog.rules.studio.samples.bomdomainpopulate` directory:

This will compile the source code 
```bash
docker run --rm \
      -v "$(pwd)":/usr/src/sample \
      -w /usr/src/sample \
      maven:3.9.9-ibm-semeru-21-jammy \
      mvn clean install
``` 
> **Result**: The generated JAR file will be located in the `target` directory with the name `bomdomainpopulate-1.0.jar`.

## 2. Uploading JARs on a file server

Any file server reachable by Decision Center is suitable. 

You can either use an existing one or follow the instructions [here](https://github.com/DecisionsDev/odm-docker-kubernetes/blob/vnext-release/contrib/file-server/README.md#setup-an-httpd-file-server) to deploy a httpd file server in a new pod.

Two JARs must be made available:
  1. the Decision Center extension JAR built [previously](README-KUBERNETES.md#1-build-the-sample-code)
  1. the JDBC driver of the database storing the data of the dynamic domains.

Download the JDBC driver (if the internal PostgreSQL database is used):
```
curl -kLo jdbc-driver.jar https://jdbc.postgresql.org/download/postgresql-42.5.6.jar
```

Upload the two JARs on the file server (the files must be named `bomdomainpopulate-1.0.jar` and `jdbc-driver.jar` because Decision Center will be configured to download files named that way):
```
curl -T target/bomdomainpopulate-1.0.jar $FILESERVER_URL
curl -T jdbc-driver.jar $FILESERVER_URL
```

If all goes well, you should have an output with `201` status for the uploading of the JARs. For example:
```console
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>201 Created</title>
</head><body>
<h1>Created</h1>
<p>Resource /bomdomainpopulate-1.0.jar has been created.</p>
</body></html>
```

## 3. Deploying ODM

To get access to the ODM material, you must have an IBM entitlement key to pull the images from the IBM Cloud Container registry.

### a. Retrieve your entitled registry key

- Log in to [MyIBM Container Software Library](https://myibm.ibm.com/products-services/containerlibrary) with the IBMid and password that are associated with the entitled software.

- In the **Container Software and Entitlement Keys** tile, verify your entitlement on the **View library page**, and then go to *Entitlement keys* to retrieve the key.

### b. Create a pull secret by running the kubectl create secret command

```bash
kubectl create secret docker-registry my-odm-docker-registry --docker-server=cp.icr.io \
    --docker-username=cp --docker-password="<ENTITLEMENT_KEY>" --docker-email=<USER_EMAIL>
```
Where:

- `<ENTITLEMENT_KEY>`: The entitlement key from the previous step. Make sure to enclose the key in double quotes.
- `<USER_EMAIL>`: The email address associated with your IBMid.

> **Note**
> The `cp.icr.io` value for the docker-server parameter is the only registry domain name that contains the images. You must set the docker-username to `cp` to use the entitlement key as the docker-password.

The `my-odm-docker-registry` secret name must be used for the `image.pullSecrets` parameter when you run a Helm install of your containers. The `image.repository` parameter should also set to `cp.icr.io/cp/cp4a/odm`.

### c. Add the public IBM Helm charts repository:

```bash
helm repo add ibm-helm https://raw.githubusercontent.com/IBM/charts/master/repo/ibm-helm
helm repo update
```

### d. Check that you can access the ODM charts:

```bash
helm search repo ibm-odm-prod
```
```bash
NAME                          CHART VERSION APP VERSION	DESCRIPTION
ibm-helm/ibm-odm-prod         25.0.0        9.5.0.0  	IBM Operational Decision Manager  License By in...
```

Create a file named `values.yaml`. This file will be used by the `helm install` command to specify the configuration parameters. 

Add the lines below in `values.yaml` to let Decision Center download the customization JARs (replace `<FILESERVER_URL>` by the actual URL of the file server hosting the JARs):
```yaml
decisionCenter:
  downloadUrl:
    - <FILESERVER_URL>/bomdomainpopulate-1.0.jar
    - <FILESERVER_URL>/jdbc-driver.jar
```

Add the lines below in `values.yaml` if you want to deploy ODM only for the purpose of testing the sample (in that case it is straightforward to use the PostgreSQL internal database and persist the data in ephemeral storage): 
```yaml
internalDatabase:
  persistence:
    enabled: false
    useDynamicProvisioning: false
  populateSampleData: true
  runAsUser: ''
```

Add all the other parameters suitable to your platform in `values.yaml`. Check this [link](https://github.com/DecisionsDev/odm-docker-kubernetes/tree/master/platform) for help.

If you are on Openshift, you can use this [values.yaml](./src/ilog.rules.studio.samples.bomdomainpopulate/values.yaml) file by replacing `<FILESERVER_URL>` with the actual URL of the file server hosting the JARs.

Install an ODM release named `myodmsample`: 
```bash
helm install myodmsample ibm-helm/ibm-odm-prod -f values.yaml
```
> **NOTE**: If you choose a **different** release name, you must edit the value of `database.url` parameter accordingly in the [database.properties](src/ilog.rules.studio.samples.bomdomainpopulate/src/main/resources/data/database.properties), [rebuild](README-KUBERNETES.md#1-build-the-sample-code) the `bomdomainpopulate-1.0.jar`, and upload the JAR to the file server again.

## 4. Configuring Decision Center

The class that implements the customization must be declared:
- either using a custom setting at Business Console
- *or* using a JVM parameter

### 4.1 Using a custom setting
1. Log in to the Business Console. Use the following credentials if you install with the [values.yaml](./src/ilog.rules.studio.samples.bomdomainpopulate/values.yaml) for OCP:  
   - **Username**: `odmAdmin`  
   - **Password**: `odmAdmin`
2. Navigate to **Administration > Settings > Custom Settings**
3. Click the *Add custom setting* **icon** and set:
    - name = `teamserver.derbyDataBaseDomainProvider`
    - description = `derbyDataBaseDomainProvider`
    - type = `String`
    - leave `default value` empty. Save the setting.
4. Set the value of this new custom setting to `ilog.rules.studio.samples.bomdomainpopulate.DataBaseDomainValueProvider`.

### 4.2 Using a JVM parameter

Follow instructions as described [here](https://www.ibm.com/docs/en/odm/9.0.0?topic=kubernetes-persisting-decision-center-ruleset-cache) to create a Config Map with the additional JVM parameter below: 
```
-Dilog.rules.teamserver.derbyDataBaseDomainProvider=ilog.rules.studio.samples.bomdomainpopulate.DataBaseDomainValueProvider
```
You need to set this new Config Map using the Helm parameter **decisionCenter.jvmOptionsRef**. To do so, apply an upgrade to your helm release:
```bash
helm upgrade myodmsample ibm-helm/ibm-odm-prod --reuse-values --set decisionCenter.jvmOptionsRef=<your_updated_dc_jvm_options_ref_configmap>
```

## 5. Initializing the database

1. If the data of the dynamic domains are stored in the internal PostgreSQL database, you can find the name of that pod by running:
    ```bash
    DBSERVER=$(kubectl get pods --no-headers -o custom-columns=":metadata.name" |grep dbserver)
    echo $DBSERVER
    ```

2. Navigate to the BOM dynamic domain sample directory:
    ```bash
    cd decisioncenter/dynamicdomain
    ```

3. Copy the initialization SQL script into the pod and execute it:
    ```bash
    kubectl cp ./sql_scripts/createAndPopulate.sql $DBSERVER:/tmp
    kubectl exec $DBSERVER -- psql -U odmusr -d odmdb -f /tmp/createAndPopulate.sql
    ```

## 6. Using the sample

1. Log in to the Business Console.
2. Navigate to the **Library** tab.
3. Import the rule project archive `dynamicdomain/projects/bomdomainpopulate-rules.zip`.
    > Note: this rule project `bomdomainpopulate-rules` is only aimed at editing rules to demonstrate loading domains from a database. It is missing a deployment configuration and cannot be executed.
4. Display the rule `CheckOrder > OrderType`. An error **Value (string) 'CompanyX' is incorrect** is displayed. Edit the rule and either remove **"CompanyX"** and press SPACE or double-click **"CompanyX"**. A list of suitable companies gets displayed in a drop-down. Close down the rule **without** saving.
5. Display the rule `CheckCurrency > CurrencyRestriction`. No warning is displayed.
6. Let's now make some changes in the dynamic domains in the database. Run:
    ```bash
    kubectl cp ./sql_scripts/modifyTables.sql $DBSERVER:/tmp
    kubectl exec $DBSERVER -- psql -U odmusr -d odmdb -f /tmp/modifyTables.sql
    ```

7. Display the rule `CheckOrder > OrderType` back again. Notice that there is no error anymore. The effects of the changes (the addition of `CompanyX` and `CompanyY`) done in the database are taken into account automatically because the values that the field `stock` can take are dynamically fetched from the database (and not stored in the BOM).
8. Conversely if you display the rule `CheckCurrency > CurrencyRestriction`, there is still no warning. So let's now import the changes done in the database into the BOM. Click the **Model** tab, and then the **Dynamic Domains** sub-tab. Expand all the three domains. You should see this: (Notice that the **Australian Dollar** was removed)

    ![Dynamic Domains update](images/dynamicDomainsUpdate.png)

9. Tick **Domain** to select all the domains, and click the **Apply changes** button. Confirm the change.
10. Display the rule `CheckCurrency > CurrencyRestriction` back again. Now a warning `'Australian Dollar' is deprecated` is displayed as the result of the update of the Dynamic Domains in the BOM.
