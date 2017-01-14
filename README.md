# logSys
A distribute log system which is based on spring cloud &amp; docker

端口分布:
  serviceCenter:
    registry: 10100
    configCenter: 10200
    gateway: 10300
  service:
    api: 10400
    searcherGateway: 10500
    searcher: 10600
    collector: 10700

部署步骤:
  注册中心:
  配置中心(暂时不用):
  入口网关:
  api:
  搜索网关:
  搜索节点:
    配置:
      logRootDir: 存储日志的根目录
        正确示例: /data/logRootDir
        错误示例: /data/logRootDir/
      indexRootDir: 索引根目录
        示例: 同上
  采集结点:
    配置:
      url:
        logRoot: 要被采集的日志根目录
        tmpZip: 临时zip 解压,压缩目录
        gateway: 入口网关
        uploadServer: 上传到哪一台服务器
        tagSet: 获取索引tag
      path:
        properties: 缓存配置
