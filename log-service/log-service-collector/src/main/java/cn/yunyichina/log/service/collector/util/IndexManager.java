package cn.yunyichina.log.service.collector.util;

import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import cn.yunyichina.log.service.collector.cache.CollectedItemCache;
import cn.yunyichina.log.service.collector.constants.CacheName;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/3 18:28
 * @Description:
 */
@Component
public class IndexManager {

    /**
     * key: collected item id
     * value : CollectedItemCache
     */
    private Map<Integer, CollectedItemCache> collectedItemCacheMap = new HashMap<>();

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
    private ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();

    public void setContextIndexBy(Integer collectedItemId, ConcurrentHashMap<Long, ContextInfo> contextInfoMap) {
        CollectedItemCache collectedItemCache = collectedItemCacheMap.get(collectedItemId);
        if (collectedItemCache == null) {
            collectedItemCache = new CollectedItemCache()
                    .setContextInfoMap(contextInfoMap);

            collectedItemCacheMap.put(collectedItemId, collectedItemCache);
        } else {
            collectedItemCache.setContextInfoMap(contextInfoMap);
        }
    }

    public void setKeywordIndexBy(Integer collectedItemId, ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap) {
        CollectedItemCache collectedItemCache = collectedItemCacheMap.get(collectedItemId);
        if (collectedItemCache == null) {
            collectedItemCache = new CollectedItemCache()
                    .setKeywordIndexMap(keywordIndexMap);

            collectedItemCacheMap.put(collectedItemId, collectedItemCache);
        } else {
            collectedItemCache.setKeywordIndexMap(keywordIndexMap);
        }
    }

    public void setKvIndexBy(Integer collectedItemId, ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap) {
        CollectedItemCache collectedItemCache = collectedItemCacheMap.get(collectedItemId);
        if (collectedItemCache == null) {
            collectedItemCache = new CollectedItemCache()
                    .setKvIndexMap(kvIndexMap);

            collectedItemCacheMap.put(collectedItemId, collectedItemCache);
        } else {
            collectedItemCache.setKvIndexMap(kvIndexMap);
        }
    }

    public ConcurrentHashMap<Long, ContextInfo> getContextIndexBy(Integer collectedItemId) throws Exception {
        writeLock.lock();
        try {
            CollectedItemCache collectedItemCache = collectedItemCacheMap.get(collectedItemId);
            if (collectedItemCache == null) {
                collectedItemCache = new CollectedItemCache();

                CollectedItemCache.BaseInfo baseInfo = CacheUtil.read(collectedItemId, CacheName.COLLECTED_ITEM_BASE_INFO);
                ConcurrentHashMap<Long, ContextInfo> contextIndexMap = CacheUtil.read(collectedItemId, CacheName.CONTEXT_INDEX);

                collectedItemCache.setBaseInfo(baseInfo);
                collectedItemCache.setContextInfoMap(contextIndexMap);

                collectedItemCacheMap.put(collectedItemId, collectedItemCache);

                return contextIndexMap;
            } else {
                ConcurrentHashMap<Long, ContextInfo> contextInfoMap = collectedItemCache.getContextInfoMap();
                if (contextInfoMap == null) {
                    contextInfoMap = CacheUtil.read(collectedItemId, CacheName.CONTEXT_INDEX);
                    collectedItemCache.setContextInfoMap(contextInfoMap);
                    return contextInfoMap;
                } else {
                    return contextInfoMap;
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    public ConcurrentHashMap<String, Set<KeywordIndex>> getKeywordIndexBy(Integer collectedItemId) throws Exception {
        writeLock.lock();
        try {
            CollectedItemCache collectedItemCache = collectedItemCacheMap.get(collectedItemId);
            if (collectedItemCache == null) {
                collectedItemCache = new CollectedItemCache();

                CollectedItemCache.BaseInfo baseInfo = CacheUtil.read(collectedItemId, CacheName.COLLECTED_ITEM_BASE_INFO);
                ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = CacheUtil.read(collectedItemId, CacheName.KEYWORD_INDEX);

                collectedItemCache.setBaseInfo(baseInfo);
                collectedItemCache.setKeywordIndexMap(keywordIndexMap);

                collectedItemCacheMap.put(collectedItemId, collectedItemCache);

                return keywordIndexMap;
            } else {
                ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = collectedItemCache.getKeywordIndexMap();
                if (keywordIndexMap == null) {
                    keywordIndexMap = CacheUtil.read(collectedItemId, CacheName.KEYWORD_INDEX);
                    collectedItemCache.setKeywordIndexMap(keywordIndexMap);
                    return keywordIndexMap;
                } else {
                    return keywordIndexMap;
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> getKvIndexBy(Integer collectedItemId) throws Exception {
        writeLock.lock();
        try {
            CollectedItemCache collectedItemCache = collectedItemCacheMap.get(collectedItemId);
            if (collectedItemCache == null) {
                collectedItemCache = new CollectedItemCache();

                CollectedItemCache.BaseInfo baseInfo = CacheUtil.read(collectedItemId, CacheName.COLLECTED_ITEM_BASE_INFO);
                ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap = CacheUtil.read(collectedItemId, CacheName.KV_INDEX);

                collectedItemCache.setBaseInfo(baseInfo);
                collectedItemCache.setKvIndexMap(kvIndexMap);

                collectedItemCacheMap.put(collectedItemId, collectedItemCache);

                return kvIndexMap;
            } else {
                ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap = collectedItemCache.getKvIndexMap();
                if (kvIndexMap == null) {
                    kvIndexMap = CacheUtil.read(collectedItemId, CacheName.KV_INDEX);
                    collectedItemCache.setKvIndexMap(kvIndexMap);
                    return kvIndexMap;
                } else {
                    return kvIndexMap;
                }
            }
        } finally {
            writeLock.unlock();
        }
    }
}
