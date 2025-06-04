# IBM Operational Decision Manager (ODM) Samples for Container Environments

This repository provides a collection of code samples designed to showcase the capabilities of IBM Operational Decision Manager (ODM) in containerized environments. These samples illustrate best practices for deploying, managing, and customizing ODM in both development and production scenarios.

## Target Audience

This repository is aimed at both technical and business users interested in learning how to author, manage, and execute business rules using IBM ODM in a containerized setup. It covers the following ODM offerings:

- **[IBM Operational Decision Manager for Developers](https://hub.docker.com/r/ibmcom/odm/)**  
  Explore ODM in Docker environments for development and evaluation purposes.

- **[IBM Operational Decision Manager for Developers on Certified Kubernetes](https://artifacthub.io/packages/helm/ibm-odm-charts/ibm-odm-dev/25.0.0)**  
  Run ODM on certified Kubernetes environments for development and evaluation.

- **[IBM Operational Decision Manager for Production on Certified Kubernetes](https://www.ibm.com/docs/en/odm/9.5.0?topic=kubernetes-installing-odm-production)**  
  Deploy ODM on certified Kubernetes environments for production use.

## Learning Objectives

Through these samples, users will:

- Gain hands-on experience deploying IBM ODM in containerized environments.
- Learn to customize ODM behavior to meet specific business needs.
- Explore integration patterns with external systems.
  
## Repository Structure

Each sample is housed in a dedicated subfolder, containing all the required source code, dependencies, and configuration files. Installation and deployment instructions are provided in the corresponding `README.md` files within each sample directory.

### Decision Center Samples

#### Authoring extensions for Decision Center

These samples demonstrate how to customize Decision Center editing features.

- **[Custom Value Editor](decisioncenter/businessvalueeditor/README.md)**  
  Demonstrates how to integrate a custom value editor within the IntelliRule editor of the Business Console. Key features include:
  - Implementing a custom value editor.
  - Seamless integration with the Business Console.

- **[BOM Dynamic Domain](decisioncenter/dynamicdomain/README.md)**  
  Illustrates how to manage dynamic domain values stored in a database. In this sample, you will:
  - Build and deploy a customization to handle dynamic domains.
  - Deploy Decision Center with ODM using this extension.
  - Store and update dynamic domain values in a database.
  - Reflect dynamic domain changes in rule projects.

- **[GUI Customization](decisioncenter/guicustomization/README.md)**  
  Shows how to customize the Business Console interface based on user groups. This sample includes:
  - Defining a Spring controller to retrieve server-side data.
  - Adding custom buttons to the toolbar and context-sensitive areas.
  - Creating new tabs in the console, tailored to user groups.

#### Business rule management extensions for Decision Center

These samples demonstrate how to customize various business rule management functions in Decision Center.

- **[Webhook Notification Setup](decisioncenter/webhooknotifier/README.md)**  
  Demonstrates how to configure webhooks to send notifications from ODM to external systems. This sample includes:
  - Setting up the Webhook Notifier to send notifications to a Slack channel or a Node.js server.
  - Recording and managing notifications in external systems.

## License

This repository is licensed under the [Apache 2.0 License](LICENSE).

## Notice

© Copyright IBM Corporation 2025.
