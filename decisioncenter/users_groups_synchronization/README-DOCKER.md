
### Introduction

This readme explains how to run the Customizable Users/Groups Synchronization sample in Docker.

Doing so, you do not need to have ODM installed. Instead we are relying on the 'ODM for developer' container image.

### Running the sample 

   Run ODM docker images
   ```bash
   docker-compose -f compose-odm-dev.yml up odm &
   ```

###  Using the Sample

**Log in** to the Business Console at [http://localhost:9060/decisioncenter](http://localhost:9060/decisioncenter) using the credentials:  
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
   - **Password**: `rtsUser1`
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
   - **Password**: `rtsUser2`
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

- Synchronize this organization evolution with the Decision Center Business Console  by pushing the new file using the Decision Center rest-api:

```bash
curl -X 'POST' 'http://localhost:9060/decisioncenter-api/v1/repository/users-roles-registry?eraseAllUsersAndGroups=true' -H 'accept: */*'   -H 'Content-Type: multipart/form-data'   -F 'file=@./new-group-security-configurations.xml;type=text/xml' -u odmAdmin:odmAdmin 
```

Check that new access is correctly set for the **rtsUser1** user:
- Click on **rtsUser2** at the top right corner
- Click on **Log out** link
- **Log in** to the Business Console using the credentials:
   - **Username**: `rtsUser1`
   - **Password**: `rtsUser1`
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
   - **Password**: `rtsUser2`
- Click on the **LIBRARY** tab
- Check there is no accessible decision service 
- Click on **rtsUser2** at the top right corner
- Click on the **Profile** link
- Check that **Groups** is well displaying **rtsUsers**

![rtsUser2 Access](images/rtsUser2_2.png)

### Stopping the Sample

```bash
docker-compose -f compose-odm-dev.yml down
```



