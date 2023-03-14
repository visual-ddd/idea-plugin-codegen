# idea-plugin-dev

![Build](https://github.com/Shimmernight/idea-plugin-dev/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [ ] Get familiar with the [template documentation][template].
- [ ] Verify the [pluginGroup](./gradle.properties), [plugin ID](./src/main/resources/META-INF/plugin.xml) and [sources package](./src/main/kotlin).
- [ ] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html?from=IJPluginTemplate).
- [ ] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the Plugin ID in the above README badges.
- [ ] Set the [Plugin Signing](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html?from=IJPluginTemplate) related [secrets](https://github.com/JetBrains/intellij-platform-plugin-template#environment-variables).
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html?from=IJPluginTemplate).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.

<!-- Plugin description -->
## 开发云 - VisualDDD（DDD可视化开发平台）
官网地址：https://ddd.wakedt.cn/login

### 环境准备
一、 配置开发云 host 访问环境
> Host: 172.31.13.178 ddd.wakedt.cn

二、 进入DDD可视化开发平台(https://ddd.wakedt.cn/login)创建项目，关联需要的业务域、业务场景

### 快速开始
一、 安装**VisualDDD**(DDD可视化开发平台)插件
- 打开IDEA设置面板，进入插件页面，点击通过本地安装插件

- 选择插件下载的安装包
- 完成安装后，菜单栏上多出一个DDD可视化平台按钮

二、 登录平台账号
> tips: 同平台账号

三、 关联平台配置
> 将当前项目同平台应用进行关联绑定

四、 生成代码
 
- 点击刷新，拉取最新的应用信息、关联业务域和业务场景信息
- 支持自定义项目信息（不影响平台数据，只对代码生成有效）
  - 唯一标识 -- 默认应用标识，可重写为**项目名称**
  - 包路径 -- 默认应用包路径，可重写为**项目包结构**
  - 版本号 -- 默认应用版本号，可重写为**项目版本号**
- 支持勾选需要生成的业务域、业务场景
- 支持自定义输出路径
  - 默认生成到当前项目下
  - 如果要将生成代码加入当前项目，则可以输出到当前项目的上一级目录下
- 支持生成项目框架
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "idea-plugin-dev"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/Shimmernight/idea-plugin-dev/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
