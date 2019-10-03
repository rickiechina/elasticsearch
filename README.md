# Elasticsearch 7.* 和 Spring Boot 2.* 集成项目 

## 第一个项目：演示Elasticsearch 6.4.3 和Spring Boot 2.1.7集成

基于spring-data-elasticsearch 组件，实现和Spring Boot 2.1.7的集成。

spring-data-elasticsearch 是 Spring Data 的 Community modules 之一，是 Spring Data 对 Elasticsearch 引擎的实现。Elasticsearch 默认提供轻量级的 HTTP Restful 接口形式的访问。相对来说，使用 HTTP Client 调用也很简单。但 spring-data-elasticsearch 可以更快的支持构建在 Spring 应用上，比如在 application.properties 配置 ES 节点信息和 spring-boot-starter-data-elasticsearch 依赖，直接在 Spring Boot 应用上使用。

## 第二个项目：rest-client-demo

使用Java High Level REST Client操作elasticsearch。

现在Elasticsearch 官方推出Java High Level REST Client，它是基于Java Low Level REST Client的封装，并且API接收参数和返回值和TransportClient是一样的，使得代码迁移变得容易并且支持了RESTful的风格，兼容了这两种客户端的优点。当然缺点是存在的，就是版本的问题。ES的小版本更新非常频繁，在最理想的情况下，客户端的版本要和ES的版本一致（至少主版本号一致），次版本号不一致的话，基本操作也许可以，但是新API就不支持了。

## 第三个项目：transport-client-demo

使用Java TransportClient 操作Elasticsearch（Spring Boot/Maven）。

本文演示通过Transport Client来操作Elasticsearch。TransportClient 是一种轻量级的方法，它通过Socket与Elasticsearch集群连接，是基于Netty 线程池的方式。

完整的技术文档，可以参考如下链接：

https://learning.snssdk.com/feoffline/toutiao_wallet_bundles/toutiao_learning_wap/online/album_detail.html?content_id=6733933729732886796

![](https://raw.githubusercontent.com/rickiechina/elasticsearch-order/master/images/%E3%80%8AElasticsearch%207.x%E4%BB%8E%E5%85%A5%E9%97%A8%E5%88%B0%E7%B2%BE%E9%80%9A%E3%80%8B%E4%B8%93%E6%A0%8F%E6%B5%B7%E6%8A%A51.png)