
# Webhook Notification Setup Sample
### Introduction
This sample demonstrates how to use webhooks within the IBM Operational Decision Manager (ODM) to send notifications or data to external systems whenever the following events occur:
- When actions that are related to the decision governance framework happen in Decision Center, for example when a user creates a release, or approves an activity.
- When a deployment ends, you are notified of its status: completed, failed, or aborted.
- When a user creates or updates a rule or a decision table.

Webhooks allow for real-time data sharing and integration, making it easier to trigger actions or workflows in other applications.

In this sample, you will learn how to configure and use the Webhook Notifier to send notifications:
- either to a Slack channel,
- or to a Node.js server which records the notifications into log files.

## Prerequisites

Before you begin, ensure you have the following:

- **Container Platform**: Docker 19.03 or Kubernetes 1.19+.

## Setup Instructions

### 1. Build the samples

This NodeJS sample code will be packaged in two docker images.

   1. Navigate to the Slack webhook sample directory:
        ```bash
        cd decisioncenter/webhooknotifier
        ```

   2. Edit the `endPoints.json` file to set the URL of your Slack channel and the token to use for authentication. You can skip this step if you do not want to use Slack and only want to use the example based on notifications saved into files.
   3. Build the docker images
        ```bash
        docker-compose build
        ```

### Run the sample

   * [In Kubernetes](README-KUBERNETES.md). (You need ODM installed in Kubernetes)
   * [In Docker](README-DOCKER.md). 

