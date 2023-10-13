# VisualDDD

![Build](https://github.com/Shimmernight/idea-plugin-dev/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
## 开发云 - VisualDDD（DDD可视化开发平台）
官网地址：https://ddd-test.wakedt.cn/login

### 环境准备
一、 配置开发云 host 访问环境
> Host: 172.31.13.178 ddd-test.wakedt.cn

二、 进入DDD可视化开发平台(https://ddd-test.wakedt.cn/login)创建项目，关联需要的业务域、业务场景

### 快速开始
一、 安装**VisualDDD**(DDD可视化开发平台)插件
1. 设置私服地址（推荐）

- 添加插件私服地址
> http://172.26.57.49:8849/download/updatePlugins.xml
- 搜索插件VisualDDD

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