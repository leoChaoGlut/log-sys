package cn.yunyichina.log.service.collectorNode.util;

import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.common.util.PropertiesFileUtil;
import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.index.scanner.imp.LogFileScanner;
import cn.yunyichina.log.service.collectorNode.constants.Constants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 11:10
 * @Description:
 */
public class IndexManager{
    private Collection<File> files;
    private Set<KeyValueIndexBuilder.KvTag> kvTagSet;
    private Set<String> keywordSet;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap;
    Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap;
    Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap;

    public IndexManager(SearchCondition searchCondition,
                        Set<KeyValueIndexBuilder.KvTag> kvTagSet,
                        Set<String> keywordSet) {

        Constants constants = (Constants) SpringContextUtil.getBean("constants");

        String beginTime = new PropertiesFileUtil(constants.cursorPropPath).getValue(constants.cursorKey);
        String endTime = sdf.format(searchCondition.getEndDateTime());
        this.kvTagSet = kvTagSet;
        this.keywordSet = keywordSet;
        LogFileScanner logFileScanner = new LogFileScanner(beginTime, endTime, constants.logPath);
        Map<String, File> fileMap = logFileScanner.scan();
        files = fileMap.values();

        buildContextIndexMap();
        buildKeywordIndexMap();
        buildKeyValueIndexMap();
    }

    private void buildKeyValueIndexMap() {
        KeyValueIndexAggregator aggregator = new KeyValueIndexAggregator();

        for (File file : files) {
            KeyValueIndexBuilder builder = new KeyValueIndexBuilder(kvTagSet, file);
            Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> map = builder.build();
            aggregator.aggregate(map);
        }

        keyValueIndexMap = aggregator.getAggregatedCollection();
    }

    private void buildKeywordIndexMap() {


        KeywordIndexAggregator aggregator = new KeywordIndexAggregator();

        for (File file : files) {
            KeywordIndexBuilder builder = new KeywordIndexBuilder(file, keywordSet);
            Map<String, Set<KeywordIndexBuilder.IndexInfo>> map = builder.build();
            aggregator.aggregate(map);
        }

        keywordIndexMap = aggregator.getAggregatedCollection();
    }

    private void buildContextIndexMap() {
        ContextIndexAggregator aggregator = new ContextIndexAggregator();
        for (File f : files) {
            ContextIndexBuilder builder = new ContextIndexBuilder(f);
            Map<Long, ContextIndexBuilder.ContextInfo> map = builder.build();
            aggregator.aggregate(map);
        }

        contextIndexMap = aggregator.getAggregatedCollection();
    }

    public Map<Long, ContextIndexBuilder.ContextInfo> getContextIndexMap() {
        return contextIndexMap;
    }

    public Map<String, Set<KeywordIndexBuilder.IndexInfo>> getKeywordIndexMap() {
        return keywordIndexMap;
    }

    public Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> getKeyValueIndexMap() {
        return keyValueIndexMap;
    }
}
