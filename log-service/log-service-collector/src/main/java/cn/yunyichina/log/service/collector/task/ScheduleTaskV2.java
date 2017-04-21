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
import cn.yunyichina.log.service.collector.client.CollectorServiceClient;
import cn.yunyichina.log.service.collector.constants.CacheName;
import cn.yunyichina.log.service.collector.service.CacheService;
import cn.yunyichina.log.service.collector.util.CacheUtil;
import cn.yunyichina.log.service.common.entity.dto.RedisProxyIndexDTO;
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
public class ScheduleTaskV2 {
    private final Logger logger = LoggerFactory.getLogger(ScheduleTaskV2.class);
    private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");

    @Value("${fixedRateInMillis:60000}")
    private String fixedRate;

    @Autowired
    CacheService cacheService;

    @Autowired
    CollectorServiceClient collectorServiceClient;

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

    @Scheduled(fixedRateString = "${fixedRateInMillis:60000}")
    public void execute() {
        logger.info(JSON.toJSONString(collectedItemList, true));
        if (CollectionUtils.isNotEmpty(collectedItemList)) {
            for (CollectedItemDO collectedItem : collectedItemList) {
                scanLogsAndBuildIndex(collectedItem);
            }
        }
    }

    private void scanLogsAndBuildIndex(CollectedItemDO collectedItem) {
        try {
            Collection<File> logs = scanLastestLogs(collectedItem);
            if (CollectionUtils.isNotEmpty(logs)) {
                buildIndexAndSendToRedisProxy(logs, collectedItem);
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
//            TODO Test
//            TODO Test
//            TODO Test
//            beginDatetime = dateFormat.format(new Date(0));
            beginDatetime = dateFormat.format(new Date(fixedRateAgo));
        }
        String collectedLogDir = collectedItem.getCollectedLogDir();
        logger.info(beginDatetime + " - " + endDatetime + " - " + collectedLogDir);
        Map<String, File> logMap = LogScanner.scan(beginDatetime, endDatetime, collectedLogDir);
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

    private void buildIndexAndSendToRedisProxy(Collection<File> logs, CollectedItemDO collectedItem) throws Exception {
        ConcurrentHashMap<String, ContextInfo> contextInfoMap = buildContextIndexBy(logs);
        ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = buildKeywordIndexBy(logs, collectedItem);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap = buildKvIndexBy(logs, collectedItem);

        RedisProxyIndexDTO redisProxyIndexDTO = new RedisProxyIndexDTO()
                .setCollectedItem(collectedItem)
                .setContextInfoMap(contextInfoMap)
                .setKeywordIndexMap(keywordIndexMap)
                .setKvIndexMap(kvIndexMap);

        collectorServiceClient.cacheIndex(redisProxyIndexDTO);
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

    private ConcurrentHashMap<String, ContextInfo> buildContextIndexBy(Collection<File> logs) {
        ContextIndexAggregator aggregator = new ContextIndexAggregator();
        for (File log : logs) {
            ConcurrentHashMap<String, ContextInfo> contextInfoMap = new ContextIndexBuilder(log).build();
            aggregator.aggregate(contextInfoMap);
        }
        return aggregator.getAggregatedCollection();
    }
}
