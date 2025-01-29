# Customizable Users/Groups Synchronization Sample

## Introduction

This sample demonstrates how to synchronize users and groups in the Decision Center Business Console administration tab with an external enterprise repository that can be an LDAP, an OpenId User registry, or any kind of backend or business logic.
The users and groups that are imported in the Business Console allow to manage fine grained permission.
Basically, when enabling the project security, a decision service will be only visible from users that belong to a specific group.
So, this sample allows to show how to synchronize in Decision Center some enterprise organizational changes like:
* a user is leaving the company, so is removed from groups and cannot access anymore projects
* a user is coming in the company and is affected to some groups to work on specific projects
* a user is changing of organization/groups and should change of project access    

## Scenario

In this sample, we will use 2 users rtsUser1 and rtsUser2 belonging respectively to 2 enteprise services LoanService1 and LoanService2.
This organization is stored inside a user registry backend that can be an LDAP, Entra ID, or Amazon Cognito user pool, ...
It is refelected inside an XML file named group-security-configurations.xml that can be consumed by Decision Center to manage the access permission on Decision Services.
So, we will enforce project security to provide access to:
* the **Loan Validation Service** project only to users belonging to the LoanService1 group, meaning **rtsUser1** 
* the **Miniloan Service** project only to users belonging to the LoanService2 group, meaning **rtsUser2**

![Scenario 1](images/scenario1.png)

Then, some organizational changes happen like rtsUser1 moving from LoanService1 to LoanService2, and rtsUser2 removed from LoanService2.
These changes will be reflected to a new-group-security-configurations.xml file that will be consumed by Decision Center.
Then we will see that access to project has changed for users.

![Scenario 2](images/scenario2.png)

## Running this sample in Decision Center

### 1) Prerequisites

Before you begin, ensure you have at least Docker 24.0.x (and optionally Kubernetes 1.27+).

### 2) Instructions to use the sample in Decision Center

Click one of the links below:
   * In [Kubernetes](README-KUBERNETES.md).
   * In [Docker](README-DOCKER.md). 
