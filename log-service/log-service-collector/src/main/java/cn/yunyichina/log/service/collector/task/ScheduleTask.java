package cn.yunyichina.log.service.collector.task;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.do_.KeywordTagDO;
import cn.yunyichina.log.common.entity.do_.KvTagDO;
import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KvIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KvIndexBuilder;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import cn.yunyichina.log.component.scanner.imp.LogScanner;
import cn.yunyichina.log.service.collector.cache.CollectedItemCache;
import cn.yunyichina.log.service.collector.constants.CacheName;
import cn.yunyichina.log.service.collector.service.CacheService;
import cn.yunyichina.log.service.collector.util.CacheUtil;
import cn.yunyichina.log.service.collector.util.IndexManager;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/5 14:18
 * @Description:
 */
@Service
public class ScheduleTask {
    final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");

    private boolean hasInit = false;

    @Autowired
    IndexManager indexManager;

    @Value("${fixedRate}")
    private String fixedRate;

    @Autowired
    CacheService cacheService;

    private List<CollectedItemDO> collectedItemList = new ArrayList<>();

    /**
     * 在回调ApplicationReadyListener处理完成后手动回调
     */
    public void initCollectedItemList() {
        if (hasInit) {

        } else {
            CollectorDO collector = cacheService.getCollector();
            logger.info(collector.toString());
            if (collector == null) {

            } else {
                collectedItemList = collector.getCollectedItemList();
                if (null == collectedItemList) {
                    collectedItemList = new ArrayList<>();
                }
            }
            hasInit = true;
        }
    }

    @Scheduled(fixedRateString = "${fixedRate}")
    public void execute() {
        logger.info(JSON.toJSONString(collectedItemList, true));
        if (hasInit) {
            for (CollectedItemDO collectedItem : collectedItemList) {
                scanLogsAndBuildIndex(collectedItem);
            }
        }
    }

