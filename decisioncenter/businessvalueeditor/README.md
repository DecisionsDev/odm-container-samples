# Custom Value Editor Sample

## Introduction

This sample demonstrates how to plug a custom value editor into the Business console Intellirule editor.

It is the ODM on k8s adaptation of the ODM on premises [Custom Value Editor sample](https://www.ibm.com/docs/en/odm/9.0.0?topic=center-custom-value-editor).

## Running this sample in Decision Center

### 1) Prerequisites

Before you begin, ensure you have at least Docker 24.0.x (and optionally Kubernetes 1.27+).

### 2) Building the Decision Center extension JAR

To use the sample in Decision Center, you need to build a JAR for [Docker](README-DOCKER.md) and a ZIP for [Kubernetes](README-KUBERNETES.md)

   1. Retrieve ODM libraries:
      
      Navigate to the source directory of the Custom Value Editor sample:

      ```bash
      cd decisioncenter/businessvalueeditor/businessvalueeditor-source
      ```
      
      ODM libraries are required to compile the JAR.
      For that, deploy ODM for Developer public docker image:
      ```bash
      docker-compose -f compose-odm-dev.yml up odm &
      ```

      and then download the ODM libraries:
      ```bash
      wget http://localhost:9060/decisioncenter/assets/decision-center-client-api.zip
      ```

      Unzip decision-center-client-api.zip in the **lib** directory :
      ```bash
      unzip decision-center-client-api.zip -d "lib"
      ```

      Undeploy ODM for developer (especially if you continue this tutorial on [Docker](README-DOCKER.md) to avoid a TCP/IP port usage conflict) :
      ```bash
      docker-compose -f compose-odm-dev.yml down
      ```

   2. Build the JAR

      The instructions below allow you to build the JAR using a Docker container featuring Maven and a JDK version 21, compatible for ODM 9.5.

      Run the command in the **decisioncenter/businessvalueeditor/businessvalueeditor-source** directory:

         ```bash
         docker run --rm \
               -v "$(pwd)":/usr/src/sample \
               -w /usr/src/sample \
               maven:3.9.9-ibm-semeru-21-jammy \
               mvn clean install
         ```

      The JAR that will be used on [Docker](README-DOCKER.md) is generated in the **target** directory and is named **businessvalueeditor-1.0.jar**.

      The ZIP that will be used on [Kubernetes](README-KUBERNETES.md) is generated in the **target** directory and is named **businessvalueeditor-1.0.zip**.


### 3) Instructions to use the sample in Decision Center

Click one of the links below:
   * In [Kubernetes](README-KUBERNETES.md).
   * In [Docker](README-DOCKER.md). 
