package cn.yunyichina.log.service.collector.service;

import cn.yunyichina.log.common.entity.constant.SearchEngineType;
import cn.yunyichina.log.common.entity.entity.dto.LogResult;
import cn.yunyichina.log.common.entity.entity.dto.SearchCondition;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.component.aggregator.log.LogAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.scanner.imp.LogFileScanner;
import cn.yunyichina.log.component.searchEngine.imp.KeyValueSearchEngine;
import cn.yunyichina.log.component.searchEngine.imp.KeywordSearchEngine;
import cn.yunyichina.log.component.searchEngine.imp.NoIndexSearchEngine;
import cn.yunyichina.log.service.collector.constants.CacheName;
import cn.yunyichina.log.service.collector.constants.Config;
import cn.yunyichina.log.service.collector.constants.Key;
import cn.yunyichina.log.service.collector.constants.ServiceConfig;
import cn.yunyichina.log.service.collector.util.IndexManager;
import cn.yunyichina.log.service.collector.util.PropertiesUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 14:49
 * @Description:
 */
@Service
public class SearchService {

    final LoggerWrapper logger = LoggerWrapper.getLogger(SearchService.class);

    @Autowired
    Config config;

    @Autowired
    ServiceConfig serviceConfig;

    @Autowired
    PropertiesUtil propUtil;

    @Cacheable(cacheNames = {CacheName.REAL_TIME}, key = "#condition.toString()")
    public List<LogResult> realtime(SearchCondition condition) throws Exception {
        List<String> keywordList = JSON.parseArray(propUtil.get(Key.KEYWORD_SET), String.class);
        List<KeyValueIndexBuilder.KvTag> kvTagList = JSON.parseArray(propUtil.get(Key.KV_TAG_SET), KeyValueIndexBuilder.KvTag.class);
        if (CollectionUtils.isEmpty(keywordList) && CollectionUtils.isEmpty(kvTagList)) {
            throw new Exception("keyword和keyValue为空,请检查properties.");
        } else {
            if (CollectionUtils.isEmpty(keywordList)) {//防止hashset初始化null异常
                keywordList = new ArrayList<>();
            } else if (CollectionUtils.isEmpty(kvTagList)) {
                kvTagList = new ArrayList<>();
            }
            Set<String> keywordSet = new HashSet<>(keywordList);
            Set<KeyValueIndexBuilder.KvTag> kvTagSet = new HashSet<>(kvTagList);
            String beginDatetime = propUtil.get(Key.LAST_MODIFY_TIME);
            IndexManager indexManager = new IndexManager(condition, kvTagSet, keywordSet, beginDatetime, config.getLogRootDir());
            Set<ContextIndexBuilder.ContextInfo> contextInfoSet;
            switch (condition.getSearchEngineType()) {
                case SearchEngineType.KEYWORD:
                    KeywordSearchEngine keywordSearchEngine = new KeywordSearchEngine(indexManager.getKeywordIndexMap(), indexManager.getContextIndexMap(), condition);
                    keywordSearchEngine.setFuzzySearch(condition.isFuzzy());
                    contextInfoSet = keywordSearchEngine.search();
                    break;
                case SearchEngineType.KEY_VALUE:
                    KeyValueSearchEngine keyValueSearchEngine = new KeyValueSearchEngine(indexManager.getKeyValueIndexMap(), indexManager.getContextIndexMap(), condition);
                    keyValueSearchEngine.setFuzzySearch(condition.isFuzzy());
                    contextInfoSet = keyValueSearchEngine.search();
                    break;
                case SearchEngineType.NO_INDEX:
                    LogFileScanner scanner = new LogFileScanner(condition.getBeginDateTimeStr(), condition.getEndDateTimeStr(), config.getLogRootDir());
                    Map<String, File> logFileMap = scanner.scan();
                    if (CollectionUtils.isEmpty(logFileMap)) {
                        return new ArrayList<>();
                    } else {
                        NoIndexSearchEngine noIndexSearchEngine = new NoIndexSearchEngine(logFileMap.values(), indexManager.getContextIndexMap(), condition.getNoIndexKeyword());
                        contextInfoSet = noIndexSearchEngine.search();
                    }
                    break;
                default:
                    throw new Exception("不支持的搜索引擎类型:" + condition.getSearchEngineType());
            }

            if (contextInfoSet == null) {
                return new ArrayList<>();
            } else {
                List<LogResult> logResultList = new ArrayList<>(contextInfoSet.size());
                for (ContextIndexBuilder.ContextInfo contextInfo : contextInfoSet) {
                    String contextStr = LogAggregator.aggregate(contextInfo, config.getLogRootDir());
                    if (StringUtils.isEmpty(contextStr)) {
                        contextStr = null;//告诉GC,可以清理了.因为这里是比较耗内存的,所以要主动赋null
                    } else {
                        TreeSet<String> logRegionSet = scanLogRegion(contextInfo);
                        LogResult logResult = new LogResult()
                                .setLogRegionSet(logRegionSet)
                                .setContextStr(contextStr);

                        logResultList.add(logResult);
                    }
                }
                Collections.sort(logResultList, new Comparator<LogResult>() {
                    @Override
                    public int compare(LogResult o1, LogResult o2) {
                        return o1.getLogRegionSet().first().compareTo(o2.getLogRegionSet().first());
                    }
                });
                return logResultList;
            }
        }
    }

    public String getLastUploadDatetime() {
        String lastUploadDatetime = propUtil.get(Key.LAST_MODIFY_TIME);
        return lastUploadDatetime;
    }

    //    @Cacheable(cacheNames = {OPTION}, key = "#contextInfo.getBeginLogAndEndLogName()")
    private TreeSet<String> scanLogRegion(ContextIndexBuilder.ContextInfo contextInfo) {
        File beginLogFile = contextInfo.getBegin().getLogFile();
        File endLogFile = contextInfo.getEnd().getLogFile();
        logger.info("=======scanLogRegion:" + contextInfo.getBeginLogAndEndLogName());
        LogFileScanner scanner = new LogFileScanner(beginLogFile, endLogFile, config.getLogRootDir());
        Map<String, File> logFileMap = scanner.scan();
        if (null == logFileMap) {
            return new TreeSet<>();
        } else {
            Collection<File> logs = logFileMap.values();
            TreeSet<String> logRegionSet = new TreeSet<>();
            for (File log : logs) {
                String logName = log.getName();
                logName = logName.substring(0, 4) + "-" +
                        logName.substring(4, 6) + "-" +
                        logName.substring(6, 8) + " " +
                        logName.substring(8, 10) + ":" +
                        logName.substring(10, 12);
                logRegionSet.add(logName);
            }
            return logRegionSet;
        }
    }

    public Map<String, ServiceConfig.ConfigCollector> getDirs() {
        return serviceConfig.getCollectorMap();
    }

}
