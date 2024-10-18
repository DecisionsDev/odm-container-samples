# Custom Value Editor Sample

## Introduction

This sample demonstrates how to plug a custom value editor into the Business console Intellirule editor.

It is the ODM on k8s adaptation of the ODM on premises [Custom Value Editor sample](https://www.ibm.com/docs/en/odm/9.0.0?topic=center-custom-value-editor).

## Running this sample in Decision Center

### 1) Prerequisites

Before you begin, ensure you have one of the following **Container Platform**: Docker 24.0.x or Kubernetes 1.27+.

### 2) Building the Decision Center extension JAR

To use the sample in Decision Center, you need to build a JAR for [Docker](README-DOCKER.md) and a ZIP for [Kubernetes](README-KUBERNETES.md)

   1. Retrieve ODM libraries:

      Navigate to the source directory of the Custom Value Editor sample:

      ```
      cd decisioncenter/businessvalueeditor/businessvalueeditor-source
      ```
      
      ODM libraries are required to compile the JAR.
      To get the ODM libraries, you need an access to a running Decision Center instance.      
      Download the **decision-center-client-api.zip** file :

      ```
      wget https://DC_HOST:DC_PORT/decisioncenter/assets/decision-center-client-api.zip --no-check-certificate
      ```

      Then, unzip decision-center-client-api.zip on the **lib** directory :
      ```
      unzip decision-center-client-api.zip -d "lib"
      ```

> [!NOTE]
> If you don't have a running Decision Center, install it using the ODM for Developer public docker image, by running :
> ```
> docker-compose -f compose-odm-dev.yml up odm
> ```
> Then download the **decision-center-client-api.zip** file :
> ```
> wget http://localhost:9060/decisioncenter/assets/decision-center-client-api.zip
> ```
> Uninstall the ODM for Developer instance to avoid a port usage conflict if you continue this tutorial on [Docker](README-DOCKER.md) :
> ```
> docker-compose -f compose-odm-dev.yml up down
> ```

   2. Build the JAR

      The instructions below enable to build the JAR using a Docker container featuring Maven and a JDK version 17. For ODM 8.12, you must use `maven:3.8.1-openjdk-11` instead and `maven:3.8-adoptopenjdk-8` for earlier releases.

      Run the command below in the **decisioncenter/businessvalueeditor/businessvalueeditor-source** directory:

         ```bash
         docker run --rm --name my-maven-container \
               -v "$(pwd)":/usr/src/sample \
               -w /usr/src/sample \
               maven:3.8.5-openjdk-17 \
               mvn clean install
         ```

      The JAR is generated in the `target` directory and is named `businessvalueeditor-1.0.jar`.

### 3) Instructions to use the sample in Decision Center

Click one of the links below:
   * In [Kubernetes](README-KUBERNETES.md).
   * In [Docker](README-DOCKER.md). 
