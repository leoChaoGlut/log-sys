package cn.yunyichina.log.service.collector.task;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.do_.KeywordTagDO;
import cn.yunyichina.log.common.entity.do_.KvTagDO;
import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.common.constant.IndexFormat;
import cn.yunyichina.log.component.common.constant.IndexType;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KvIndexBuilder;
import cn.yunyichina.log.component.index.entity.ContextIndex;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import cn.yunyichina.log.component.scanner.imp.LogScanner;
import cn.yunyichina.log.service.collector.cache.CollectedItemCache;
import cn.yunyichina.log.service.collector.service.CacheService;
import cn.yunyichina.log.service.collector.util.CacheUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/13 15:16
 * @Description:
 */
@Service
@RefreshScope
public class LogTask {
    private final Logger logger = LoggerFactory.getLogger(LogTask.class);
    private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");
    private final FastDateFormat cacheDateFormat = FastDateFormat.getInstance("yyyyMMddHHmm");
    private final String DOT = ".";

    @Value("${logTaskIntervalInMillis:60000}")
    private String fixedRate;

    @Autowired
    CacheService cacheService;

    private List<CollectedItemDO> collectedItemList = new ArrayList<>();

    /**
     * 在回调ApplicationReadyListener处理完成后手动回调
     */
    public void buildCollectedItemList() {
        CollectorDO collector = cacheService.getCollector();
        if (null != collector) {
            collectedItemList = collector.getCollectedItemList();
            if (null == collectedItemList) {
                collectedItemList = new ArrayList<>();
            }
        }
    }

