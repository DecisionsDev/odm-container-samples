
# Webhook Notification Setup Sample
### Introduction
This sample demonstrates how to use webhooks within the IBM Operational Decision Manager (ODM) to send notifications or data to external systems whenever certain rules are executed. Webhooks allow for real-time data sharing and integration, making it easier to trigger actions or workflows in other applications based on decision outcomes.

In this sample, you'll learn how to configure and use the Webhook Notifier to send notifications to an external URL, providing seamless integration between IBM ODM and Slack or log files using a Node.js server.

## Prerequisites

Before you begin, ensure you have the following:

- **Container Platform**: Docker 19.03 or Kubernetes 1.19+.

## Setup Instructions

### 1. Build the samples

This NodeJS sample code will be packaged in 2 docker images.

   1. Navigate to the Slack webhook sample directory:
        ```bash
        cd decisioncenter/webhooknotifier
        ```

   2. Edit your Slack webhook URL and token in the `endPoints.json` file to be able to notify on slack. You can skip this step if you are don't want to use slack. Only file notifier will work will work.
   3. Build the docker images
        ```bash
        docker-compose  build
        ```

### Run the sample

   * [In Kubernetes](README-KUBERNETES.md). (You need ODM installed in Kubernetes)
   * [In Docker](README-DOCKER.md). 


