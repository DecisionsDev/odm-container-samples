
## Introduction

This readme explains how to run the Customizable Users/Groups Synchronization sample in Docker.

Doing so, you do not need to have ODM installed. Instead we are relying on the [ODM for developer](https://hub.docker.com/r/ibmcom/odm/) container image.

## Prerequisites

- Docker setup in your machine
  
---

## Clone the sample repository

   Run ODM docker images
   ```bash
   git clone https://github.com/DecisionsDev/odm-container-samples.git
   cd decisioncenter/users_groups_synchronization/users_groups_synchronization-source
   ```

## Running the sample 

   Run ODM docker images
   ```bash
   docker-compose -f compose-odm-dev.yml up odm 
   ```

   Wait until ODM is started. 

---
##  Using the Sample

### 1. Log in to the Business Console

**Log in** to the Business Console at [http://localhost:9060/decisioncenter](http://localhost:9060/decisioncenter) using the credentials:  
   - **Username**: `odmAdmin`  
   - **Password**: `odmAdmin`


---
### 2. Verify Users and Groups Synchronization

#### Groups
To check the custom Users and Groups are corrected imported, after login in Decision Center as an administrator:
- Navigate to **Administration > Groups**.
- Check that `LoanService1` and `LoanService2` groups are present.
  
![Groups](images/groups.png)

#### Users

1. Navigate to **Administration > Users**.
2. Verify:
   - `rtsUser1` belongs to `LoanService1`.
   - `rtsUser2` belongs to `LoanService2`.

![Users](images/users.png)

### 3. Enable Security for Decision Services
In this section, you will configure access control for decision services in Decision Center Business Console. The goal is to restrict access to specific decision services based on user group membership, ensuring that only authorized users can view and modify certain projects.

We will enable the security by restricting the access to the **Loan Validation Service** project only to users that belong to the **LoanService1** group (like rtsUser1) and restricting the access to the **Miniloan Service** project only to users that belong to the **LoanService2** group (like rtsUser2):

| User      | Group        | Accessible Project         |
|-----------|-------------|---------------------------|
| rtsUser1  | LoanService1 | Loan Validation Service  |
| rtsUser2  | LoanService2 | Miniloan Service        |

#### Loan Validation Service

1. Navigate to **Administration > Project Security**.
2. Click **Edit decision service security** for **Loan Validation Service**.
3. Select **Enforce security** and choose `LoanService1`.
4. Click **Done**.

#### Miniloan Service

1. Navigate to **Administration > Project Security**.
1. Click on the **Edit decision service security** button of the **Miniloan Service** project
2. Click on the **Security is not enforced for this decision service** link
3. Choose **Enforce security**
4. Select the **LoanService2** group
5. Click **Done**
   
![Project Security](images/project_security.png)


### 4. Validate User Access

#### `rtsUser1`

1. Click on **odmAdmin** at the top right corner
2. Click on **Log out** link
3. **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser1`
   - **Password**: `odmAdmin`
4. Verify that only **Loan Validation Service** is visible under the **LIBRARY** tab.
5. Click on **rtsUser1** at the top right corner
6. Click on the **Profile** link
7. Check that **Groups** is well displaying **LoanService1, rtsUsers**

![rtsUser1 Access](images/rtsUser1_1.png)


#### `rtsUser2`

1. Click on **rtsUser1** at the top right corner
2. Click on **Log out** link
3. **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser2`
   - **Password**: `odmAdmin`
4. Verify that only **Miniloan Service** is visible under the **LIBRARY** tab.
5. Click on **rtsUser2** at the top right corner
6. Click on the **Profile** link
7. Check that **Groups** is well displaying **LoanService2, rtsUsers**
   

![rtsUser2 Access](images/rtsUser2_1.png)

--- 
## Updating User Group Assignments

As part of organizational changes, **rtsUser1** has transitioned from the **LoanService1** service to the **LoanService2** service, while **rtsUser2** is no longer part of the **LoanService2** service.

| User      | Group        | Accessible Project         |
|-----------|-------------|---------------------------|
| rtsUser1  | LoanService2 | Miniloan Service        |

### 1. Modify Group Assignments

Update `group-security-configurations.xml` to reflect organizational changes:

#### Before:

```xml
<user name="rtsUser1" groups="LoanService1, rtsUsers"/>
<user name="rtsUser2" groups="LoanService2, rtsUsers"/>
```

#### After:

```xml
<user name="rtsUser1" groups="LoanService2, rtsUsers"/>
<user name="rtsUser2" groups="rtsUsers"/>
```


### 2. Apply the Updated Configuration

To synchronize the evolution of this organization with the Decision Center Business Console, push the updated file using the Decision Center REST API:

```bash
curl -X 'POST' 'http://localhost:9060/decisioncenter-api/v1/repository/users-roles-registry?eraseAllUsersAndGroups=true' -H 'accept: */*'   -H 'Content-Type: multipart/form-data'   -F 'file=@./new-group-security-configurations.xml;type=text/xml' -u odmAdmin:odmAdmin
```

### 4. Validate New Access Rules

#### `rtsUser1`
Check that new access is correctly set for the **rtsUser1** user:
1. Click on **rtsUser2** at the top right corner
2. Click on **Log out** link
3. **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser1`
   - **Password**: `odmAdmin`
4. Click on the **LIBRARY** tab
5. Check that only the **Miniloan Service** project is visible
6. Click on **rtsUser1** at the top right corner
7. Click on the **Profile** link
8. Check that **Groups** is well displaying **LoanService2, rtsUsers**

![rtsUser1 Access](images/rtsUser1_2.png)

#### `rtsUser2`

Check that access is correctly set for the **rtsUser2** user:
1. Click on **rtsUser1** at the top right corner
2. Click on **Log out** link
3. **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser2`
   - **Password**: `odmAdmin`
4. Click on the **LIBRARY** tab
5. Check there is no accessible decision service 
6. Click on **rtsUser2** at the top right corner
7. Click on the **Profile** link
8. Check that **Groups** is well displaying **rtsUsers**

![rtsUser2 Access](images/rtsUser2_2.png)

--- 
### 3. Validate New Access Rules

#### `rtsUser1`
Check that new access is correctly set for the **rtsUser1** user:
1. Click on **rtsUser2** at the top right corner
2. Click on **Log out** link
3. **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser1`
   - **Password**: `odmAdmin`
4. Click on the **LIBRARY** tab
5. Check that only the **Miniloan Service** project is visible
6. Click on **rtsUser1** at the top right corner
7. Click on the **Profile** link
8. Check that **Groups** is well displaying **LoanService2, rtsUsers**

![rtsUser1 Access](images/rtsUser1_2.png)

#### `rtsUser2`

Check that access is correctly set for the **rtsUser2** user:
1. Click on **rtsUser1** at the top right corner
2. Click on **Log out** link
3. **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser2`
   - **Password**: `odmAdmin`
4. Click on the **LIBRARY** tab
5. Check there is no accessible decision service 
6. Click on **rtsUser2** at the top right corner
7. Click on the **Profile** link
8. Check that **Groups** is well displaying **rtsUsers**

![rtsUser2 Access](images/rtsUser2_2.png)

--- 

## Stopping the Sample

```bash
docker-compose -f compose-odm-dev.yml down
```



