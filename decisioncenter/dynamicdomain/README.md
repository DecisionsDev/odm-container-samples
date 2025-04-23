# BOM dynamic domain sample

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

## 1. Running the sample in Rule Designer

### 1.1 Building the Eclipse plugin

To use the sample in Rule Designer, you need to build an Eclipse plugin.
- In Eclipse, make sure that,
    - **Rule Designer** is installed (if not read these [instructions](https://github.com/DecisionsDev/ruledesigner))
    - **PDE** (Plugin Development Environment) is installed
- Click **Import > General > Existing Projects into Workspace**
- Select the folder `decisioncenter/dynamicdomain/src/ilog.rules.studio.samples.bomdomainpopulate` 
- Tick the project `ilog.rules.studio.samples.bomdomainpopulate` and click **Finish**
- Right-click the `build-bomdomainepopulate.xml` file and click **Run as > Ant Build**. A file named `bomdomainpopulate.jar` gets generated at the root of the project.
- Right-click on the project and click **Export > Plug-in Development > Deployable Plug-ins and fragments** to build the plugin. Click **Next**, enter `<ECLIPSE_HOME>/dropins` as the destination directory (where `<ECLIPSE_HOME>` should be replaced by the HOME directory of the running Eclipse), and click **Finish**.
- Restart Eclipse.

  >Note: if you modify the plugin and deploy it again, start Eclipse with the `-clean` parameter to force a cache update.

### 1.2 Instructions to use the sample in Rule Designer

- Import the projects below located in the `decisioncenter/dynamicdomain/projects/` directory:
  - bomdomainpopulate-bd-helper
  - bomdomainpopulate-rules
  - bomdomainpopulate-xom

- Follow the [instructions from the documentation](https://www.ibm.com/docs/en/odm/9.0.0?topic=domains-data-sources-bom-sample-details) to try out how the sample works in Rule Designer.

## 2. Running the sample in Decision Center

### 2.1 Prerequisites

Ensure you have at least Docker 24.0.x (and optionally Kubernetes 1.27+).

### 2.2 Configuring how to connect to the database (kubernetes only)

If you deploy ODM on Docker, there is nothing to configure.

If you deploy ODM on kubernetes, and for the sole purpose of testing the sample, it is best to use the [PostgreSQL internal database](https://www.ibm.com/docs/en/SSQP76_9.0.0/com.ibm.odm.kube/topics/con_internal_db.html) to store the dynamic domain data (along with Decision Center data).

In that case, and provided ODM is deployed in a Helm Release named `myodmsample`, you do not need to modify [database.properties](src/ilog.rules.studio.samples.bomdomainpopulate/src/main/resources/data/database.properties).
Edit that file otherwise.

> Note: The data of the dynamic domains are stored in a separate schema named `BOMDOMAINSAMPLE`

### 3) Retrieve ODM Libraries

To use the sample in Decision Center, you need to build a JAR file that depends on IBM ODM libraries for compilation.

#### Steps:

1. **Navigate to the Sample Source Directory**

   Move to the source directory for the BOM dynamic domain sample:
   ```bash
   cd decisioncenter/dynamicdomain/src/ilog.rules.studio.samples.bomdomainpopulate
   ```

2. **Deploy ODM for Developer Using Docker**

   Start the ODM for Developer Docker image to quickly access the necessary libraries:
   ```bash
   docker-compose up odm &
   ```

3. **Download the Decision Center Client API**

   Retrieve the required libraries by downloading `decision-center-client-api.zip` from the local ODM instance:
   ```bash
   wget http://localhost:9060/decisioncenter/assets/decision-center-client-api.zip
   ```

4. **Extract Libraries**

   Unzip the downloaded archive into a directory called `lib` to make the libraries available for compilation:
   ```bash
   unzip decision-center-client-api.zip -d "lib"
   ```

5. **Stop the ODM for Developer Container**
   Undeploy ODM for developer (especially if you continue this tutorial on [Docker](README-DOCKER.md) to avoid a TCP/IP port usage conflict) :
   ```bash
   docker-compose down
   ```


### 4) Instructions to use the sample in Decision Center

Click one of the links below:
   * On [Kubernetes](README-KUBERNETES.md).
   * On [Docker](README-DOCKER.md). 
