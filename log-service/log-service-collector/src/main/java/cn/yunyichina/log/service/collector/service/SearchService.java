package cn.yunyichina.log.service.collector.service;

import cn.yunyichina.log.common.ContextId;
import cn.yunyichina.log.common.constant.SearchEngineType;
import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.dto.SearchConditionDTO;
import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KvIndexAggregator;
import cn.yunyichina.log.component.aggregator.log.LogAggregator;
import cn.yunyichina.log.component.common.constant.IndexFormat;
import cn.yunyichina.log.component.common.constant.IndexType;
import cn.yunyichina.log.component.common.constant.IndexTypeReference;
import cn.yunyichina.log.component.filter.entity.FilterConditionDTO;
import cn.yunyichina.log.component.filter.imp.ContextFilter;
import cn.yunyichina.log.component.index.entity.ContextIndex;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import cn.yunyichina.log.component.scanner.imp.IndexCacheScanner;
import cn.yunyichina.log.component.scanner.imp.LogScanner;
import cn.yunyichina.log.component.searchengine.imp.KeywordSearchEngine;
import cn.yunyichina.log.component.searchengine.imp.KvSearchEngine;
import cn.yunyichina.log.component.searchengine.imp.NoIndexSearchEngine;
import cn.yunyichina.log.service.collector.exception.CollectorException;
import cn.yunyichina.log.service.collector.util.CacheUtil;
import cn.yunyichina.log.service.common.entity.dto.ContextInfoDTO;
import cn.yunyichina.log.service.common.entity.dto.LogRegion;
import cn.yunyichina.log.service.common.entity.dto.LogResultDTO;
import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/3 16:42
 * @Description:
 */
@Service
public class SearchService {

    public static final int DEFAULT_LOG_SIZE = 1024 * 1024;//1 M
    final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    CacheService cacheService;

    private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");

    public List<LogResultDTO> searchHistory(SearchConditionDTO condition) throws Exception {
        Integer collectedItemId = condition.getCollectedItemId();
        String beginDatetimeStr = dateFormat.format(condition.getBeginDateTime());
        String endDatetimeStr = dateFormat.format(condition.getEndDateTime());
        ConcurrentHashMap<String, ContextInfo> contextInfoMap = buildContextInfo(collectedItemId, beginDatetimeStr, endDatetimeStr);
        Set<ContextInfo> contextInfoSet;
        switch (condition.getSearchEngineType()) {
            case SearchEngineType.KEYWORD:
                contextInfoSet = useKeywordSearchEngine(condition, contextInfoMap, beginDatetimeStr, endDatetimeStr);
                break;
            case SearchEngineType.KEY_VALUE:
                contextInfoSet = useKvSearchEngine(condition, contextInfoMap, beginDatetimeStr, endDatetimeStr);
                break;
            case SearchEngineType.NO_INDEX:
                contextInfoSet = useNoIndexSearchEngine(condition, beginDatetimeStr, endDatetimeStr, contextInfoMap);
                break;
            default:
                throw new CollectorException("不支持的搜索引擎类型:" + condition.getSearchEngineType());
        }
        List<LogResultDTO> logResultList = buildLogResultListBy(collectedItemId, contextInfoSet);
        return logResultList;
    }

    public String searchByContextId(Integer collectedItemId, String contextId) throws Exception {
        CollectedItemDO collectedItem = cacheService.getCollectedItemBy(collectedItemId);
        if (collectedItem == null) {
            throw new CollectorException(collectedItemId + " 对应的采集项不存在");
        } else {
            long timestamp = ContextId.getTimestamp(contextId);
            Calendar begin = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            begin.setTimeInMillis(timestamp);
            end.setTimeInMillis(timestamp);
            begin.add(Calendar.MINUTE, -1);//在contextId生成的前后 1 分钟查询索引
            end.add(Calendar.MINUTE, 1);
            String beginDatetimeStr = dateFormat.format(begin);
            String endDatetimeStr = dateFormat.format(end);

            ConcurrentHashMap<String, ContextInfo> contextInfoMap = buildContextInfo(collectedItemId, beginDatetimeStr, endDatetimeStr);
            ContextInfo contextInfo = contextInfoMap.get(contextId);
            if (contextInfo == null) {
                throw new CollectorException("查询不到" + contextId + "的上下文");
            } else {
                String collectedLogDir = collectedItem.getCollectedLogDir();
                String contextContent = LogAggregator.aggregate(contextInfo, collectedLogDir);
                String filteredLogContent = new ContextFilter(
                        contextContent,
                        new FilterConditionDTO().setContextId(contextId)
                ).filter();
                return filteredLogContent;
            }
        }
    }

