# BOM dynamic domain Sample

## Introduction

ODM enables to create BOM domains that are either static or dynamic: 
- In a static domain, the list of values are managed inside the BOM.
- In a dynamic domain, the list of values are managed outside the BOM and then imported into the BOM (and updated later on when a change is made).

Off-the-shelf, ODM only enables to store the values of a dynamic domain in an Excel file.

This sample demonstrates how to store the values of a dynamic domain in a database.

This sample was adapted from the [Data sources for BOM domains](https://www.ibm.com/docs/en/odm/9.0.0?topic=extensions-data-sources-bom-domains) sample for (on-premise) Rule Designer.
This sample runs both in Rule Designer and in Decision Center.

In this sample, you will :
- build the customization required to manage dynamic domains whose values are stored in a database,
- deploy ODM so that Decision Center uses this extension,
- store new values of the dynamic domains in a database,
- update the rule project with the new values of the dynamic domains.

## Running this sample in Rule Designer

### 1) Building the Eclipse plugin

To use the sample in Rule Designer, you need to build an Eclipse plugin.
- In Eclipse, make sure that,
    - **Rule Designer** is installed (if not read these [instructions](https://github.com/DecisionsDev/ruledesigner))
    - **PDE** (Plugin Development Environment) is installed
- Run **Import > General > Existing Projects into Workspace**
- Select the folder `decisioncenter/dynamicdomain/src/ilog.rules.studio.samples.bomdomainpopulate` and click **Next**
- Tick the project `ilog.rules.studio.samples.bomdomainpopulate` and click **Finish**
- Right-click the `build-bomdomainepopulate.xml` file and click **Run as > Ant Build**. A file named `bomdomainpopulate.jar` gets generated at the root of the project.
- Run **Export > Plug-in Development > Deployable Plug-ins and fragments** to build the plugin. Click **Next**, choose the destination directory, and click **Finish**. A file named `ilog.rules.studio.samples.bomdomainpopulate_VERSION.jar` gets generated in the chosen directory (in the `plugins` sub-directory).
- Copy the plugin into `<ECLIPSE_HOME>/dropins` to deploy it.
- Restart Eclipse.

  >Note: if you modify the plugin and deploy it again, start Eclipse with the `-clean` parameter to force a cache update.

### 2) Instructions to use the sample in Rule Designer

- Import the projects below located in the `projects/` directory:
  - bomdomainpopulate-bd-helper
  - bomdomainpopulate-rules
  - bomdomainpopulate-xom

- Follow the [instructions from the documentation](https://www.ibm.com/docs/en/odm/9.0.0?topic=domains-data-sources-bom-sample-details).

## Running this sample in Decision Center

### 1) Prerequisites

Before you begin, ensure you have one of the following **Container Platform**: Docker 24.0.x or Kubernetes 1.27+.

### 2) Configuring how to connect to the database

If you deploy ODM only to test the sample, it is best to use the [PostgreSQL internal database](https://www.ibm.com/docs/en/SSQP76_9.0.0/com.ibm.odm.kube/topics/con_internal_db.html) to store the dynamic domain data (along with Decision Center data).

In that case, and provided ODM is deployed in a Helm Release named `myodmsample`, you do not need to modify [database.properties](src/ilog.rules.studio.samples.bomdomainpopulate/src/main/resources/data/database.properties).
Edit that file otherwise.

> Note: The data of the dynamic domains are stored in a separate schema named `BOMDOMAINSAMPLE`

### 3) Building the Decision Center extension JAR

To use the sample in Decision Center, you need to build a JAR. 

   1. Retrieve ODM libraries:

      ODM libraries are required to compile the JAR. You can find them either from a running instance of Decision Center or from Rule Designer.

       * **Option 1:** Download ODM libraries from Decision Center

          - Navigate to the source directory of the BOM dynamic domain sample:

            ```bash
            cd decisioncenter/dynamicdomain/src/ilog.rules.studio.samples.bomdomainpopulate
            ```

          - Download the following compressed file: `https://DC_HOST:DC_PORT/decisioncenter/assets/decision-center-client-api.zip`

          - Then, run:
            ```bash
            unzip decision-center-client-api.zip -d "lib"
            ```

       * **Option 2:** use ODM libraries from Rule Designer (available in Eclipse plugins directory)

          Set the `ECLIPSE_PLUGINS` environment variable:
         ```bash
         ECLIPSE_PLUGINS=<ECLIPSE PLUGINS DIRECTORY>
         ```

   1. Build the JAR

      The instructions below enable to build the JAR using a Docker container featuring Maven and a JDK version 17. For ODM 8.12, you must use `maven:3.8.1-openjdk-11` instead and `maven:3.8-adoptopenjdk-8` for earlier releases.

      Run one of the command below in the `decisioncenter/dynamicdomain/src/ilog.rules.studio.samples.bomdomainpopulate` directory:
          
      > [!IMPORTANT]
      > add `-Dtarget.docker` after `mvn clean install` to use the sample in Docker 

       * **Option 1:** when using ODM libraries from Decision Center

         ```bash
         docker run --rm --name my-maven-container \
               -v "$(pwd)":/usr/src/sample \
               -w /usr/src/sample \
               maven:3.8.5-openjdk-17 \
               mvn clean install
         ```

       * **Option 2** when using ODM libraries from Rule Designer

         ```bash
         docker run --rm --name my-maven-container \
               -v "$(pwd)":/usr/src/sample \
               -w /usr/src/sample \
               -v "${ECLIPSE_PLUGINS}":/usr/src/eclipse/plugins \
               -e ECLIPSE_PLUGINS=/usr/src/eclipse/plugins \
               maven:3.8.5-openjdk-17 \
               mvn clean install
         ```

      The JAR is generated in the `target` directory and is named `bomdomainpopulate-1.0.jar`.

### 4) Instructions to use the sample in Decision Center

Click one of the links below:
   * In [Kubernetes](README-KUBERNETES.md).
   * In [Docker](README-DOCKER.md). 
