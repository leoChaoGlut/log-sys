# logSys
A distribute log system which is based on spring cloud &amp; docker

端口分布:<br>
--serviceCenter:<br>
----registry: 10100<br>
----configCenter: 10200<br>
----gateway: 10300<br>
--service:<br>
----api: 10400<br>
----searcherGateway: 10500<br>
----searcher: 10600<br>
----collector: 10700<br>
<br>
部署步骤:<br>
--注册中心:<br>
--配置中心(暂时不用):<br>
--入口网关:<br>
--api:<br>
--搜索网关:<br>
--搜索节点:<br>
----配置:<br>
------logRootDir: 存储日志的根目录<br>
--------正确示例: /data/logRootDir<br>
--------错误示例: /data/logRootDir/<br>
------indexRootDir: 索引根目录<br>
--------示例: 同上<br>
--采集结点:<br>
----配置:<br>
------url:<br>
--------logRoot: 要被采集的日志根目录<br>
--------tmpZip: 临时zip 解压,压缩目录<br>
--------gateway: 入口网关<br>
--------uploadServer: 上传到哪一台服务器<br>
--------tagSet: 获取索引tag<br>
------path:<br>
--------properties: 缓存配置<br>
