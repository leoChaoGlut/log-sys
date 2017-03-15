
======= log-sys project structure ========= 

- log-common 
- log-component      -> 组件
--- log-component-aggregator      -> 聚合器,主要功能有:['索引聚合','日志文件聚合']
--- log-component-index           -> 基础索引数据结构,共有3种:['ContextIndex','KeywordIndex','keyValueIndex']
--- log-component-scanner         -> 扫描器,主要用于扫描目录结构为'/YYYY/MM/DD/HH/mm/YYYYMMDDHHmm.log'的日志文件
--- log-component-search-engine   -> 搜索引擎,通过给定的入参,来搜索基础索引数据结构.
- log-front-end      -> 前端代码
- log-resource       -> sql,shell,test-resource等
- log-service        -> 所有基础服务
--- log-service-api               -> API,暂时没有用到
--- log-service-collector         -> 采集结点,一个采集节点可采集多个采集项.功能['索引构建','WebSocket实时日志','Http历史日志']
--- log-service-collector-service -> 对采集结点提供服务,主要功能:['配置获取','节点注册'].
--- log-service-tracer            -> 分布式应用日志追踪器,暂未开发.
- log-service-center -> 服务中心
--- log-service-center-config-center -> 配置中心
--- log-service-center-gateway       -> 统一入口网关
--- log-service-center-registry      -> 注册中心
