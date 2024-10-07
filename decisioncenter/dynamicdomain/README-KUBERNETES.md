# Introduction

This README explains how to run the BOM dynamic domain sample in Kubernetes.

#  Configuring the sample in Kubernetes

## 1. Uploading JARs on a file server

The customization JARs can be made available to Decision Center in two ways:
1. the legacy way: by copying the JARs to a PVC and referencing this PVC using the parameter `decisionCenter.customlibPvc`
1. the new way (since 9.0 only): by copying the JARs on a fileserver and referencing the files to download from this fileserver using the parameter `decisionCenter.downloadUrl`

This document explains how to use the new way. If you intend to deploy an older version of ODM, you must use the legacy way instead. Here is an [page in the documentation](https://www.ibm.com/docs/en/odm/9.0.0?topic=kubernetes-customizing-decision-center-business-console) that can help.

Two JARs must be made available:
- the Decision Center extension JAR [built as explained previously](README.md#building-the-decision-center-extension-jar),
- the JDBC driver of the database storing the data of the dynamic domains.

Any file server reachable by Decision Center is suitable. You can either use an existing one or follow the instructions [here](../README.md#setup-an-httpd-file-server) to deploy a httpd file server in a new pod.

The JDBC driver can be downloaded as explained below if the internal PostgreSQL database is used:
```
curl -kLo jdbc-driver.jar https://jdbc.postgresql.org/download/postgresql-42.5.6.jar
```

The two JARs can be uploaded as follows (the files must be named `bomdomainpopulate-1.0.jar` and `jdbc-driver.jar` because Decision Center will be configured to download files named that way):
```
curl -T target/bomdomainpopulate-1.0.jar $FILESERVER_URL
curl -T jdbc-driver.jar $FILESERVER_URL
```

## 2. Deploying ODM

Add the public IBM Helm charts repository:
```bash
helm repo add ibmcharts https://raw.githubusercontent.com/IBM/charts/master/repo/ibm-helm
helm repo update
````

Check that you can access the ODM charts:
```bash
helm search repo ibm-odm-prod
NAME                        	CHART VERSION	APP VERSION	DESCRIPTION
ibmcharts/ibm-odm-prod      	<version>     <version>  	IBM Operational Decision Manager  License By in...
```

Create a file named `values.yaml`. This file will be used by the `helm install` command to specify the configuration parameters. 

Add the lines below in `values.yaml` to let Decision Center download the customization JARs (replace `<FILESERVER_URL>` by the actual URL of the file server hosting the JARs):
```yaml
decisionCenter:
  downloadUrl:
    - <FILESERVER_URL>/bomdomainpopulate-1.0.jar
    - <FILESERVER_URL>/jdbc-driver.jar
```

>Note: if you deployed a httpd fileserver following the instructions [here](../README.md#setup-an-httpd-file-server), then the internal URL of the fileserver is <!-- markdown-link-check-disable -->http://fileserver-apache.NAMESPACE.svc.cluster.local:80<!-- markdown-link-check-enable --> (where NAMESPACE is the project in which the fileserver was deployed).

Add the lines below in `values.yaml` if you want to deploy ODM only for the purpose of testing the sample (in taht case it is straightforward to use the PostgreSQL internal database and persist the data in ephemeral storage): 
```yaml
internalDatabase:
  persistence:
    enabled: false
    useDynamicProvisioning: false
  populateSampleData: true
  runAsUser: ''
```

Add all the other parameters suitable to your platform in `values.yaml`. Check this [link](https://github.com/DecisionsDev/odm-docker-kubernetes/tree/master/platform) for help.

Install an ODM release named `myodmsample` (if you choose a different release name, you need to edit the file [database.properties](src/ilog.rules.studio.samples.bomdomainpopulate/src/main/resources/database.properties)):
```bash
helm install myodmsample ibmcharts/ibm-odm-prod -f values.yaml
```

## 3. Configuring Decision Center

1. Log in into the Business Console as an admin
1. Navigate to Administration > Settings > Custom Settings
1. Click the "Add custom setting" icon and set:
    - name = `teamserver.derbyDataBaseDomainProvider`
    - description = `derbyDataBaseDomainProvider`
    - type = `String`
    - leave `default value` empty
1. Set the value of the newly created custom setting to `ilog.rules.studio.samples.bomdomainpopulate.DataBaseDomainValueProvider`

## 4. Initializing the database

If the data of the dynamic domains are stored in the internal PostgreSQL database, you can find the name of that pod by running:
```bash
DBSERVER=$(kubectl get pods -o name --no-headers | grep dbserver)
echo $DBSERVER
```

Navigate to the BOM dynamic domain sample directory:
```bash
cd decisioncenter/dynamicdomain
```

Copy the initialization SQL script into the pod and execute it:
```bash
kubectl cp ${pwd}/sql_scripts/createAndPopulate.sql $DBSERVER:/tmp
kubectl cp ${pwd}/sql_scripts/modifyTables.sql $DBSERVER:/tmp
kubectl exec $DBSERVER -- psql -U odmusr -d odmdb -f /tmp/createAndPopulate.sql
kubectl exec $DBSERVER -- psql -U odmusr -d odmdb -f /tmp/modifyTables.sql
```

## 5. Using the Sample

1. Log in into the Business Console.
1. Navigate to the **Library** tab.
1. Import the rule project archive `rule_project/bomdomainpopulate-rules.zip`.
    > Note: this rule project `bomdomainpopulate-rules` is only aimed at editing rules to demonstrate loading domains from a database. It is missing a deployment configuration and cannot be executed.
1. Display the rule `CheckCurrency > CurrencyRestriction`. No error is displayed.
1. Click the **Model** tab, and then the **Dynamic Domains** sub-tab. Expand the 3 domains. You should see this:

![Dynamic Domains update](images/dynamicDomainsUpdate.png)

1. Select all three domains, and click the **Apply changes** button and confirm the change.
1. Display the rule `CheckCurrency > CurrencyRestriction` back again. Now a warning `'Australian Dollar' is deprecated` is displayed as result of the Dynamic Domains update.
1. Edit the rule, remove `Australian Dollar` and press SPACE. You will see all the entries of the `CurrencyType` dynamic domain in a drop-down, including the new ones. 
