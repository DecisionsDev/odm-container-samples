# Introduction

This README explains how to run the Custom Value Editor sample in Kubernetes.
Before following the steps below, make sure you have built the images as explained in [README.md](README.md).

#  Configuring the sample in Kubernetes

## 1. Deploying ODM

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

#### e. Create the synchronization secret

We create a secret that will be used inside the Decision Center sidecar container to synchronize users and groups.

```shell
kubectl create secret generic users-groups-synchro-secret \
        --from-file=sidecar-start.sh \
        --from-file=group-security-configurations.xml
```

#### f. Install an IBM Operational Decision Manager release

Create a file named **values.yaml**. This file will be used by the **helm install** command to specify the configuration parameters. 

Add the lines below in **values.yaml** to create the Decision Center sidecar and populate the sample projects:
```
decisionCenter:
  sidecar:
    enabled: true
    confSecretRef: users-groups-synchro-secret
internalDatabase:
  populateSampleData: true
```

Add all the other parameters suitable to your platform in `values.yaml`. Check this [link](https://github.com/DecisionsDev/odm-docker-kubernetes/tree/master/platform) for help.

If you are on OCP, you can use this [values.yaml](./users_groups_synchronization-source/values.yaml).

```bash
helm install synchro-sample ibmcharts/ibm-odm-prod -f values.yaml
```

#  Using the Sample

**Log in** to the Business Console at **http://<HOST:PORT>/decisioncenter** using the credentials:
   - **Username**: `odmAdmin`  
   - **Password**: `odmAdmin`

To check the custom Users and Groups are corrected imported, after login in Decision Center as an administrator:
- Go in the menu **Administration>Groups**
- Check the presence of LoanService1 and LoanService2 groups

![Groups](images/groups.png)

- Go in the menu **Administration>Users**
- Check the user rtsUser1 is belonging to the LoanService1 group and the user rtsUser2 to the LoanService2 group.

![Users](images/users.png)

We will enable the security by restricting the access to the **Loan Validation Service** project only to users that belong to the **LoanService1** group (like rtsUser1)
and restricting the access to the **Miniloan Service** project only to users that belong to the **LoanService2** group (like rtsUser2):
- Go in the menu **Administration>Project Security**
- Click on the **Edit decision service security** button of the **Loan Validation Service** project
- Click on the **Security is not enforced for this decision service** link
- Choose **Enforce security**
- Select the **LoanService1** group
- Click on the **Done** button
- Click on the **Edit decision service security** button of the **Miniloan Service** project
- Click on the **Security is not enforced for this decision service** link
- Choose **Enforce security**
- Select the **LoanService2** group
- Click on the **Done** button

![Project Security](images/project_security.png)

Check that access is correctly set for the **rtsUser1** user:
- Click on **odmAdmin** at the top right corner
- Click on **Log out** link
- **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser1`
   - **Password**: `odmAdmin`
- Click on the **LIBRARY** tab
- Check that only the **Loan Validation Service** project is visible
- Click on **rtsUser1** at the top right corner
- Click on the **Profile** link
- Check that **Groups** is well displaying **LoanService1, rtsUsers**

![rtsUser1 Access](images/rtsUser1_1.png)

Check that access is correctly set for the **rtsUser2** user:
- Click on **rtsUser1** at the top right corner
- Click on **Log out** link
- **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser2`
   - **Password**: `odmAdmin`
- Click on the **LIBRARY** tab
- Check that only the **Miniloan Service** project is visible
- Click on **rtsUser2** at the top right corner
- Click on the **Profile** link
- Check that **Groups** is well displaying **LoanService2, rtsUsers**

![rtsUser2 Access](images/rtsUser2_1.png)

Now the **rtsUser1** user is moving in the company from the **LoanService1** service to the **LoanService2** service
and the **rtsUser2** user left the **LoanService2** service.

In the **new-group-security-configurations.xml** file you can see:

```bash
       <user name="rtsUser1" groups="LoanService2, rtsUsers"/>
       <user name="rtsUser2" groups="rtsUsers"/>
```

that is replacing the initial setting:

```bash
       <user name="rtsUser1" groups="LoanService1, rtsUsers"/>
       <user name="rtsUser2" groups="LoanService2, rtsUsers"/>
```

Synchronize this organization evolution with the Decision Center Business Console  by modifying the group-security-configurations.xml in the **users-groups-synchro-secret** secret:

```bash
NEW_FILE=$(base64 < "./new-group-security-configurations.xml" | tr -d '\n')
kubectl patch secret users-groups-synchro-secret -p "{\"data\":{\"group-security-configurations.xml\":\"${NEW_FILE}\"}}"
```

Wait the updated group-security-configurations.xml is consumed by the sidecar script:

```bash
DC_POD=$(kubectl get pods -o name | grep odm-decisioncenter)
kubectl logs $DC_POD -c sidecar -f
```

Check that new access is correctly set for the **rtsUser1** user:
- Click on **rtsUser2** at the top right corner
- Click on **Log out** link
- **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser1`
   - **Password**: `odmAdmin`
- Click on the **LIBRARY** tab
- Check that only the **Miniloan Service** project is visible
- Click on **rtsUser1** at the top right corner
- Click on the **Profile** link
- Check that **Groups** is well displaying **LoanService2, rtsUsers**

![rtsUser1 Access](images/rtsUser1_2.png)

Check that access is correctly set for the **rtsUser2** user:
- Click on **rtsUser1** at the top right corner
- Click on **Log out** link
- **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser2`
   - **Password**: `odmAdmin`
- Click on the **LIBRARY** tab
- Check there is no accessible decision service 
- Click on **rtsUser2** at the top right corner
- Click on the **Profile** link
- Check that **Groups** is well displaying **rtsUsers**

![rtsUser2 Access](images/rtsUser2_2.png)