    public String readLogContentBy(ContextInfoDTO contextInfoDTO) throws IOException {
        TreeSet<String> absLogFilePathSet = contextInfoDTO.getAbsLogFilePathSet();
        int absLogFilePathSetSize = absLogFilePathSet.size();
        if (absLogFilePathSetSize == 1) {
            String logContent = Files.toString(new File(absLogFilePathSet.first()), StandardCharsets.UTF_8);
            logContent = logContent.substring(contextInfoDTO.getIndexOfBeginFile(), contextInfoDTO.getIndexOfEndFile());
            String filteredLogContent = new ContextFilter(
                    logContent,
                    new FilterConditionDTO().setContextId(contextInfoDTO.getContextId())
            ).filter();
            return filteredLogContent;
        } else if (absLogFilePathSetSize > 1) {
            int i = 0;
            StringBuilder logContentBuilder = new StringBuilder(DEFAULT_LOG_SIZE);
            for (String absLogFilePath : absLogFilePathSet) {
                String logContent = Files.toString(new File(absLogFilePath), StandardCharsets.UTF_8);
                if (i == 0) {
                    logContentBuilder.append(logContent.substring(contextInfoDTO.getIndexOfBeginFile()));
                } else if (i == absLogFilePathSetSize - 1) {
                    logContentBuilder.append(logContent.substring(0, contextInfoDTO.getIndexOfEndFile()));
                } else {
                    logContentBuilder.append(logContent);
                }
                i++;
            }
            String filteredLogContent = new ContextFilter(
                    logContentBuilder.toString(),
                    new FilterConditionDTO().setContextId(contextInfoDTO.getContextId())
            ).filter();
            return filteredLogContent;
        } else {
            throw new CollectorException("absLogFilePathSet is empty");
        }

    }

