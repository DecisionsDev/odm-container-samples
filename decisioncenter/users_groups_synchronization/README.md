# Customizable Users/Groups Synchronization Sample

## Introduction

This sample demonstrates how to synchronize users and groups in the Decision Center Business Console administration tab with an external enterprise repository that can located in LDAP, an OpenId User registry, or any kind of backend or business logic.
The users and groups that are imported in the Business Console allow to manage fine grained permission.
Basically, when enabling the project security, a decision service will be only visible from users that belong to a specific group.
So, this sample allows to show how to synchronize in Decision Center some enterprise organizational changes like:
* a user is leaving the company, so is removed from groups and cannot access anymore projects
* a user is coming in the company and is affected to some groups to work on specific projects
* a user is changing of organization/groups and should change of project access    

## Running this sample in Decision Center

### 1) Prerequisites

Before you begin, ensure you have at least Docker 24.0.x (and optionally Kubernetes 1.27+).

### 2) Instructions to use the sample in Decision Center

Click one of the links below:
   * In [Kubernetes](README-KUBERNETES.md).
   * In [Docker](README-DOCKER.md). 
