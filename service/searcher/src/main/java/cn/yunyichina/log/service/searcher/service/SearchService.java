package cn.yunyichina.log.service.searcher.service;

import cn.yunyichina.log.common.entity.constant.SearchEngineType;
import cn.yunyichina.log.common.entity.entity.dto.LogResult;
import cn.yunyichina.log.common.entity.entity.dto.SearchCondition;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.common.util.ZipUtil;
import cn.yunyichina.log.component.aggregator.log.LogAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.scanner.imp.LogFileScanner;
import cn.yunyichina.log.component.searchEngine.imp.KeyValueSearchEngine;
import cn.yunyichina.log.component.searchEngine.imp.KeywordSearchEngine;
import cn.yunyichina.log.component.searchEngine.imp.NoIndexSearchEngine;
import cn.yunyichina.log.service.searcher.constants.CacheName;
import cn.yunyichina.log.service.searcher.util.IndexManager;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    IndexManager indexManager;

    @Value("${constants.upload.logRootDir}")
    private String UPLOADED_LOG_ROOT_DIR;

    @Value("${constants.download.logZip}")
    private String DOWNLOAD_FILE_ZIP;

    //    @Cacheable(cacheNames = {CacheName.OPTION}, key = "#condition.toString()")
    public List<LogResult> history(SearchCondition condition) throws Exception {
        logger.info("service开始搜索历史日志:" + JSON.toJSONString(condition, true));
        Set<ContextIndexBuilder.ContextInfo> contextInfoSet = null;
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
                LogFileScanner scanner = new LogFileScanner(condition.getBeginDateTimeStr(), condition.getEndDateTimeStr(), UPLOADED_LOG_ROOT_DIR);
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
            logger.info("上下文集合为空");
            return new ArrayList<>();
        } else {
            logger.info("搜索到的符合条件的上下文个数:" + contextInfoSet.size());
            List<LogResult> logResultList = new ArrayList<>(contextInfoSet.size());
            for (ContextIndexBuilder.ContextInfo contextInfo : contextInfoSet) {
                String contextStr = LogAggregator.aggregate(contextInfo, UPLOADED_LOG_ROOT_DIR);
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
            logger.info("logResultList size:" + logResultList.size());
            return logResultList;
        }
    }

    //    @Cacheable(cacheNames = {OPTION}, key = "#condition.toString()")
    public Set<String> getDownloadableLogs(SearchCondition condition) {
        LogFileScanner scanner = new LogFileScanner(condition.getBeginDateTimeStr(), condition.getEndDateTimeStr(), UPLOADED_LOG_ROOT_DIR);
        Map<String, File> logFileMap = scanner.scan();
        if (CollectionUtils.isEmpty(logFileMap)) {
            return new TreeSet<>();
        } else {
            Collection<File> logs = logFileMap.values();
            TreeSet<String> logFileSet = new TreeSet<>();
            for (File log : logs) {
                String logFileName = log.getName();
                logFileName = logFileName.substring(0, logFileName.lastIndexOf("."));
                logFileSet.add(logFileName);
            }
            return logFileSet;
        }
    }

    @Cacheable(cacheNames = {CacheName.OPTION}, key = "#contextInfo.getBeginLogAndEndLogName()")
    private TreeSet<String> scanLogRegion(ContextIndexBuilder.ContextInfo contextInfo) {
        File beginLogFile = contextInfo.getBegin().getLogFile();
        File endLogFile = contextInfo.getEnd().getLogFile();
        logger.info("=======scanLogRegion:" + contextInfo.getBeginLogAndEndLogName());
        LogFileScanner scanner = new LogFileScanner(beginLogFile, endLogFile, UPLOADED_LOG_ROOT_DIR);
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

    public File download(String[] logNames) throws Exception{

        File[] logFiles = new File[logNames.length];

        for (int i = 0; i < logNames.length; i++) {
            String logName = UPLOADED_LOG_ROOT_DIR
                    + File.separator + logNames[i].substring(0, 4)
                    + File.separator + logNames[i].substring(4, 6)
                    + File.separator + logNames[i].substring(6, 8)
                    + File.separator + logNames[i].substring(8, 10)
                    + File.separator + logNames[i].substring(10, 12)
                    + File.separator + logNames[i] + ".log";
            File logFile = new File(logName);
            if (logFile.exists()){
                logFiles[i] = logFile;
            }
        }
        ZipUtil.zip(DOWNLOAD_FILE_ZIP,logFiles);
        File zipFile = new File(DOWNLOAD_FILE_ZIP);
        return zipFile;
    }

}
