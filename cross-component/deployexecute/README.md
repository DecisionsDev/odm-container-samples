# Automating the deployment and execution of rulesets with the REST API

## Introduction

This sample demonstrates how to automate the deployment and execution of a ruleset using the REST API.

In this sample, you will:
- deploy ODM either on Kubernetes or in Docker,
- import a Decision Service in Decision Center (optionally),
- deploy a ruleset from Decision Center to Decision Server,
- execute this ruleset.

## Prerequisites

- Ensure you have at least Docker 24.0.x or Kubernetes 1.27+
- Install [jq](https://jqlang.github.io/jq/download/)

## Installation of ODM

Click one of the links below depending on how you prefer to deploy ODM:
   * [Kubernetes](README-KUBERNETES.md)
   * [Docker](README-DOCKER.md)

## Configuration

### 1. Set Environment Variables

Set the following environment variables:

<!-- markdown-link-check-disable --> 

| Name  | Description | Value on Kubernetes | Value on Docker |
| - | - | - | - |
| DC_API_URL | Decision Center API URL | see below | http://localhost:9060/decisioncenter-api |
| DSR_URL | Decision Server Runtime URL | see below | http://localhost:9060/decisioncenter/DecisionService |

<!-- markdown-link-check-enable-->

If ODM is deployed on Kubernetes, you can check the section **Access the ODM services** for your platform to find out how to get those URLs:
- [Amazon EKS](https://github.com/DecisionsDev/odm-docker-kubernetes/blob/master/platform/eks/README.md#6-access-the-odm-services)
- [Azure](https://github.com/DecisionsDev/odm-docker-kubernetes/blob/master/platform/azure/README.md#access-odm-services)
- [Google GKE](https://github.com/DecisionsDev/odm-docker-kubernetes/blob/master/platform/gcloud/README.md#6-access-odm-services)
- [Minikube](https://github.com/DecisionsDev/odm-docker-kubernetes/blob/master/platform/minikube/README.md#c-access-the-odm-services)
- [Openshift](https://github.com/DecisionsDev/odm-docker-kubernetes/blob/master/platform/roks/README.md#4-access-the-odm-services)

### 2. Deploy the `Loan Validation Service` Decision Service

If the `Loan Validation Service` Decision Service is not available in Decision Center yet, run:

```bash
# configure the Authentication (using Basic Auth)
export AUTH_CREDENTIALS=(--user "odmAdmin:odmAdmin")

export DECISION_SERVICE_NAME="Loan Validation Service"
export DS_FILENAME="${DECISION_SERVICE_NAME// /_}.zip" # replace spaces by underscores
export DS_FILENAME_URLENCODED="${DECISION_SERVICE_NAME// /%20}.zip" # replace spaces by %20

# download the Decision Service zip file
curl -sL -o ${DS_FILENAME} "https://github.com/DecisionsDev/odm-for-dev-getting-started/blob/master/${DS_FILENAME_URLENCODED}?raw=1"

# upload the Decision Service in Decision Center
curl -sk -X POST ${AUTH_CREDENTIALS[@]} -H "accept: application/json" -H "Content-Type: multipart/form-data" \
    --form "file=@${DS_FILENAME};type=application/zip" \
    "${DC_API_URL}/v1/decisionservices/import"
```

## Running the sample

```bash
# configure the Authentication (using Basic Auth)
export AUTH_CREDENTIALS=(--user "odmAdmin:odmAdmin")

# get the ID of the Decision Service in Decision Center
export DECISION_SERVICE_NAME="Loan Validation Service"
export DECISION_SERVICE_NAME_URLENCODED="${DECISION_SERVICE_NAME// /%20}" # replace spaces by %20
export GET_DECISIONSERVICE_RESULT=$(curl -sk -X GET ${AUTH_CREDENTIALS[@]} -H "accept: application/json" "${DC_API_URL}/v1/decisionservices?q=name%3A${DECISION_SERVICE_NAME_URLENCODED}")
export DECISIONSERVICEID=$(echo ${GET_DECISIONSERVICE_RESULT} | jq -r '.elements[0].id')

# get the ID of the deployment configuration in Decision Center
export DEPLOYMENT_NAME="production deployment"
export DEPLOYMENT_NAME_URLENCODED="${DEPLOYMENT_NAME// /%20}" # replace spaces by %20
export GET_DEPLOYMENT_RESULT=$(curl -sk -X GET ${AUTH_CREDENTIALS[@]} -H "accept: application/json" "${DC_API_URL}/v1/decisionservices/${DECISIONSERVICEID}/deployments?q=name%3A${DEPLOYMENT_NAME_URLENCODED}")
export DEPLOYMENTCONFIGURATIONID=$(echo ${GET_DEPLOYMENT_RESULT} | jq -r '.elements[0].id')

# deploy the ruleapp from Decision Center
curl -sk -X POST ${AUTH_CREDENTIALS[@]} -H "accept: application/json" "${DC_API_URL}/v1/deployments/${DEPLOYMENTCONFIGURATIONID}/deploy" | jq

# execute the ruleset that was deployed
export RULESET_PATH="/production_deployment/loan_validation_production"
curl -sk -X POST ${AUTH_CREDENTIALS[@]} -H "accept: application/json" -H "Content-Type: application/json" -d "@payload.json" ${DSR_URL}/rest${RULESET_PATH} | jq
```

> [!NOTE]
> The commands above rely on the Basic Authentication. 
> 
> - In an environment with OpenID Connect, the authentication can be performed using an access token retrieved with the `client_credentials` grant type by:
>    - setting the environment variables
>       - CLIENT_ID
>       - CLIENT_SECRET
>       - OPENID_TOKEN_URL
>    - and replacing `export auth_credentials=(--user "odmAdmin:odmAdmin")` by the commands below:
> ```bash
> export ACCESS_TOKEN=$(curl -sk -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "client_id=${CLIENT_ID}&scope=openid&client_secret=${CLIENT_SECRET}&grant_type=client_credentials" ${OPENID_TOKEN_URL} | jq -r '.access_token')
> export AUTH_CREDENTIALS=(-H "Authorization: Bearer ${ACCESS_TOKEN}")
> ```
> - In a CP4BA environment, the authentication can be performed using a [Zen API key](https://www.ibm.com/docs/en/cloud-paks/cp-biz-automation/24.0.1?topic=access-using-zen-api-key-authentication). To do so:
>    - set the environment variables
>      - USERNAME
>      - ZEN_API_KEY
>    - replace `export auth_credentials=(--user "odmAdmin:odmAdmin")` by the commands below:
> ```bash
> export BASE64ENCODED_USERNAME_AND_APIKEY=$(echo "${USERNAME}:${ZEN_API_KEY}" | base64)
> export AUTH_CREDENTIALS=(-H "Authorization: ZenApiKey ${BASE64ENCODED_USERNAME_AND_APIKEY}")
> ```

## Expected result

You should see traces similar to these:
1. after the ruleapp deployment:
    ```
    {
    "id": "e51db153-bca2-49a8-8998-2b5e6c93f6cb",
    "internalId": "dsm.DSDeploymentReport:35:35",
    "name": "Report  2025-01-27_11-11-37-203",
    "createdBy": "odmAdmin",
    "createdOn": "2025-01-27T10:11:37.000+00:00",
    "lastchangedBy": "odmAdmin",
    "lastChangedOn": "2025-01-27T10:11:51.000+00:00",
    "status": "COMPLETED",
    "ruleAppName": "production_deployment",
    "messages": {
        "elements": [
        {
            "message": "The version '1.0' of the ruleset 'loan_validation_production' was successfully deployed to Decision Service Execution (http://localhost:9060/res).",
            "ruleArtifact": null,
            "severity": "INFO"
        },
        {
            "message": "The RuleApp was successfully deployed to Decision Service Execution (http://localhost:9060/res).",
            "ruleArtifact": null,
            "severity": "INFO"
        }
        ],
        "totalCount": 2,
        "number": 0,
        "size": 2
    },
    "snapshot": {
        "id": "f5efc08f-23d2-46ef-bed8-515fec6b46cf",
        "internalId": "dsm.DsDeploymentBsln:71:71",
        "name": "production-deployment_2025-01-27_11-11-37-153",
        "createdBy": "odmAdmin",
        "createdOn": "2025-01-27T10:11:37.000+00:00",
        "lastchangedBy": "odmAdmin",
        "lastChangedOn": "2025-01-27T10:11:38.000+00:00",
        "parentId": "1558f25b-daa6-4982-8b0b-48a388c7c202",
        "documentation": null,
        "buildMode": "DecisionEngine",
        "kind": "DeploymentSnapshot"
    },
    "servers": [
        "Decision Service Execution"
    ],
    "archive": null
    }
    ```
1. after the ruleset execution:
    ```
    {
    "report": {
        "borrower": {
        "firstName": "string",
        "lastName": "string",
        "birth": "1988-09-29T01:49:45.000+0000",
        "SSN": {
            "areaNumber": "string",
            "groupCode": "string",
            "serialNumber": "string"
        },
        "yearlyIncome": 3,
        "zipCode": "string",
        "creditScore": 3,
        "spouse": {
            "birth": "1982-12-08T14:13:09.850+0000",
            "SSN": {
            "areaNumber": "",
            "groupCode": "",
            "serialNumber": ""
            },
            "yearlyIncome": 0,
            "creditScore": 0,
            "latestBankruptcy": {
            "chapter": 0
            }
        },
        "latestBankruptcy": {
            "date": "2014-09-18T23:18:33.000+0000",
            "chapter": 3,
            "reason": "string"
        }
        },
        "loan": {
        "numberOfMonthlyPayments": 3,
        "startDate": "2025-08-19T17:27:14.000+0000",
        "amount": 3,
        "loanToValue": 1.051732E+7
        },
        "validData": true,
        "insuranceRequired": true,
        "insuranceRate": 0.02,
        "approved": true,
        "messages": [],
        "yearlyInterestRate": 0.0,
        "monthlyRepayment": 0.0,
        "insurance": "2%",
        "message": "",
        "yearlyRepayment": 0.0
    },
    "__DecisionID__": "aa46362d-fa30-4331-b241-d1ff7e8ac4270"
    }
    ```