    private void scanLogsAndBuildIndex(CollectedItemDO collectedItem) {
        try {
            Collection<File> logs = scanLastestLogs(collectedItem);
//            logger.info(JSON.toJSONString(logs, true));
            if (CollectionUtils.isEmpty(logs)) {

            } else {
                buildIndexAndWriteToDisk(logs, collectedItem);
                recordLastestModifyTime(collectedItem.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Collection<File> scanLastestLogs(CollectedItemDO collectedItem) throws Exception {
        Date now = new Date();
        String beginDatetime = getLastModifyTime(collectedItem.getId());
        String endDatetime = dateFormat.format(now);

        if (StringUtils.isBlank(beginDatetime)) {
            long fixedRateAgo = now.getTime() - Long.valueOf(fixedRate);
            beginDatetime = dateFormat.format(new Date(fixedRateAgo));
        }
        logger.info(beginDatetime + " - " + endDatetime);
        Map<String, File> logMap = LogScanner.scan(beginDatetime, endDatetime, collectedItem.getCollectedLogDir());
        return logMap.values();
    }

    private String getLastModifyTime(Integer collectedItemId) throws Exception {
        CollectedItemCache.BaseInfo baseInfo = CacheUtil.read(collectedItemId, CacheName.COLLECTED_ITEM_BASE_INFO);
        if (baseInfo == null) {
            return null;
        } else {
            return baseInfo.getLastModifyTimeStr();
        }
    }

    private void buildIndexAndWriteToDisk(Collection<File> logs, CollectedItemDO collectedItem) throws Exception {
        Integer collectedItemId = collectedItem.getId();

        ConcurrentHashMap<Long, ContextInfo> contextInfoMap = buildContextIndexBy(logs);
        ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = buildKeywordIndexBy(logs, collectedItem);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap = buildKvIndexBy(logs, collectedItem);

        recordLastestContextCount(collectedItemId, contextInfoMap);

        cacheContextIndex(contextInfoMap, collectedItemId);
        cacheKeywordIndex(keywordIndexMap, collectedItemId);
        cacheKvIndex(kvIndexMap, collectedItemId);
    }

    private void recordLastestModifyTime(Integer collectedItemId) throws Exception {
        String lastModifyTime = dateFormat.format(new Date());
        CollectedItemCache.BaseInfo baseInfo = CacheUtil.read(collectedItemId, CacheName.COLLECTED_ITEM_BASE_INFO);
        if (baseInfo == null) {
            baseInfo = new CollectedItemCache.BaseInfo();
        }
        baseInfo.setLastModifyTimeStr(lastModifyTime);
        CacheUtil.write(baseInfo, collectedItemId, CacheName.COLLECTED_ITEM_BASE_INFO);
    }

    /**
     * TODO 好像没啥用?
     *
     * @param collectedItemId
     * @param contextInfoMap
     * @throws Exception
     */
    private void recordLastestContextCount(Integer collectedItemId, Map<Long, ContextInfo> contextInfoMap) throws Exception {
        Set<Long> countSet = contextInfoMap.keySet();
        Long lastestCount = Collections.max(countSet);
        CollectedItemCache.BaseInfo baseInfo = CacheUtil.read(collectedItemId, CacheName.COLLECTED_ITEM_BASE_INFO);
        if (baseInfo == null) {
            baseInfo = new CollectedItemCache.BaseInfo();
        }
        baseInfo.setContextCount(lastestCount);
        CacheUtil.write(baseInfo, collectedItemId, CacheName.COLLECTED_ITEM_BASE_INFO);
    }

    private ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> buildKvIndexBy(Collection<File> logs, CollectedItemDO collectedItem) {
        KvIndexAggregator aggregator = new KvIndexAggregator();
        List<KvTagDO> kvTagList = collectedItem.getKvTagList();
        if (CollectionUtils.isEmpty(kvTagList)) {
            return null;
        } else {
            Set<KvTagDO> kvTagSet = new HashSet<>(kvTagList);
            for (File log : logs) {
                ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> keyValueIndexMap = new KvIndexBuilder(log, kvTagSet).build();
                aggregator.aggregate(keyValueIndexMap);
            }
            return aggregator.getAggregatedCollection();
        }
    }

    private ConcurrentHashMap<String, Set<KeywordIndex>> buildKeywordIndexBy(Collection<File> logs, CollectedItemDO collectedItem) {
        KeywordIndexAggregator aggregator = new KeywordIndexAggregator();
        List<KeywordTagDO> keywordTagList = collectedItem.getKeywordTagList();
        if (CollectionUtils.isEmpty(keywordTagList)) {
            return null;
        } else {
            Set<String> keywordSet = new HashSet<>(keywordTagList.size());
            for (KeywordTagDO keywordTag : keywordTagList) {
                keywordSet.add(keywordTag.getKeyword());
            }
            for (File log : logs) {
                ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = new KeywordIndexBuilder(log, keywordSet).build();
                aggregator.aggregate(keywordIndexMap);
            }
            return aggregator.getAggregatedCollection();
        }
    }

    private ConcurrentHashMap<Long, ContextInfo> buildContextIndexBy(Collection<File> logs) {
        ContextIndexAggregator aggregator = new ContextIndexAggregator();
        for (File log : logs) {
            ConcurrentHashMap<Long, ContextInfo> contextInfoMap = new ContextIndexBuilder(log).build();
            aggregator.aggregate(contextInfoMap);
        }
        return aggregator.getAggregatedCollection();
    }

    public void cacheContextIndex(ConcurrentHashMap<Long, ContextInfo> contextInfoMap, Integer collectedItemId) throws Exception {
        ConcurrentHashMap<Long, ContextInfo> contextInfoCacheMap = indexManager.getContextIndexBy(collectedItemId);
        if (contextInfoCacheMap == null) {
            CacheUtil.write(contextInfoMap, collectedItemId, CacheName.CONTEXT_INDEX);
        } else {
            ContextIndexAggregator aggregator = new ContextIndexAggregator();
            aggregator.aggregate(contextInfoMap);
            aggregator.aggregate(contextInfoCacheMap);

            ConcurrentHashMap<Long, ContextInfo> aggregatedContextInfoMap = aggregator.getAggregatedCollection();
            indexManager.setContextIndexBy(collectedItemId, aggregatedContextInfoMap);

            CacheUtil.write(aggregatedContextInfoMap, collectedItemId, CacheName.CONTEXT_INDEX);
        }
    }

    public void cacheKeywordIndex(ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap, Integer collectedItemId) throws Exception {
        ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexCacheMap = indexManager.getKeywordIndexBy(collectedItemId);
        if (keywordIndexCacheMap == null) {
            CacheUtil.write(keywordIndexMap, collectedItemId, CacheName.KEYWORD_INDEX);
        } else {
            KeywordIndexAggregator aggregator = new KeywordIndexAggregator();
            aggregator.aggregate(keywordIndexMap);
            aggregator.aggregate(keywordIndexCacheMap);

            ConcurrentHashMap<String, Set<KeywordIndex>> aggregatedKeywordIndexMap = aggregator.getAggregatedCollection();
            indexManager.setKeywordIndexBy(collectedItemId, aggregatedKeywordIndexMap);

            CacheUtil.write(aggregatedKeywordIndexMap, collectedItemId, CacheName.KEYWORD_INDEX);
        }
    }

    public void cacheKvIndex(ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap, Integer collectedItemId) throws Exception {
        ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexCacheMap = indexManager.getKvIndexBy(collectedItemId);
        if (kvIndexCacheMap == null) {
            CacheUtil.write(kvIndexMap, collectedItemId, CacheName.KV_INDEX);
        } else {
            KvIndexAggregator aggregator = new KvIndexAggregator();
            aggregator.aggregate(kvIndexMap);
            aggregator.aggregate(kvIndexCacheMap);

            ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> aggregatedKvIndexMap = aggregator.getAggregatedCollection();
            indexManager.setKvIndexBy(collectedItemId, kvIndexMap);

            CacheUtil.write(aggregatedKvIndexMap, collectedItemId, CacheName.KV_INDEX);
        }
    }


}
