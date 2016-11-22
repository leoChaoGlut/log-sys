package cn.yunyichina.log.service.logCenter.util;

import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.indexBuilder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.indexBuilder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.indexBuilder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.indexBuilder.util.LogFileScanner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 11:10
 * @Description:
 */
@Component
public class IndexManager {

    Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap;
    Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap;
    Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap;

    @PostConstruct
    public void init() {
        buildContextIndexMap();
        buildKeywordIndexMap();
        buildKeyValueIndexMap();
    }

    private void buildKeyValueIndexMap() {
        LogFileScanner logFileScanner = new LogFileScanner("2016-11-15 14:23", "2016-11-15 14:25", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> files = fileMap.values();
        KeyValueIndexAggregator aggregator = new KeyValueIndexAggregator();

        Set<KeyValueIndexBuilder.KvTag> kvTagSet = new HashSet<>();

        kvTagSet.add(new KeyValueIndexBuilder.KvTag("pat_card_no", "\"pat_card_no\":\"", "\""));
        kvTagSet.add(new KeyValueIndexBuilder.KvTag("pat_id_no", "\"pat_id_no\":\"", "\""));
        kvTagSet.add(new KeyValueIndexBuilder.KvTag("patName", "<patName>", "</patName>"));

        for (File file : files) {
            KeyValueIndexBuilder builder = new KeyValueIndexBuilder(kvTagSet, file);
            Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> map = builder.build();
            aggregator.aggregate(map);
        }

        keyValueIndexMap = aggregator.getAggregatedCollection();
    }

    private void buildKeywordIndexMap() {
        LogFileScanner logFileScanner = new LogFileScanner("2016-11-15 14:23", "2016-11-15 14:25", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> files = fileMap.values();

        Set<String> keywordSet = new HashSet<>();

        keywordSet.add("pat_card_no");

        KeywordIndexAggregator aggregator = new KeywordIndexAggregator();

        for (File file : files) {
            KeywordIndexBuilder builder = new KeywordIndexBuilder(file, keywordSet);
            Map<String, Set<KeywordIndexBuilder.IndexInfo>> map = builder.build();
            aggregator.aggregate(map);
        }

        keywordIndexMap = aggregator.getAggregatedCollection();
    }

    private void buildContextIndexMap() {
        LogFileScanner logFileScanner = new LogFileScanner("2016-11-15 14:23", "2016-11-15 14:25", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> values = fileMap.values();
        ContextIndexAggregator aggregator = new ContextIndexAggregator();
        for (File f : values) {
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
