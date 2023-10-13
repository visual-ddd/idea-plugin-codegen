# VisualDDD

![Build](https://github.com/Shimmernight/idea-plugin-dev/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
## Development Cloud - VisualDDD (Visual Development Platform for Domain-Driven Design)
Official website: https://ddd-test.wakedt.cn/login

### Environment Setup
1. Configure the development cloud host access environment
> Host: 172.31.13.178 ddd-test.wakedt.cn

2. Access the VisualDDD platform (https://ddd-test.wakedt.cn/login) to create a project and associate the required business domains and scenarios.

### Quick Start
1. Install the **VisualDDD** plugin (Visual Development Platform for Domain-Driven Design)
  - Set up the plugin repository address (recommended)
    > http://172.26.57.49:8849/download/updatePlugins.xml
  - Search for the VisualDDD plugin

2. Log in to the platform account
   > Tips: Use the same account as the platform.

3. Associate platform configurations
   > Associate and bind the current project with the platform application.

4. Generate code
  - Click refresh to fetch the latest application information, associated business domains, and scenarios.
  - Support custom project information (does not affect the platform data, only affects code generation)
    - Unique identifier: default application identifier, can be overwritten as the **project name**
    - Package path: default application package path, can be overwritten as the **project package structure**
    - Version number: default application version number, can be overwritten as the **project version number**
  - Support selecting the business domains and scenarios to generate.
  - Support custom output path
    - Default: generate to the current project
    - To include the generated code in the current project, output to the parent directory of the current project.
  - Support generating project framework
<!-- Plugin description end -->