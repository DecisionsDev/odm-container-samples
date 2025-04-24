
# Introduction

This readme explains how to run the BOM dynamic domain sample in Docker.

Doing so, you do not need to have ODM installed. Instead we are relying on the [ODM for developers](https://github.com/DecisionsDev/odm-for-developers) container image.

Before following the steps below, make sure you have built the customization JAR as explained in [README.md](README.md).

# Configuring the Sample for Docker

## 1. Build the Sample Code

The following steps show how to compile the sample code into a JAR file using a Docker container with Maven and JDK 21.

1. Navigate to the project directory:
   ```bash
   cd decisioncenter/dynamicdomain/src/ilog.rules.studio.samples.bomdomainpopulate
   ```

1. Run the command below to build the JAR file:
   ```bash
   docker run --rm \
        -v "$(pwd)":/usr/src/sample \
        -w /usr/src/sample \
        maven:3.9.9-ibm-semeru-21-jammy \
        mvn clean install -Dtarget.docker
   ```
   > **Result**: The generated JAR file will be located in the `target` directory with the name `bomdomainpopulate-1.0.jar`.


## 2. Start the ODM Container

To set up the ODM container with dynamic domain support:

1. Start the ODM container:
   ```bash
   docker-compose up odm-dynamic-domain &
   ```
   > **Explanation**: This command initializes the ODM environment required for the sample.

1. Copy and configure the H2 database library for Decision Center:
   ```bash
   docker-compose exec odm-dynamic-domain sh -c "cp /config/resources/h2*.jar /config/apps/decisioncenter.war/WEB-INF/lib/h2.jar"
   docker-compose restart odm-dynamic-domain
   ```
   > **Explanation**: The H2 database library is needed to support the dynamic domains sample. Restarting ensures all configurations take effect.


## 3. Initialize the Dynamic Domains Database

To set up and populate the dynamic domains database:

1. Run the initialization script:
   ```bash
   docker-compose exec odm-dynamic-domain sh -c "java \
       -cp /config/resources/h2*.jar \
       org.h2.tools.RunScript \
       -url jdbc:h2:/config/dbdata/bomdomain \
       -user sa \
       -script /script/sql/createAndPopulate.sql \
       -showResults"
   ```
   > **Explanation**: This command initializes the H2 database schema for the sample and populates it with the necessary data, enabling ODM to recognize and use the dynamic domain setup.

# Using the sample

1. Log in to the Business Console at [http://localhost:9060/decisioncenter](http://localhost:9060/decisioncenter).<!-- markdown-link-check-disable-line -->
    - user = `rtsAdmin`
    - password = `rtsAdmin`
1. Navigate to the **Administration** tab. In the **Settings** sub-tab, click **Custom Settings** and click the *Add custom setting* **icon** and set:
    - name = `teamserver.derbyDataBaseDomainProvider`
    - description = `derbyDataBaseDomainProvider`
    - type = `String`
    - leave `default value` empty. Save the setting.
1. Set the value of this new custom setting to `ilog.rules.studio.samples.bomdomainpopulate.DataBaseDomainValueProvider`.
1. Navigate to the **Library** tab.
1. Import the rule project archive `dynamicdomain/projects/bomdomainpopulate-rules.zip`.
    > Note: this rule project `bomdomainpopulate-rules` is only aimed at editing rules to demonstrate loading domains from a database. It is missing a deployment configuration and cannot be executed.
1. Once the archive is imported, the **bomdomainpopulate-rules** decision service will be displayed. Click the **main** branch to access to the decision artifacts. 
1. Display the rule `CheckOrder > OrderType`. An error **Value (string) 'CompanyX' is incorrect** is displayed. Edit the rule and either remove **"CompanyX"** and press SPACE or double-click **"CompanyX"**. A list of suitable companies gets displayed in a drop-down. `CompanyX` is not one of these companies. Close down the rule **without** saving.
1. Display the rule `CheckCurrency > CurrencyRestriction`. No warning is displayed.
1. Let's now make some changes in the dynamic domains in the database. Run:
    ```bash
    docker-compose exec odm-dynamic-domain sh -c "java \
        -cp /config/resources/h2*.jar \
        org.h2.tools.RunScript \
        -url jdbc:h2:/config/dbdata/bomdomain \
        -user sa \
        -script /script/sql/modifyTables.sql \
        -showResults"
    ```
1. Display the rule `CheckOrder > OrderType` back again. Notice that there is no error anymore. The effects of the changes (the addition of `CompanyX` and `CompanyY`) done in the database are taken into account automatically because the values that the field `stock` can take are dynamically fetched from the database (and not stored in the BOM).
1. Conversely if you display the rule `CheckCurrency > CurrencyRestriction`, there is still no warning. So let's now import the changes done in the database into the BOM. Click the **Model** tab, and then the **Dynamic Domains** sub-tab. Expand all the three domains. You should see this: (Notice that the **Australian Dollar** was removed)

    ![Dynamic Domains update](images/dynamicDomainsUpdate.png)

1. Tick **Domain** to select all the domains, and click the **Apply changes** button. Confirm the change.
1. Display the rule `CheckCurrency > CurrencyRestriction` back again. Now a warning `'Australian Dollar' is deprecated` is displayed as the result of the update of the Dynamic Domains in the BOM.

# Stopping the sample

```bash
docker-compose down
```