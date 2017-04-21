package cn.yunyichina.log.service.collectorservice.service;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import cn.yunyichina.log.service.collectorservice.factory.CollectorServiceJedisFactory;
import cn.yunyichina.log.service.common.entity.dto.RedisProxyIndexDTO;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/20 9:33
 * @Description:
 */
@Service
public class IndexService {
    private final Logger logger = LoggerFactory.getLogger(IndexService.class);

    final String REDIS_KEY_CTX_INFO = "-ctx-";
    final String REDIS_KEY_KEYWORD = "-kw-";
    final String REDIS_KV_KEY = "-key-";
    final String REDIS_KV_VAL = "-val-";

    @Autowired
    CollectorServiceJedisFactory jedisFactory;

    @Async
    public void cacheIndex(RedisProxyIndexDTO redisProxyIndexDTO) {
        try (
                Jedis jedis = jedisFactory.getJedis(redisProxyIndexDTO.getCollectedItem());
        ) {
            logger.info("开始缓存索引到redis");
            Pipeline pipeline = jedis.pipelined();
            saveContextInfo(pipeline, redisProxyIndexDTO);
            saveKeywordIndex(pipeline, redisProxyIndexDTO);
            saveKvIndex(pipeline, redisProxyIndexDTO);
            pipeline.sync();
            logger.info("缓存完毕");
        }
    }

    private void saveKvIndex(Pipeline pipeline, RedisProxyIndexDTO redisProxyIndexDTO) {
        CollectedItemDO collectedItem = redisProxyIndexDTO.getCollectedItem();
        Integer collectedItemId = collectedItem.getId();
        ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> map0 = redisProxyIndexDTO.getKvIndexMap();//由于数据结构比较复杂,命名只能以0,1结尾
        if (map0 != null) {
            String keyStr;//key,e.g: "username"
            String valueStr;//value,e.g:"123"
            Set<Map.Entry<String, ConcurrentHashMap<String, Set<KvIndex>>>> entrySet0 = map0.entrySet();
            if (CollectionUtils.isNotEmpty(entrySet0)) {
                //key:Key  value:Map<Value,Set<ContextId>>
                for (Map.Entry<String, ConcurrentHashMap<String, Set<KvIndex>>> entry0 : entrySet0) {
                    ConcurrentHashMap<String, Set<KvIndex>> map1 = entry0.getValue();
                    if (map1 != null) {
                        Set<Map.Entry<String, Set<KvIndex>>> entrySet1 = map1.entrySet();
                        keyStr = entry0.getKey();
                        for (Map.Entry<String, Set<KvIndex>> entry1 : entrySet1) {
                            valueStr = entry1.getKey();
                            pipeline.set(collectedItemId + REDIS_KV_KEY + keyStr, valueStr);
                            Set<KvIndex> kvIndexSet = entry1.getValue();
                            if (CollectionUtils.isNotEmpty(kvIndexSet)) {
                                String[] kvIndexJsons = new String[kvIndexSet.size()];
                                int i = 0;
                                for (KvIndex kvIndex : kvIndexSet) {
                                    kvIndexJsons[i] = JSON.toJSONString(kvIndex);
                                    i++;
                                }
                                pipeline.sadd(collectedItemId + REDIS_KV_VAL + valueStr, kvIndexJsons);
                            }
                        }
                    }
                }
            }
        }
    }

    private void saveKeywordIndex(Pipeline pipeline, RedisProxyIndexDTO redisProxyIndexDTO) {
        CollectedItemDO collectedItem = redisProxyIndexDTO.getCollectedItem();
        Integer collectedItemId = collectedItem.getId();
        ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = redisProxyIndexDTO.getKeywordIndexMap();
        if (keywordIndexMap != null) {
            Set<Map.Entry<String, Set<KeywordIndex>>> keywordIndexEntrySet = keywordIndexMap.entrySet();
            if (CollectionUtils.isNotEmpty(keywordIndexEntrySet)) {
                for (Map.Entry<String, Set<KeywordIndex>> entry : keywordIndexEntrySet) {
                    Set<KeywordIndex> keywordIndexSet = entry.getValue();
                    if (CollectionUtils.isNotEmpty(keywordIndexSet)) {
                        int i = 0;
                        String[] keywordJsons = new String[keywordIndexSet.size()];
                        for (KeywordIndex keywordIndex : keywordIndexSet) {
                            keywordJsons[i] = JSON.toJSONString(keywordIndex);
                            i++;
                        }
                        pipeline.sadd(collectedItemId + REDIS_KEY_KEYWORD + entry.getKey(), keywordJsons);
                    }
                }
            }
        }
    }

    private void saveContextInfo(Pipeline pipeline, RedisProxyIndexDTO redisProxyIndexDTO) {
        ConcurrentHashMap<String, ContextInfo> contextInfoMap = redisProxyIndexDTO.getContextInfoMap();
        CollectedItemDO collectedItem = redisProxyIndexDTO.getCollectedItem();
        Integer collectedItemId = collectedItem.getId();
        if (contextInfoMap != null) {
            String contextId;
            String contextInfoJson;
            Set<Map.Entry<String, ContextInfo>> contextInfoEntrySet = contextInfoMap.entrySet();
            if (CollectionUtils.isNotEmpty(contextInfoEntrySet)) {
                for (Map.Entry<String, ContextInfo> entry : contextInfoEntrySet) {
                    contextId = entry.getKey();
                    contextInfoJson = JSON.toJSONString(entry.getValue());
                    pipeline.set(collectedItemId + REDIS_KEY_CTX_INFO + contextId, contextInfoJson);
                }
            }
        }
    }
}
