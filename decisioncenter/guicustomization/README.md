# GUI Customization Sample

## Introduction

This sample shows you how to customize the Business console.
It is the ODM on k8s adaptation of the ODM on premises [GUI customization sample](https://www.ibm.com/docs/en/odm/9.0.0?topic=center-gui-customization).

## Running this sample in Decision Center

### 1) Prerequisites

Before you begin, ensure you have one of the following **Container Platform**: Docker 24.0.x or Kubernetes 1.27+.

### 2) Building the Decision Center extension JAR

To use the sample in Decision Center, you need to build a JAR. 

   1. Retrieve ODM libraries:

      ODM libraries are required to compile the JAR. 

      Download ODM libraries from Decision Center

          - Navigate to the source directory of the GUI Customization sample:

            ```bash
            cd decisioncenter/guicustomization/guicustomization-source
            ```

          - Download the following compressed file: `https://DC_HOST:DC_PORT/decisioncenter/assets/decision-center-client-api.zip`

          - Then, run:
            ```bash
            unzip decision-center-client-api.zip -d "lib"
            ```

   1. Build the JAR

      The instructions below enable to build the JAR using a Docker container featuring Maven and a JDK version 17. For ODM 8.12, you must use `maven:3.8.1-openjdk-11` instead and `maven:3.8-adoptopenjdk-8` for earlier releases.

      Run the command below in the `decisioncenter/dynamicdomain/guicustomization-source` directory:

         ```bash
         docker run --rm --name my-maven-container \
               -v "$(pwd)":/usr/src/sample \
               -w /usr/src/sample \
               maven:3.8.5-openjdk-17 \
               mvn clean install
         ```

      The JAR is generated in the `target` directory and is named `guicustomization-1.0.jar`.

### 4) Instructions to use the sample in Decision Center

Click one of the links below:
   * In [Kubernetes](README-KUBERNETES.md).
   * In [Docker](README-DOCKER.md). 
