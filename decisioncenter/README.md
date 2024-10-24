# Decision Center Samples

This directory contains various samples that demonstrate how to customize and extend the IBM Operational Decision Manager (ODM) Decision Center. Each sample provides detailed instructions and source code to help you explore and implement specific customizations.

## Available Samples

### Authoring extensions for Decision Center

These samples demonstrate how to customize Decision Center editing features.

1. **[Custom Value Editor](./businessvalueeditor/README.md)**  
   Demonstrates how to integrate a custom value editor into the IntelliRule editor in the Decision Center Business Console.  
   Key features:
   - Custom value editor implementation.
   - Seamless integration with the Business Console.

2. **[BOM Dynamic Domain](./dynamicdomain/README.md)**  
   Shows how to manage dynamic domain values stored in a database within Decision Center.  
   Key features:
   - Build and deploy customizations to manage dynamic domains.
   - Update rule projects to reflect new dynamic domain values.
   - Store and update dynamic domain values in a database.

3. **[GUI Customization](./guicustomization/README.md)**  
   Demonstrates how to customize the Business Console's user interface based on user groups.  
   Key features:
   - Add custom buttons to the toolbar and specific contexts.
   - Define new tabs and retrieve server-side data using a Spring controller.

### Business rule management extensions for Decision Center

These samples demonstrate how to customize various business rule management functions in Decision Center.

1. **[Webhook Notification Setup](./webhooknotifier/README.md)**  
   Provides a way to send notifications from ODM Decision Center to external systems using webhooks.  
   Key features:
   - Configure the Webhook Notifier to send notifications to a Slack channel or a Node.js server.
   - Record and manage notifications in external systems.# Decision Center Samples