    private List<LogResultDTO> buildLogResultListBy(Integer collectedItemId, Set<ContextInfo> contextInfoSet) {
        if (CollectionUtils.isEmpty(contextInfoSet)) {
            throw new CollectorException("查不到符合条件的上下文");
        } else {
            CollectedItemDO collectedItem = cacheService.getCollectedItemBy(collectedItemId);
            if (collectedItem == null) {
                throw new CollectorException(collectedItemId + " 对应的采集项不存在");
            } else {
                List<LogResultDTO> logResultDTOList = new ArrayList<>(contextInfoSet.size());
                String collectedLogDir = collectedItem.getCollectedLogDir();
                long begin = System.nanoTime();
                logger.info("buildLogResult 开始");
                for (ContextInfo contextInfo : contextInfoSet) {
                    ContextIndex beginCtx = contextInfo.getBegin();
                    ContextIndex endCtx = contextInfo.getEnd();
                    if (beginCtx != null && endCtx != null) {
                        List<LogRegion> logRegionList = buildLogRegionBy(contextInfo, collectedLogDir);
                        ContextInfoDTO contextInfoDTO = buildContextInfoDTO(contextInfo, beginCtx, endCtx, logRegionList);
                        LogResultDTO logResultDTO = buildLogResultDTO(contextInfo, contextInfoDTO, logRegionList);
                        logResultDTOList.add(logResultDTO);
                    }
                }
                logger.info("buildLogResult 结束,耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9) + "秒");
                sortLogResultListByTimeDesc(logResultDTOList);
                return logResultDTOList;
            }
        }
    }


    /**
     * 按照上下文开始时间排序，用于前端显示
     *
     * @param logResultList
     */
    private void sortLogResultListByTimeDesc(List<LogResultDTO> logResultList) {
        long begin = System.nanoTime();
        logger.info("logResult 排序开始");
        Collections.sort(logResultList, new Comparator<LogResultDTO>() {
            @Override
            public int compare(LogResultDTO o1, LogResultDTO o2) {
                return o1.getLogRegionSet().first()
                        .compareTo(o2.getLogRegionSet().first());
            }
        });
        logger.info("logResult 排序结束,耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9) + "秒");
    }

    private LogResultDTO buildLogResultDTO(ContextInfo contextInfo, ContextInfoDTO contextInfoDTO, List<LogRegion> logRegionList) {
        TreeSet<String> logRegionSet = new TreeSet<>();
        for (LogRegion logRegion : logRegionList) {
            logRegionSet.add(logRegion.getDatetimeStr());
        }
        return new LogResultDTO()
                .setContextId(contextInfo.getContextId())
                .setContextInfoDTO(contextInfoDTO)
                .setLogRegionSet(logRegionSet);
    }

    private ContextInfoDTO buildContextInfoDTO(ContextInfo contextInfo, ContextIndex beginCtx, ContextIndex endCtx, List<LogRegion> logRegionList) {
        TreeSet<String> absLogFilePathSet = new TreeSet<>();
        for (LogRegion logRegion : logRegionList) {
            absLogFilePathSet.add(logRegion.getAbsFilePath());
        }
        return new ContextInfoDTO()
                .setContextId(contextInfo.getContextId())
                .setAbsLogFilePathSet(absLogFilePathSet)
                .setIndexOfBeginFile(beginCtx.getIndexOfLogFile())
                .setIndexOfEndFile(endCtx.getIndexOfLogFile());
    }

    /**
     * 获取上下文所贯穿的所有日志
     *
     * @param contextInfo
     * @param collectedLogDir
     * @return
     */
    private List<LogRegion> buildLogRegionBy(ContextInfo contextInfo, String collectedLogDir) {
        File beginLogFile = contextInfo.getBegin().getLogFile();
        File endLogFile = contextInfo.getEnd().getLogFile();
        Map<String, File> logMap = LogScanner.scan(beginLogFile, endLogFile, collectedLogDir);
        if (CollectionUtils.isEmpty(logMap)) {
            return new ArrayList<>();
        } else {
            Collection<File> logs = logMap.values();
            List<LogRegion> logRegionList = new ArrayList<>(logs.size());
            for (File log : logs) {
                String logName = log.getName();
                logName = logName.substring(0, 4) + "-" +
                        logName.substring(4, 6) + "-" +
                        logName.substring(6, 8) + " " +
                        logName.substring(8, 10) + ":" +
                        logName.substring(10, 12);
                LogRegion logRegion = new LogRegion()
                        .setAbsFilePath(log.getAbsolutePath())
                        .setDatetimeStr(logName);
                logRegionList.add(logRegion);
            }
            return logRegionList;
        }
    }

    private Set<ContextInfo> useNoIndexSearchEngine(SearchConditionDTO condition, String beginDatetimeStr, String endDatetimeStr, ConcurrentHashMap<String, ContextInfo> contextInfoMap) throws Exception {
        Integer collectedItemId = condition.getCollectedItemId();
        CollectedItemDO collectedItem = cacheService.getCollectedItemBy(collectedItemId);
        if (collectedItem == null) {
            throw new CollectorException(collectedItemId + " 对应的采集项不存在");
        } else {
            String collectedLogDir = collectedItem.getCollectedLogDir();
            Map<String, File> logMap = LogScanner.scan(beginDatetimeStr, endDatetimeStr, collectedLogDir);
            Set<ContextInfo> contextInfoSet = new NoIndexSearchEngine(
                    logMap.values(),
                    contextInfoMap,
                    condition.getNoIndexKeyword()
            ).search();
            return contextInfoSet;
        }
    }


    private Set<ContextInfo> useKvSearchEngine(SearchConditionDTO condition, ConcurrentHashMap<String, ContextInfo> contextInfoMap, String beginDatetimeStr, String endDatetiemStr) throws Exception {
        ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap = buildKvIndex(condition.getCollectedItemId(), beginDatetimeStr, endDatetiemStr);
        Set<ContextInfo> contextInfoSet = new KvSearchEngine(
                kvIndexMap,
                contextInfoMap,
                condition
        ).search();
        return contextInfoSet;
    }

    private Set<ContextInfo> useKeywordSearchEngine(SearchConditionDTO condition, ConcurrentHashMap<String, ContextInfo> contextInfoMap, String beginDatetimeStr, String endDatetiemStr) throws Exception {
        ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = buildKeywordIndex(condition.getCollectedItemId(), beginDatetimeStr, endDatetiemStr);
        Set<ContextInfo> contextInfoSet = new KeywordSearchEngine(
                keywordIndexMap,
                contextInfoMap,
                condition
        ).search();
        return contextInfoSet;
    }


    private ConcurrentHashMap<String, ContextInfo> buildContextInfo(Integer collectedItemId, String beginDatetimeStr, String endDatetimeStr) throws IOException {
        List<File> indexFileList = IndexCacheScanner.scan(
                beginDatetimeStr,
                endDatetimeStr,
                CacheUtil.CACHE_DIR + File.separator + collectedItemId + File.separator + IndexType.CONTEXT.getVal(),
                IndexFormat.CONTEXT
        );
        if (CollectionUtils.isEmpty(indexFileList)) {
            throw new CollectorException(beginDatetimeStr + " - " + endDatetimeStr + " 时间段内不存在上下文索引");
        } else {
            logger.info("读取上下文索引缓存文件开始:" + indexFileList.size());
            long begin = System.nanoTime();
            ContextIndexAggregator aggregator = new ContextIndexAggregator();
            for (File indexFile : indexFileList) {
                String json = Files.toString(indexFile, StandardCharsets.UTF_8);
                ConcurrentHashMap<String, ContextInfo> contextInfoMap = JSON.parseObject(json, IndexTypeReference.CONTEXT);
                aggregator.aggregate(contextInfoMap);
            }
            logger.info("读取上下文索引缓存文件结束,耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9) + "秒");
            return aggregator.getAggregatedCollection();
        }
    }

    private ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> buildKvIndex(Integer collectedItemId, String beginDatetimeStr, String endDatetimeStr) throws IOException {
        List<File> indexFileList = IndexCacheScanner.scan(
                beginDatetimeStr,
                endDatetimeStr,
                CacheUtil.CACHE_DIR + File.separator + collectedItemId + File.separator + IndexType.KEY_VALUE.getVal(),
                IndexFormat.KEY_VALUE
        );
        if (CollectionUtils.isEmpty(indexFileList)) {
            throw new CollectorException(beginDatetimeStr + " - " + endDatetimeStr + " 时间段内不存在上下文索引");
        } else {
            logger.info("读取 key value 索引缓存文件开始:" + indexFileList.size());
            long begin = System.nanoTime();
            KvIndexAggregator aggregator = new KvIndexAggregator();
            for (File indexFile : indexFileList) {
                String json = Files.toString(indexFile, StandardCharsets.UTF_8);
                ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexMap = JSON.parseObject(json, IndexTypeReference.KEY_VALUE);
                aggregator.aggregate(kvIndexMap);
            }
            logger.info("读取 key value 索引缓存文件结束,耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9) + "秒");
            return aggregator.getAggregatedCollection();
        }
    }

    private ConcurrentHashMap<String, Set<KeywordIndex>> buildKeywordIndex(Integer collectedItemId, String beginDatetimeStr, String endDatetimeStr) throws IOException {

        List<File> indexFileList = IndexCacheScanner.scan(
                beginDatetimeStr,
                endDatetimeStr,
                CacheUtil.CACHE_DIR + File.separator + collectedItemId + File.separator + IndexType.KEYWORD.getVal(),
                IndexFormat.KEYWORD
        );
        if (CollectionUtils.isEmpty(indexFileList)) {
            throw new CollectorException(beginDatetimeStr + " - " + endDatetimeStr + " 时间段内不存在上下文索引");
        } else {
            logger.info("读取 keyword 索引缓存文件开始:" + indexFileList.size());
            long begin = System.nanoTime();
            KeywordIndexAggregator aggregator = new KeywordIndexAggregator();
            for (File indexFile : indexFileList) {
                String json = Files.toString(indexFile, StandardCharsets.UTF_8);
                ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = JSON.parseObject(json, IndexTypeReference.KEYWORD);
                aggregator.aggregate(keywordIndexMap);
            }
            logger.info("读取 keyword 索引缓存文件结束,耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9) + "秒");
            return aggregator.getAggregatedCollection();
        }
    }


}
