# Automating the deployment and execution of rulesets with the REST API

## Introduction

This sample demonstrates how to automate the deployment and execution of a ruleset using the REST API.

In this sample, you will:
- deploy ODM either on Kubernetes or in Docker,
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

| Name  | Description | Value for Docker |
| - | - | - |
| DC_API_URL | Decision Center API URL | http://localhost:9060/decisioncenter-api |
| DSR_URL | Decision Server Runtime URL | http://localhost:9060/decisioncenter/DecisionService |

<!-- markdown-link-check-enable-->

### 2. Deploy the `Loan Validation Service` Decision Service

If the `Loan Validation Service` Decision Service is not available in Decision Center yet, run:

```bash
# configure the Authentication (using Basic Auth)
export auth_credentials=(--user "odmAdmin:odmAdmin")

export decision_service_name="Loan Validation Service"
export filename="${decision_service_name// /_}.zip" # replace spaces by underscores
export filename_urlencoded="${decision_service_name// /%20}.zip" # replace spaces by %20

# download the Decision Service zip file
curl -sL -o ${filename} "https://github.com/DecisionsDev/odm-for-dev-getting-started/blob/master/${filename_urlencoded}?raw=1"

# upload the Decision Service in Decision Center
curl -sk -X POST ${auth_credentials[@]} -H "accept: application/json" -H "Content-Type: multipart/form-data" \
    --form "file=@${filename};type=application/zip" \
    "${DC_API_URL}/v1/decisionservices/import"
```

## Running the sample

```bash
# configure the Authentication (using Basic Auth)
export auth_credentials=(--user "odmAdmin:odmAdmin")

# get the ID of the Decision Service in Decision Center
export decision_service_name="Loan Validation Service"
export decision_service_name_urlencoded="${decision_service_name// /%20}" # replace spaces by %20
export get_decisionService_result=$(curl -sk -X GET ${auth_credentials[@]} -H "accept: application/json" "${DC_API_URL}/v1/decisionservices?q=name%3A${decision_service_name_urlencoded}")
export decisionServiceId=$(echo ${get_decisionService_result} | jq -r '.elements[0].id')

# get the ID of the deployment configuration in Decision Center
export deployment_name="production deployment"
export deployment_name_urlencoded="${deployment_name// /%20}" # replace spaces by %20
export get_deployment_result=$(curl -sk -X GET ${auth_credentials[@]} -H "accept: application/json" "${DC_API_URL}/v1/decisionservices/${decisionServiceId}/deployments?q=name%3A${deployment_name_urlencoded}")
export deploymentConfigurationId=$(echo ${get_deployment_result} | jq -r '.elements[0].id')

# deploy the ruleapp from Decision Center
curl -sk -X POST ${auth_credentials[@]} -H "accept: application/json" "${DC_API_URL}/v1/deployments/${deploymentConfigurationId}/deploy" | jq

# execute the ruleset that was deployed
export ruleset_path="/production_deployment/loan_validation_production"
curl -sk -X POST ${auth_credentials[@]} -H "accept: application/json" -H "Content-Type: application/json" -d "@payload.json" ${DSR_URL}/rest${ruleset_path} | jq
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
> export access_token=$(curl -sk -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "client_id=${CLIENT_ID}&scope=openid&client_secret=${CLIENT_SECRET}&grant_type=client_credentials" ${OPENID_TOKEN_URL} | jq -r '.access_token')
> export auth_credentials=(-H "Authorization: Bearer ${access_token}")
> ```
> - In a CP4BA environment, the authentication can be performed using a [Zen API key](https://www.ibm.com/docs/en/cloud-paks/cp-biz-automation/24.0.1?topic=access-using-zen-api-key-authentication). To do so:
>    - set the environment variables
>      - USERNAME
>      - ZEN_API_KEY
>    - replace `export auth_credentials=(--user "odmAdmin:odmAdmin")` by the commands below:
> ```bash
> export base64encoded_username_and_APIkey=$(echo "${USERNAME}:${ZEN_API_KEY}" | openssl base64)
> export auth_credentials=(-H "Authorization: ZenApiKey ${base64encoded_username_and_APIkey}")
> ```
