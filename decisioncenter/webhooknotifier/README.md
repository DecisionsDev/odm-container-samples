
# Webhook Notification Setup Sample

This project demonstrates how to configure webhook notifications for IBM Operational Decision Manager (ODM), allowing you to send events to either Slack or log files using a Node.js server.

## Prerequisites

- docker installed
- docker-compose 
- (Optional) For Slack notifications, you need a Slack channel with an incoming webhook URL and a token.

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



### 2. Run this sample on Docker
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
    curl -X 'GET' \
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
