import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.aggregator.log.LogAggregator;
import cn.yunyichina.log.component.entity.dto.SearchCondition;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.index.scanner.imp.LogFileScanner;
import cn.yunyichina.log.component.searchEngine.imp.KeyValueSearchEngine;
import cn.yunyichina.log.component.searchEngine.imp.KeywordSearchEngine;
import cn.yunyichina.log.component.searchEngine.imp.NoIndexSearchEngine;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/23 16:45
 * @Description:
 */
public class SearchEngineTest {

    Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Before
    public void before() {
        LogFileScanner logFileScanner = new LogFileScanner("2016-11-15 14:23", "2016-11-15 14:25", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> values = fileMap.values();
        ContextIndexAggregator aggregator = new ContextIndexAggregator();
        for (File f : values) {
            ContextIndexBuilder builder = new ContextIndexBuilder(f);
            Map<Long, ContextIndexBuilder.ContextInfo> map = builder.build();
//            System.out.println(JSON.toJSONString(map, true));
//            System.out.println("===================");
            aggregator.aggregate(map);
        }

        this.contextIndexMap = aggregator.getAggregatedCollection();
//        System.out.println(JSON.toJSONString(this.contextIndexMap, true));
    }


    @Test
    public void keywordSearchEngineTest() throws Exception {
        LogFileScanner logFileScanner = new LogFileScanner("2016-11-15 14:23", "2016-11-15 14:25", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> files = fileMap.values();

        Set<String> keywordSet = new HashSet<>();

        keywordSet.add("pat_card_no");
//        keywordSet.add("将要返回给平台的Response As Follow");
//        keywordSet.add("patCardNo");
//        keywordSet.add("patCardNo1");

        KeywordIndexAggregator aggregator = new KeywordIndexAggregator();

        for (File file : files) {
            KeywordIndexBuilder builder = new KeywordIndexBuilder(file, keywordSet);
            Map<String, Set<KeywordIndexBuilder.IndexInfo>> map = builder.build();
//            System.out.println(JSON.toJSONString(map, true));
            aggregator.aggregate(map);
        }

        Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap = aggregator.getAggregatedCollection();

//        System.out.println(JSON.toJSONString(keywordIndexMap, true));

        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setBeginDateTime(sdf.parse("2016-11-15 14:23"));
        searchCondition.setEndDateTime(sdf.parse("2016-11-15 14:25"));
        searchCondition.setKeyword("pat_card_no");

        KeywordSearchEngine searchEngine = new KeywordSearchEngine(keywordIndexMap, contextIndexMap, searchCondition);
//        searchEngine.setAllowOverTimeRange(true);
//        searchEngine.setExactlyBetweenTimeRange(true);
        Set<ContextIndexBuilder.ContextInfo> contextInfoSet = searchEngine.search();
        System.out.println(JSON.toJSONString(contextInfoSet, true));
        System.out.println(contextInfoSet.size());
//        for (ContextIndexBuilder.ContextInfo contextInfo : contextInfoSet) {
//            String contextStr = LogAggregator.aggregate(contextInfo);
//            System.out.println(contextStr);
//            TimeUnit.SECONDS.sleep(5);
//        }

    }

    @Test
    public void keyValueSearchEngineTest() throws Exception {
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
//            System.out.println(JSON.toJSONString(map, true));
//            System.out.println("===============");
            aggregator.aggregate(map);
        }

        Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap = aggregator.getAggregatedCollection();
//        System.out.println(JSON.toJSONString(keyValueIndexMap, true));
        SearchCondition searchCondition = new SearchCondition();

        searchCondition.setKey("pat_card_no");
        searchCondition.setValue("0000426666");
        searchCondition.setBeginDateTime(sdf.parse("2016-11-15 14:23"));
        searchCondition.setEndDateTime(sdf.parse("2016-11-15 14:25"));

        KeyValueSearchEngine searchEngine = new KeyValueSearchEngine(keyValueIndexMap, contextIndexMap, searchCondition);
        searchEngine.setExactlyBetweenTimeRange(true);
        Set<ContextIndexBuilder.ContextInfo> contextInfoSet = searchEngine.search();
//        System.out.println(JSON.toJSONString(contextInfoSet, true));
        System.out.println(contextInfoSet.size());
        for (ContextIndexBuilder.ContextInfo contextInfo : contextInfoSet) {
            String contextStr = LogAggregator.aggregate(contextInfo, "D://tmp");
            System.out.println(contextStr);
        }

    }

    @Test
    public void noIndexSearchEngineTest() {

        LogFileScanner logFileScanner = new LogFileScanner("2016-11-15 14:24", "2016-11-15 14:24", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> files = fileMap.values();

        String keyword = "getMZPatient";

        long start = System.nanoTime();
        NoIndexSearchEngine searchEngine = new NoIndexSearchEngine(files, contextIndexMap, keyword);
        try {
            Set<ContextIndexBuilder.ContextInfo> contextInfoSet = searchEngine.search();
            System.out.println(JSON.toJSONString(contextInfoSet, true));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("总共消耗" + BigDecimal.valueOf(System.nanoTime() - start, 9));
    }
}