    @Scheduled(fixedRateString = "${logTaskIntervalInMillis:60000}")
    public void execute() {
        if (CollectionUtils.isNotEmpty(collectedItemList)) {
            logger.info(JSON.toJSONString(collectedItemList));
            long begin = System.nanoTime();
            for (CollectedItemDO collectedItem : collectedItemList) {
                scanLogsAndBuildIndex(collectedItem);
            }
            logger.info("定时任务结束,总耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9));
        }
    }

    private void scanLogsAndBuildIndex(CollectedItemDO collectedItem) {
        try {
            Collection<File> logs = scanLastestLogs(collectedItem);
            logger.info("CollectedItemId:" + collectedItem.getId() + ", log size:" + logs.size());
            if (CollectionUtils.isNotEmpty(logs)) {
                buildIndexAndWriteToDisk(logs, collectedItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Collection<File> scanLastestLogs(CollectedItemDO collectedItem) throws Exception {
        String beginDatetimeStr = getLastModifyTimeStr(collectedItem.getId());
        String endDatetimeStr = dateFormat.format(new Date());

        if (StringUtils.isBlank(beginDatetimeStr)) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MINUTE, -5);
//            long fixedRateAgo = now.getTime() - Long.valueOf(fixedRate);
//            TODO Test
//            beginDatetimeStr = dateFormat.format(new Date(0));
            beginDatetimeStr = dateFormat.format(c.getTime());
        }
        String collectedLogDir = collectedItem.getCollectedLogDir();
//        logger.info(beginDatetimeStr + " - " + endDatetimeStr + " - " + collectedLogDir);
        Map<String, File> logMap = LogScanner.scan(beginDatetimeStr, endDatetimeStr, collectedLogDir);
        return logMap.values();
    }

    private String getLastModifyTimeStr(Integer collectedItemId) throws Exception {
        CollectedItemCache collectedItemCache = CacheUtil.readCollectedItemCache(collectedItemId);
        if (collectedItemCache == null) {
            return null;
        } else {
            return collectedItemCache.getLastModifyDatetimeStr();
        }
    }

    private void buildIndexAndWriteToDisk(Collection<File> logs, CollectedItemDO collectedItem) throws Exception {
        Map<String, ConcurrentHashMap<String, ContextInfo>> contextInfoCacheMap = buildContextIndexCache(logs);
        Map<String, ConcurrentHashMap<String, Set<KeywordIndex>>> keywordIndexCacheMap = buildKeywordIndexCache(logs, collectedItem);
        Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>>> kvIndexCacheMap = buildKvIndexCache(logs, collectedItem);

        Integer collectedItemId = collectedItem.getId();
        writeContextIndexToDisk(contextInfoCacheMap, collectedItemId);
        writeKeywordIndexToDisk(keywordIndexCacheMap, collectedItemId);
        writeKvIndexToDisk(kvIndexCacheMap, collectedItemId);

        recordLastestModifyTime(collectedItemId);
    }

    public void recordLastestModifyTime(Integer collectedItemId) throws Exception {
        String lastModifyTime = dateFormat.format(new Date());
        CollectedItemCache collectedItemCache = CacheUtil.readCollectedItemCache(collectedItemId);
        if (collectedItemCache == null) {
            collectedItemCache = new CollectedItemCache();
        }
        collectedItemCache.setLastModifyDatetimeStr(lastModifyTime);
        CacheUtil.writeCollectedItemCache(collectedItemId, collectedItemCache);
    }

    public Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>>> buildKvIndexCache(Collection<File> logs, CollectedItemDO collectedItem) {
        Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>>> kvIndexTimeCacheMap = new HashMap<>();
        List<KvTagDO> kvTagList = collectedItem.getKvTagList();
        if (CollectionUtils.isEmpty(kvTagList)) {
            return new HashMap<>();
        } else {
            Set<KvTagDO> kvTagSet = new HashSet<>(kvTagList);
            for (File log : logs) {
                String logName = log.getName().substring(0, log.getName().indexOf("."));
                ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> keyValueIndexMap = new KvIndexBuilder(log, kvTagSet).build();
                kvIndexTimeCacheMap.put(logName, keyValueIndexMap);
            }
            return kvIndexTimeCacheMap;
        }
    }

    public Map<String, ConcurrentHashMap<String, Set<KeywordIndex>>> buildKeywordIndexCache(Collection<File> logs, CollectedItemDO collectedItem) {
        Map<String, ConcurrentHashMap<String, Set<KeywordIndex>>> keyWordIndexTimeCacheMap = new HashMap<>();
        List<KeywordTagDO> keywordTagList = collectedItem.getKeywordTagList();
        if (CollectionUtils.isEmpty(keywordTagList)) {
            return new HashMap<>();
        } else {
            Set<String> keywordSet = new HashSet<>(keywordTagList.size());
            for (KeywordTagDO keywordTag : keywordTagList) {
                keywordSet.add(keywordTag.getKeyword());
            }
            for (File log : logs) {
                String logName = log.getName().substring(0, log.getName().indexOf("."));
                ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = new KeywordIndexBuilder(log, keywordSet).build();
                keyWordIndexTimeCacheMap.put(logName, keywordIndexMap);
            }
            return keyWordIndexTimeCacheMap;
        }
    }

    public Map<String, ConcurrentHashMap<String, ContextInfo>> buildContextIndexCache(Collection<File> logs) throws Exception {
        Map<String, ConcurrentHashMap<String, ContextInfo>> contextInfoCacheMap = new HashMap<>();
        for (File log : logs) {
            ConcurrentHashMap<String, ContextInfo> contextInfoMap = new ContextIndexBuilder(log).build();
            Set<Map.Entry<String, ContextInfo>> entrySet = contextInfoMap.entrySet();
            for (Map.Entry<String, ContextInfo> entry : entrySet) {
                ContextInfo contextInfo = entry.getValue();
                if (contextInfo != null) {
                    ContextIndex begin = contextInfo.getBegin();
                    ContextIndex end = contextInfo.getEnd();
                    if (begin != null && end != null) {
                        String beginLogFileName = begin.getLogFile().getName();
                        String beginDatetimeStr = beginLogFileName.substring(0, beginLogFileName.indexOf(DOT));
                        if (Objects.equals(begin, end)) {
                            contextInfoCacheMap.put(beginDatetimeStr, contextInfoMap);
                        } else {
                            Date beginDate = cacheDateFormat.parse(beginDatetimeStr);

                            String endLogFileName = end.getLogFile().getName();
                            String endDatetimeStr = endLogFileName.substring(0, endLogFileName.indexOf(DOT));
                            Date endDate = cacheDateFormat.parse(endDatetimeStr);

                            long intervalOfMinutes = (endDate.getTime() - beginDate.getTime()) / 1000 / 60;
                            Calendar c = Calendar.getInstance();
                            for (int minute = 0; minute <= intervalOfMinutes; minute++) {
                                c.clear();
                                c.setTime(beginDate);
                                c.add(Calendar.MINUTE, minute);
                                String datetimeStr = cacheDateFormat.format(c.getTime());
                                ConcurrentHashMap<String, ContextInfo> cacheMap = contextInfoCacheMap.get(datetimeStr);
                                if (cacheMap == null) {
                                    contextInfoCacheMap.put(datetimeStr, contextInfoMap);
                                } else {
                                    ContextIndexAggregator aggregator = new ContextIndexAggregator();
                                    aggregator.aggregate(cacheMap);
                                    aggregator.aggregate(contextInfoMap);
                                    contextInfoCacheMap.put(datetimeStr, aggregator.getAggregatedCollection());
                                }
                            }
                        }
                    }
                }
            }

        }
        return contextInfoCacheMap;
    }

    public void writeContextIndexToDisk(Map<String, ConcurrentHashMap<String, ContextInfo>> contextInfoMap, Integer collectedItemId) throws Exception {
        for (Map.Entry<String, ConcurrentHashMap<String, ContextInfo>> entry : contextInfoMap.entrySet()) {
            String indexFileName = entry.getKey();
            ConcurrentHashMap<String, ContextInfo> contextInfoCacheMap = entry.getValue();
            String cacheFilePath = buildIndexCacheFilePath(collectedItemId, indexFileName, IndexType.CONTEXT, IndexFormat.CONTEXT);
            CacheUtil.writeIndexCache(JSON.toJSONBytes(contextInfoCacheMap), cacheFilePath);
        }
    }

    public void writeKeywordIndexToDisk(Map<String, ConcurrentHashMap<String, Set<KeywordIndex>>> keywordIndexMap, Integer collectedItemId) throws Exception {
        for (Map.Entry<String, ConcurrentHashMap<String, Set<KeywordIndex>>> entry : keywordIndexMap.entrySet()) {
            String indexFileName = entry.getKey();
            ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexCacheMap = entry.getValue();
            String cacheFilePath = buildIndexCacheFilePath(collectedItemId, indexFileName, IndexType.KEYWORD, IndexFormat.KEYWORD);
            CacheUtil.writeIndexCache(JSON.toJSONBytes(keywordIndexCacheMap), cacheFilePath);
        }
    }

    public void writeKvIndexToDisk(Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>>> kvIndexMap, Integer collectedItemId) throws Exception {
        for (Map.Entry<String, ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>>> entry : kvIndexMap.entrySet()) {
            String indexFileName = entry.getKey();
            ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexCacheMap = entry.getValue();
            String cacheFilePath = buildIndexCacheFilePath(collectedItemId, indexFileName, IndexType.KEY_VALUE, IndexFormat.KEY_VALUE);
            CacheUtil.writeIndexCache(JSON.toJSONBytes(kvIndexCacheMap), cacheFilePath);
        }
    }

    private String buildIndexCacheFilePath(Integer collectedItemId, String indexFileName, IndexType indexType, IndexFormat indexFormat) {
        return collectedItemId + File.separator + indexType.getVal() + File.separator + indexFileName.substring(0, 4) + File.separator + indexFileName.substring(4, 6)
                + File.separator + indexFileName.substring(6, 8) + File.separator + indexFileName.substring(8, 10)
                + File.separator + indexFileName.substring(10, 12) + File.separator + indexFileName + indexFormat.getVal();
    }

}
