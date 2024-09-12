
### Introduction

This readme explains how to run the webhook sample in Docker.

Doing so, you do not need to have ODM installed. Instead we are relying on the 'ODM for developer' container image.

Before following the steps below, make sure you have built the images as explained in [README.md](README.md).

### Running the sample 

1.  Run ODM and Notifiers  docker images
    ```bash
    docker-compose  -f docker-compose.yaml -f compose-odm-dev.yml up
    ```

2. Register the webhook in Decision Center using the REST API tool, providing the token and URL (`http://localhost:3000/slack` and `http://localhost:3000/print`).

   1. For the File Notifier:
   ```shell
    curl -X 'PUT' \
    'http://localhost:9060/decisioncenter-api/v1/webhook/notify?url=http%3A%2F%2Flogifle%3A3000%2Fprint' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d 'null' \
    -u odmAdmin:odmAdmin
    ```

   2. For the Slack Notifier:
    
    ```shell
    export SLACK_TOKEN=`curl 'http://localhost:3000/token.generate' -H 'accept: */*' -H 'Content-Type: application/json'`

    curl -X 'PUT' \
    'http://localhost:9060/decisioncenter-api/v1/webhook/notify?url=http%3A%2F%2Fslack%3A3000%2Fslack' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d "$SLACK_TOKEN" \
    -u odmAdmin:odmAdmin
    ```

###  Using the Sample

Once webhooks are set up, various events in Decision Center trigger notifications. 

For instance, deploying a rule app triggers a notification with a content as follows in the docker-compose windows:

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

The Slack webhook notifier forwards the notifications to Slack and the log files notifier saves them in the `results` directory in the log files below:
- `deployments.txt`: Deployment details.
- `rules.txt`: Rule changes.
- `releases.txt`: Release details.
- `activities.txt`: Activity details.

### Stopping the Sample

```bash
docker-compose  -f docker-compose.yaml -f compose-odm-dev.yml down
```



