### Introduction

This readme explains how to run the webhook sample in Kubernetes.

Doing so requires to have Decision Center set up in Kubernetes beforehand.

Also, before following the steps below, make sure you have built the images as explained in [README.md](README.md).

###  Running this sample on Kubernetes

1. Push the webhook notifier images to your Docker registry

     ```bash
     docker login $REGISTRY_HOST -u $REGISTRY_USERNAME -p "$REGISTRY_PASSWORD" --tls-verify=false

     docker tag webhooknotifier-logfile $REGISTRY_HOST/webhooknotifier-logfile
     docker tag webhooknotifier-slack $REGISTRY_HOST/webhooknotifier-slack

     docker push $REGISTRY_HOST/webhooknotifier-logfile
     docker push $REGISTRY_HOST/webhooknotifier-slack
     ```

     where `$REGISTRY_HOST` should be set to the hostname of the Docker registry optionally followed by the relevant repository depending on your Docker registry.

2. Start one pod for each image

     Set the current context to the namespace the Decision Center pods are running, and then run:

     ```bash
     kubectl run webhooknotifier-logfile \
     --image=$REGISTRY_HOST/webhooknotifier-logfile:latest \
     --expose --port 3000
     
     kubectl run webhooknotifier-slack \
     --image=$REGISTRY_HOST/webhooknotifier-slack:latest \
     --expose --port 3000
     ```

     >NOTE 1: for an Openshift registry, `$REGISTRY_HOST` should be `image-registry.openshift-image-registry.svc:5000`

     <!-- markdown-link-check-disable -->
     >NOTE 2: These commands also create one service (of type ClusterIP) for each pod, allowing to send requests to the pods using the following URLs:
     > - http://webhooknotifier-logfile.NAMESPACE.svc.cluster.local:3000
     > - http://webhooknotifier-slack.NAMESPACE.svc.cluster.local:3000
     >
     > where `NAMESPACE` is the namespace in which the pods and the services have been created.
     <!-- markdown-link-check-enable -->

     Additionally, you may need to specify the secret containing the Docker registry credentials in the pods definition (in the parameter `spec.imagePullSecrets`) if this is not done automatically.
     
     To do so, first create the secret:
     ```bash
     kubectl create secret docker-registry regcred --docker-server=<your-registry-server> --docker-username=<your-name> --docker-password=<your-pword> --docker-email=<your-email>
     ```

     Then run `kubectl edit pod webhooknotifier-logile` and add the two lines below under `spec:` :
     ```bash
     spec:
       imagePullSecrets:
       - name: regcred
     ```

     Do the same for the pod `webhooknotifier-slack`.

3. Configure the webhook notifications in Decision Center

     1. Log files notifications

     ```shell
     curl -k -X 'PUT' \
     'https://$DC_HOST/decisioncenter-api/v1/webhook/notify?url=http%3A%2F%2Fwebhooknotifier-logfile.$NAMESPACE.svc.cluster.local%3A3000%2Fprint' \
     -H 'accept: */*' \
     -H 'Content-Type: application/json' \
     -d 'null' \
     -u odmAdmin:odmAdmin
     ```

     where 
     - `$DC_HOST` should be set to the hostname of Decision Center,
     - `$NAMESPACE` should be set to the name of the namespace/project in which the webhook notifier pods and service have been created,
     - `odmAdmin:odmAdmin` should be replaced by the actual username and password of a user with rtsAdministrator role.

     2. Slack Notifications
     
     ```shell
     export SLACK_TOKEN=`kubectl exec -ti webhooknotifier-slack -- curl 'http://localhost:3000/token.generate' -H 'accept: */*' -H 'Content-Type: application/json'`

     curl -X 'PUT' -k \
     'https://$DC_HOST/decisioncenter-api/v1/webhook/notify?url=http%3A%2F%2Fwebhooknotifier-slack.$NAMESPACE.svc.cluster.local%3A3000%2Fslack' \
     -H 'accept: */*' \
     -H 'Content-Type: application/json' \
     -d "$SLACK_TOKEN" \
     -u odmAdmin:odmAdmin
     ```

     where:
     - `$DC_HOST` should be set to the hostname of Decision Center,
     - `$NAMESPACE` should be set to the name of the namespace/project in which the webhook notifier pods and service have been created,
     - `odmAdmin:odmAdmin` should be replaced by the actual username and password of a user with rtsAdministrator role.

     The expected output looks like:
     ```
     {"id":"57896361-62f8-4a0e-a86b-3da46417f493","url":"http://webhooknotifier-slack.<NAMESPACE>.svc.cluster.local:3000/slack","authToken":"*****************************************************"}%
     ```

###  Using the Sample

Once webhooks are set up, various events in Decision Center trigger notifications. 

For instance, deploying a rule app triggers a notification with a content like:

```bash
{
  "version": "1.0",
  "id": "cc66c9e8-9905-486d-99e9-7ab89af3d976",
  "author": "odmAdmin",
  "date": 1725633110628,
  "type": "SnapshotCreated",
  "content": [
    {
      "id": "dcf08c59-877c-42d4-9360-2189345577c8",
      "internalId": "dsm.DsDeploymentBsln:103:103",
      "name": "test_deployment_2024-09-06_16-31-45-855",
      "createdBy": "odmAdmin",
      "createdOn": 1725633110000,
      "lastchangedBy": "odmAdmin",
      "lastChangedOn": 1725633110000,
      "parentId": "1558f25b-daa6-4982-8b0b-48a388c7c202",
      "documentation": null,
      "buildMode": "DecisionEngine",
      "kind": "DeploymentSnapshot"
    }
  ],
  "details": [
    {
      "targetURL": "http://172.22.0.4:9060/decisioncenter/t/library#overviewsnapshot?id=dsm.DsDeploymentBsln%3A103%3A103&datasource=jdbc%2FilogDataSource&baselineId=dsm.DsDeploymentBsln%3A103%3A103"
  }
],
```

The Slack webhook notifier forwards the notifications to Slack and the log files notifier saves them in the `results` directory in the log files below:
- `deployments.txt`: Deployment details.
- `rules.txt`: Rule changes.
- `releases.txt`: Release details.
- `activities.txt`: Activity details.

### Stopping the Sample

To remove the webhooks configured in Decision Center, first list them:

```bash
curl -X 'GET' \
  'https://$DC_HOST/decisioncenter-api/v1/webhooks/notify' \
  -H 'accept: */*'
```

where `$DC_HOST` should be set to the hostname of Decision Center.

The expected output looks like:

```bash
{
  "elements": [
    {
      "id": "d2808ed5-f074-45ff-8905-a20d90279378",
      "url": "http://webhooknotifier-slack.default.svc.cluster.local:3000/slack",
      "authToken": "*************************************************************************************************************************************************************************************"
    },
  ...
```

You can then remove a webhook from Decision Center, by running the command below:

```bash
curl -X 'DELETE' \
  'https://$DC_HOST/decisioncenter-api/v1/webhooks/$WEBHOOK_ID/notify' \
  -H 'accept: */*'
```

where
- `$DC_HOST` should be set to the hostname of Decision Center,
- `$WEBHOOK_ID`should be set to the id of the webhook you want to remove (eg. `d2808ed5-f074-45ff-8905-a20d90279378` in the example above)

Finally, remove the notifier pods and their services:

```bash
oc delete svc,pod webhooknotifier-logfile
oc delete svc,pod webhooknotifier-slack
```
