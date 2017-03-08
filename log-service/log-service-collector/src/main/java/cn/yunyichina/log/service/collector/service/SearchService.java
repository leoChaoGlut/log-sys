package cn.yunyichina.log.service.collector.service;

import cn.yunyichina.log.common.constant.SearchEngineType;
import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.dto.LogResultDTO;
import cn.yunyichina.log.common.entity.dto.SearchConditionDTO;
import cn.yunyichina.log.common.exception.CollectorException;
import cn.yunyichina.log.component.aggregator.log.LogAggregator;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.scanner.imp.LogScanner;
import cn.yunyichina.log.component.searchengine.imp.KeywordSearchEngine;
import cn.yunyichina.log.component.searchengine.imp.KvSearchEngine;
import cn.yunyichina.log.component.searchengine.imp.NoIndexSearchEngine;
import cn.yunyichina.log.service.collector.util.IndexManager;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/3 16:42
 * @Description:
 */
@Service
public class SearchService {

    final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    IndexManager indexManager;

    @Autowired
    CacheService cacheService;

    private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");

    public List<LogResultDTO> searchHistory(SearchConditionDTO condition) throws Exception {
        Integer collectedItemId = condition.getCollectedItemId();
        Set<ContextInfo> contextInfoSet;
        switch (condition.getSearchEngineType()) {
            case SearchEngineType.KEYWORD:
                contextInfoSet = useKeywordSearchEngine(condition, collectedItemId);
                break;
            case SearchEngineType.KEY_VALUE:
                contextInfoSet = useKvSearchEngine(condition, collectedItemId);
                break;
            case SearchEngineType.NO_INDEX:
                contextInfoSet = useNoIndexSearchEngine(condition, collectedItemId);
                break;
            default:
                throw new CollectorException("不支持的搜索引擎类型:" + condition.getSearchEngineType());
        }

        List<LogResultDTO> logResultList = getLogResultListBy(collectedItemId, contextInfoSet);
        return logResultList;
    }

    private List<LogResultDTO> getLogResultListBy(Integer collectedItemId, Set<ContextInfo> contextInfoSet) throws CollectorException {
        logger.info(JSON.toJSONString(contextInfoSet, true));
        if (CollectionUtils.isEmpty(contextInfoSet)) {
            throw new CollectorException("查不到符合条件的上下文");
        } else {

            CollectedItemDO collectedItem = cacheService.getCollectedItemBy(collectedItemId);
            if (collectedItem == null) {
                throw new CollectorException(collectedItemId + " 对应的采集项不存在");
            } else {
                List<LogResultDTO> logResultList = new ArrayList<>(contextInfoSet.size());

                String collectedLogDir = collectedItem.getCollectedLogDir();
                String contextContent;

                for (ContextInfo contextInfo : contextInfoSet) {
                    contextContent = LogAggregator.aggregate(contextInfo, collectedLogDir);
                    if (StringUtils.isNotBlank(contextContent)) {
                        LogResultDTO logResult = buildLogResult(collectedLogDir, contextContent, contextInfo);

                        logResultList.add(logResult);
                    }
                }
                //按照上下文开始时间排序，用于前端显示
                Collections.sort(logResultList, new Comparator<LogResultDTO>() {
                    @Override
                    public int compare(LogResultDTO o1, LogResultDTO o2) {
                        return o1.getLogRegionSet().first().compareTo(o2.getLogRegionSet().first());
                    }
                });
                logger.info(JSON.toJSONString(logResultList, true));
                return logResultList;
            }
        }
    }

    private LogResultDTO buildLogResult(String collectedLogDir, String contextContent, ContextInfo contextInfo) {
        TreeSet<String> logRegionSet = scanLogRegion(contextInfo, collectedLogDir);

        return new LogResultDTO()
                .setContextContent(contextContent)
                .setLogRegionSet(logRegionSet);
    }

    /**
     * 获取上下文所贯穿的所有日志
     *
     * @param contextInfo
     * @param collectedLogDir
     * @return
     */
    private TreeSet<String> scanLogRegion(ContextInfo contextInfo, String collectedLogDir) {
        logger.info(JSON.toJSONString(contextInfo, true));
        File beginLogFile = contextInfo.getBegin().getLogFile();
        File endLogFile = contextInfo.getEnd().getLogFile();
        Map<String, File> logMap = LogScanner.scan(beginLogFile, endLogFile, collectedLogDir);
        logger.info(JSON.toJSONString(logMap, true));
        if (CollectionUtils.isEmpty(logMap)) {
            return new TreeSet<>();
        } else {
            Collection<File> logs = logMap.values();
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

    private Set<ContextInfo> useNoIndexSearchEngine(SearchConditionDTO condition, Integer collectedItemId) throws Exception {
        CollectedItemDO collectedItem = cacheService.getCollectedItemBy(collectedItemId);
        if (collectedItem == null) {
            throw new CollectorException(collectedItemId + " 对应的采集项不存在");
        } else {
            String collectedLogDir = collectedItem.getCollectedLogDir();

            Map<String, File> logMap = LogScanner.scan(
                    dateFormat.format(condition.getBeginDateTime()),
                    dateFormat.format(condition.getEndDateTime()),
                    collectedLogDir);

            Set<ContextInfo> contextInfoSet = new NoIndexSearchEngine(
                    logMap.values(),
                    indexManager.getContextIndexBy(collectedItemId),
                    condition.getNoIndexKeyword()
            ).search();

            return contextInfoSet;
        }
    }

    private Set<ContextInfo> useKvSearchEngine(SearchConditionDTO condition, Integer collectedItemId) throws Exception {
        Set<ContextInfo> contextInfoSet = new KvSearchEngine(
                indexManager.getKvIndexBy(collectedItemId),
                indexManager.getContextIndexBy(collectedItemId),
                condition
        ).search();
        return contextInfoSet;
    }

    private Set<ContextInfo> useKeywordSearchEngine(SearchConditionDTO condition, Integer collectedItemId) throws Exception {
        Set<ContextInfo> contextInfoSet = new KeywordSearchEngine(
                indexManager.getKeywordIndexBy(collectedItemId),
                indexManager.getContextIndexBy(collectedItemId),
                condition
        ).search();
        return contextInfoSet;
    }

}
