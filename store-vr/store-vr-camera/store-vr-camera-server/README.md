# 文档

以下信息为当前项目的环境信息，目前包括：配置中心、数据库、监控、API文档，你也可以将项目依赖放进来。

项目相关说明和参考文档的入口也可以放在这里。

## 配置中心

test/off/production的配置都放在配置中心里，具体请参考文档：[Apollo配置中心接入](http://doc.shtest.ke.com/infrastructure/spring-cloud/config.html)。

要管理配置，可访问管理后台地址。

如果你没有权限，请联系项目负责人分配。

## 数据库

数据库信息请参考 [DATABASE.md](docs/db/DATABASE.md)。

如果数据库有调整，建议及时更新。

## 监控指标

打开监控页面，命名空间(namespace)是应用所属分组，根据分组筛选自己的应用。

应用名通常和spring.application.name是一致的。

### 应用大盘

测试(test)环境：[JAVA-APP-V2](http://jk.shtest.ke.com/d/jvm-job/java-app-v2)

集成(off)环境：[JAVA-APP-V2](http://jk.shoff.ke.com/d/jvm-job/java-app-v2)

生产(prod)环境：[JAVA-APP-V2](http://jk.ke.com/d/jvm-job/java-app-v2)

应用大盘快捷入口：

![快捷入口](docs/images/metrics-dog-java-app-v2.png)


### 应用节点

测试(test)环境：[JAVA-INSTANCE-V2](http://jk.shtest.ke.com/d/jvm-job-node/java-instance-v2)

集成(off)环境：[JAVA-INSTANCE-V2](http://jk.shoff.ke.com/d/jvm-job-node/java-instance-v2)

生产(prod)环境：[JAVA-INSTANCE-V2](http://jk.ke.com/d/jvm-job-node/java-instance-v2)


## API文档

API文档使用[API文档自动化插件](http://doc.shtest.ke.com/common-knowledge/api/doc-auto.html)自动生成，访问地址：[http://doc.shoff.ke.com](http://doc.shoff.ke.com)

本项目文档首页为：http://doc.shoff.ke.com/v1/doc/{{spring.application.name}}

将变量`{{spring.application.name}}`改为真实的应用标识即可。

