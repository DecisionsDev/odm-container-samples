
# Webhook Notification Setup Sample

This project demonstrates how to configure webhook notifications for IBM Operational Decision Manager (ODM), allowing you to send events to either Slack or log files using a Node.js server.

## Prerequisites

- IBM ODM with Decision Center installed.
- A running Node.js server.
- For Slack notifications, you need a Slack channel with an incoming webhook URL and a token.

## Setup Instructions

### 1. Webhook for Slack

1. Navigate to the Slack webhook sample directory:
    ```bash
    cd <InstallDir>/decisioncenter/samples/webhooknotifier/slack
    ```
2. Add your Slack webhook URL and token in the `endPoints.json` file.
3. Install the necessary Node.js dependencies:
    ```bash
    npm install
    ```
4. Start the Slack notifier:
    ```bash
    node slack-notifier.js
    ```
5. Optionally, generate a security token for the webhook:
    ```bash
    http://localhost:3000/token.generate
    ```
6. Register the webhook in Decision Center using the REST API tool, providing the token and URL (`http://localhost:3000/slack`).

### 2. Webhook for Log Files

1. Navigate to the file notifier sample directory:
    ```bash
    cd <InstallDir>/decisioncenter/samples/webhooknotifier/files
    ```
2. Install the necessary Node.js dependencies:
    ```bash
    npm install
    ```
3. Start the log notifier:
    ```bash
    node log-notifier.js
    ```
4. Register the webhook in Decision Center by providing the file notifier URL (`http://localhost:3000/print`) using the REST API tool.

## Running the Sample

Once the webhook is set up, events from the Decision Center will trigger the notifications and either post messages to Slack or generate log files in the `results` directory. 

Log file structure:
- `deployments.txt`: Deployment details.
- `rules.txt`: Rule changes.
- `releases.txt`: Release details.
- `activities.txt`: Activity details.

## Stopping the Sample

To stop the server, terminate the Node.js process running in the terminal.

## Source Files

You can explore and modify the source files under `<InstallDir>/decisioncenter/samples/webhooknotifier/`, which includes:
- `slack-notifier.js`: Slack webhook implementation.
- `log-notifier.js`: File-based webhook implementation.
