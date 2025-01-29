# Customizable Users/Groups Synchronization Sample

## Introduction

This sample demonstrates how to synchronize users and groups in the Decision Center Business Console administration tab with an external enterprise repository. This repository can be an LDAP directory, an OpenID user registry, or any other backend or business logic system.

The imported users and groups in the Business Console enable fine-grained permission management. When project security is enabled, a decision service is only visible to users belonging to specific groups. 

This sample illustrates how to synchronize enterprise organizational changes within Decision Center, such as:
   * A user leaving the company, resulting in their removal from groups and loss of access to projects.
   * A new user joining the company and being assigned to groups to work on specific projects.
   * A user changing organizational groups, requiring updates to their project access.

## Scenario

In this sample, we use two users:

- `rtsUser1`, belonging to **LoanService1** group
- `rtsUser2`, belonging to **LoanService2** group
  
This organization is stored inside a **user registry backend**, such as LDAP, Entra ID, or Amazon Cognito..

The structure is reflected inside an XML configuration file: `group-security-configurations.xml`. 
Decision Center consumes this XML file to manage access permissions to **Decision Services**:

We will enforce project security as follows:

| User      | Group        | Accessible Project         |
|-----------|-------------|---------------------------|
| rtsUser1  | LoanService1 | Loan Validation Service  |
| rtsUser2  | LoanService2 | Miniloan Service        |


![Scenario 1](images/scenario1.png)

Organizational changes occur as follows:

- `rtsUser1` moves from `LoanService1` to `LoanService2`.
- `rtsUser2` is removed from `LoanService2`.

These updates are reflected in a new XML configuration file, `new-group-security-configurations.xml`, which is consumed by Decision Center. As a result, project access is updated accordingly:

- `rtsUser1` now has access to the **Miniloan Service** project instead of the **Loan Validation Service** project.
- `rtsUser2` loses access to all projects.

| User      | Group        | Accessible Project         |
|-----------|-------------|---------------------------|
| rtsUser1  | LoanService2 | Miniloan Service        |

This sample demonstrates how Decision Center dynamically adapts to organizational changes, ensuring accurate and up-to-date access control.

![Scenario 2](images/scenario2.png)

## Running this sample in Decision Center

### 1) Prerequisites

Before you begin, ensure you have at least Docker 24.0.x (and optionally Kubernetes 1.27+).

### 2) Instructions to use the sample in Decision Center

Click one of the links below:
   * In [Kubernetes](README-KUBERNETES.md).
   * In [Docker](README-DOCKER.md). 
