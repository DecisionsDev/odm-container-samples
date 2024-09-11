###  Run this sample on Kubenetes
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

###  Using the Sample

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

### Stopping the Sample

```bash
oc delete svc,pods webhooknotifier-logfile
oc delete svc,pods webhooknotifier-slack
# TODO Delete the notification hook.

```
