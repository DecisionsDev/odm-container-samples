# Introduction

This page explains how to deploy ODM in Kubernetes.

## Deploying ODM

#### a. Retrieve your entitled registry key

- Log in to [My IBM Container Software Library](https://myibm.ibm.com/products-services/containerlibrary) with the IBMid and password that are associated with the entitled software.

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

#### c. Add the public IBM Helm charts repository

```bash
helm repo add ibmcharts https://raw.githubusercontent.com/IBM/charts/master/repo/ibm-helm
helm repo update
````

Check that you can access the ODM charts:
```bash
helm search repo ibm-odm-prod
```
```bash
NAME                        	CHART VERSION	APP VERSION	DESCRIPTION
ibmcharts/ibm-odm-prod      	<version>     <version>  	IBM Operational Decision Manager  License By in...
```

### d. Create the `values.yaml` parameter file

Create a file named `values.yaml`. This file will be used by the `helm install` command to specify the configuration parameters. 

Find the parameters suitable to your platform in [link](https://github.com/DecisionsDev/odm-docker-kubernetes/tree/master/platform).

If you are on Openshift you can use this [values.yaml](values.yaml).

### e. Install an ODM release
```bash
helm install myodmsample ibmcharts/ibm-odm-prod -f values.yaml
```
