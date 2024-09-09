
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


### 2.1 Run this sample on Kubenetes
   1. Push the images to the Kubernetes registry
        ```bash
        docker login $REGISTRY_HOST -u <REGISTRY_USERNAME> -p <REGISTRY_PASSWORD>
        docker tag webhooknotifier-slack $REGISTRY_HOST/webhooknotifier-slack
        docker tag webhooknotifier-logfile $REGISTRY_HOST/samples/webhooknotifier-logfile
        docker push $REGISTRY_HOST/samples/webhooknotifier-logfile
        docker push $REGISTRY_HOST/webhooknotifier-slack
        ```

   2. Instanciate the pods 
        ```bash
        kubectl run webhooknotifier-logfile --image=image-registry.openshift-image-registry.svc:5000/samples/webhooknotifier-logfile:latest --port 3000 --expose --port 3000
        kubectl run webhooknotifier-slack --image=image-registry.openshift-image-registry.svc:5000/samples/webhooknotifier-slack:latest --port 3000 --expose --port 3000
        ```

   3. Declare the services
      1. The logs files notifier
       ```shell
        curl -k -X 'PUT' \
        'https://my-odm-release-odm-dc-route-samples.apps.odm-dev-ocp.cp.fyre.ibm.com/decisioncenter-api/v1/webhook/notify?url=http%3A%2F%2Fwebhooknotifier-logfile.samples.svc.cluster.local%3A3000%2Fprint' \
        -H 'accept: */*' \
        -H 'Content-Type: application/json' \
        -d 'null' \
        -u odmAdmin:odmAdmin -k 
       ```

       2. For the Slack Notifier:
        
        * Generate the Token : 
        ```shell
        kubectl exec -ti webhooknotifier-slack -- curl -X 'GET' \
        'http://localhost:3000/token.generate' \
        -H 'accept: */*' \
        -H 'Content-Type: application/json' 
        ```
        xxxxxx.xxxxxx.xxxxxxxx -> GENERATED_TOKEN
        
        This command return a token that should be injected in the next command line.

        * Register the webhook with the generated token
        ```shell
        curl -X 'PUT' -k \
       'https://<DC_URL>/decisioncenter-api/v1/webhook/notify?url=http%3A%2F%2Fwebhooknotifier-slack.<NAMESPACE>.svc.cluster.local%3A3000%2Fslack' \
       -H 'accept: */*' \
       -H 'Content-Type: application/json' \
       -d '<GENERATED_TOKEN>' \
       -u <USERNAME_PASSWORD>
        ```
        {"id":"57896361-62f8-4a0e-a86b-3da46417f493","url":"http://webhooknotifier-slack.svc.cluster.local:3000/slack","authToken":"*****************************************************"}%
        ```
        Where:
        * DC_URL : The URL where decision center is accessible.
        * NAMESPACE : The namespace where the sample has been deployed.
        * GENERATED_TOKEN : The token generated in the previous step.
        * USERNAME_PASSWORD : If you installed the demo or starter mode you can put odmAdmin:odmAdmin

### 2.2 Run this sample on Docker
We will use the ODM for developper image. No needs to have an ODM installed.

1.  Run ODM and Notifiers  docker images
    ```bash
    docker-compose  -f docker-compose.yaml -f compose-odm-dev.yml up
    ```

2. Register the webhook in Decision Center using the REST API tool, providing the token and URL (`http://localhost:3000/slack` and `http://localhost:3000/print`).

   1. For File Notifier:
   ```shell
    curl -X 'PUT' \
    'http://localhost:9060/decisioncenter-api/v1/webhook/notify?url=http%3A%2F%2Flogifle%3A3000%2Fprint' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d 'null' \
        -u odmAdmin:odmAdmin
    ```

   2. For the Slack Notifier:
    
    * Generate the Token : 
    ```shell
    kubectl exec -ti webhooknotifier-slack -- curl -X 'GET' \
    'http://localhost:3000/token.generate' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' 
    ```
     xxxxxx.xxxxxx.xxxxxxxx -> Token
    
     This command return a token that should be injected in the next command line.

    * Register the webhook with the generated token
    ```shell
    curl -X 'PUT' \
    'http://localhost:9060/decisioncenter-api/v1/webhook/notify?url=http%3A%2F%2Fslack%3A3000%2Fslack' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d '<GENERATED_TOKEN>' \
    -u odmAdmin:odmAdmin
    ```
## 3. Using the Sample

Once the webhook is set up, events from the Decision Center will trigger the notifications and either post messages to Slack or generate log files in the `results` directory. 

You can trigger an event by deploying a rule app for example.

You can see the notification in the docker-compose windows.

```bash
slack-1    | {
slack-1    |   "version": "1.0",
slack-1    |   "id": "cc66c9e8-9905-486d-99e9-7ab89af3d976",
slack-1    |   "author": "odmAdmin",
slack-1    |   "date": 1725633110628,
slack-1    |   "type": "SnapshotCreated",
slack-1    |   "content": [
slack-1    |     {
slack-1    |       "id": "dcf08c59-877c-42d4-9360-2189345577c8",
slack-1    |       "internalId": "dsm.DsDeploymentBsln:103:103",
slack-1    |       "name": "test_deployment_2024-09-06_16-31-45-855",
slack-1    |       "createdBy": "odmAdmin",
slack-1    |       "createdOn": 1725633110000,
slack-1    |       "lastchangedBy": "odmAdmin",
slack-1    |       "lastChangedOn": 1725633110000,
slack-1    |       "parentId": "1558f25b-daa6-4982-8b0b-48a388c7c202",
slack-1    |       "documentation": null,
slack-1    |       "buildMode": "DecisionEngine",
slack-1    |       "kind": "DeploymentSnapshot"
slack-1    |     }
slack-1    |   ],
slack-1    |   "details": [
slack-1    |     {
slack-1    |       "targetURL": "http://172.22.0.4:9060/decisioncenter/t/library#overviewsnapshot?id=dsm.DsDeploymentBsln%3A103%3A103&datasource=jdbc%2FilogDataSource&baselineId=dsm.DsDeploymentBsln%3A103%3A103"
slack-1    |     }
slack-1    |   ],
```

Log file structure:
- `deployments.txt`: Deployment details.
- `rules.txt`: Rule changes.
- `releases.txt`: Release details.
- `activities.txt`: Activity details.

## Stopping the Sample

```bash
docker-compose  -f docker-compose.yaml -f compose-odm-dev.yml down
```


## Source Files

You can explore and modify the source files under `/decisioncenter/samples/webhooknotifier/`, which includes:
- `slack-notifier.js`: Slack webhook implementation.
- `log-notifier.js`: File-based webhook implementation.
