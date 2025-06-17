# GUI Customization Sample

## Introduction

This sample shows you how to customize the Business Console using [GUI customization sample](https://www.ibm.com/docs/en/odm/9.5.0?topic=center-gui-customization) in a container environment.

## Running this sample in Decision Center

### 1) Prerequisites

Before you begin, ensure you have at least Docker 27.x (and optionally Kubernetes 1.28+). 

### 2) Building the Decision Center extension JAR

To use the sample in Decision Center, you need to build a JAR. 

   1. Retrieve ODM libraries:

      Navigate to the source directory of the GUI Customization sample:
      ```
      cd decisioncenter/guicustomization/guicustomization-source
      ```
      
      Install ODM for Developer :
      ```
      docker-compose -f compose-odm-dev.yml up odm &
      ```

      Download the **decision-center-client-api.zip** file :
      ```
      wget http://localhost:9060/decisioncenter/assets/decision-center-client-api.zip
      ```

      Then, unzip decision-center-client-api.zip to the **lib** directory :
      ```
      unzip decision-center-client-api.zip -d "lib"
      ```
      
      Uninstall the ODM for Developer instance to avoid a port usage conflict if you continue this tutorial on [Docker](README-DOCKER.md) :
      ```
      docker-compose -f compose-odm-dev.yml down
      ```

   2. Build the JAR

      The instructions below enable to build the JAR using a Docker container featuring Maven and a JDK version 21, compatible for ODM 9.5.
      
      Run the command below in the **decisioncenter/guicustomization/guicustomization-source** directory:

         ```bash
         docker run --rm \
               -v "$(pwd)":/usr/src/sample \
               -w /usr/src/sample \
               maven:3.9.9-ibm-semeru-21-jammy \
               mvn clean install
         ```

      The **guicustomization-1.0.jar** file is generated in the **target** directory.

### 3) Instructions to use the sample in Decision Center

Click one of the links below:
   * In [Kubernetes](README-KUBERNETES.md).
   * In [Docker](README-DOCKER.md). 
