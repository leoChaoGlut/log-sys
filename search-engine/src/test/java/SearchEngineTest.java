import cn.yunyichina.log.index.aggregator.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.index.aggregator.imp.KeywordIndexAggregator;
import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.index.util.LogFileScanner;
import cn.yunyichina.log.search.engine.entity.SearchCondition;
import cn.yunyichina.log.search.engine.imp.KeywordSearchEngine;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jonven on 2016/11/14.
 */
public class SearchEngineTest {
    Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Before
    public void before() {
        LogFileScanner logFileScanner = new LogFileScanner("2016-01-01 01:02", "2017-01-01 01:02", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> values = fileMap.values();
        Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap = new HashMap<>();
        for (File f : values) {
            ContextIndexBuilder builder = new ContextIndexBuilder(f);
            Map<Long, ContextIndexBuilder.ContextInfo> map = builder.build();
            System.out.println(JSON.toJSONString(map, true));
            contextIndexMap.putAll(map);
        }
        this.contextIndexMap = contextIndexMap;
    }

    @Test
    public void keywordSearchEngineTest() throws Exception {
        LogFileScanner logFileScanner = new LogFileScanner("2016-01-01 01:02", "2017-11-15 14:23", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> files = fileMap.values();

        Set<String> keywordSet = new HashSet<>();

        keywordSet.add("pat_card_no");
        keywordSet.add("将要返回给平台的Response As Follow");
        keywordSet.add("patCardNo");
        keywordSet.add("patCardNo1");

        KeywordIndexAggregator aggregator = new KeywordIndexAggregator();

        for (File file : files) {
            KeywordIndexBuilder builder = new KeywordIndexBuilder(file, keywordSet);
            Map<String, Set<KeywordIndexBuilder.IndexInfo>> map = builder.build();
            aggregator.aggregate(map);
        }

        Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap = aggregator.getAggregatedCollection();

        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setBeginDateTime(sdf.parse("2016-01-01 01:02"));
        searchCondition.setEndDateTime(sdf.parse("2017-11-15 14:23"));
        searchCondition.setKeyword("pat_card_no");
        KeywordSearchEngine searchEngine = new KeywordSearchEngine(keywordIndexMap, contextIndexMap, searchCondition);
        Set<ContextIndexBuilder.ContextInfo> contextInfoSet = searchEngine.search();
        System.out.println(JSON.toJSONString(contextInfoSet, true));

    }

    @Test
    public void keyValueSearchEngineTest() {
        LogFileScanner logFileScanner = new LogFileScanner("2016-01-01 01:02", "2017-11-15 14:23", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> files = fileMap.values();
        KeyValueIndexAggregator aggregator = new KeyValueIndexAggregator();

        Set<KeyValueIndexBuilder.KvTag> kvTagSet = new HashSet<>();

        kvTagSet.add(new KeyValueIndexBuilder.KvTag("patCardNo", "<patCardNo>", "</patCardNo>"));
        kvTagSet.add(new KeyValueIndexBuilder.KvTag("sign", "\"sign\":\"", "\""));
        kvTagSet.add(new KeyValueIndexBuilder.KvTag("pat_mobile", "\"pat_mobile\":\"", "\""));

        for (File file : files) {
            KeyValueIndexBuilder builder = new KeyValueIndexBuilder(kvTagSet, file);
            Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> map = builder.build();
            aggregator.aggregate(map);
        }

        Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap = aggregator.getAggregatedCollection();
    }

}
