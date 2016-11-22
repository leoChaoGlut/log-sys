import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.indexBuilder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.indexBuilder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.indexBuilder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.indexBuilder.util.LogFileScanner;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/16 10:24
 * @Description:
 */
public class AggregatorTest {


    @Test
    public void contextIndexAggregator() throws IOException {
        //        TODO 比较难测试
        LogFileScanner logFileScanner = new LogFileScanner("2016-01-01 01:02", "2016-11-15 14:23", "D:\\tmp");
        Map<String, File> fileMap = logFileScanner.scan();
        Collection<File> values = fileMap.values();
        ContextIndexAggregator aggregator = new ContextIndexAggregator();

        for (File f : values) {
            ContextIndexBuilder builder = new ContextIndexBuilder(f);
            Map<Long, ContextIndexBuilder.ContextInfo> map = builder.build();
            System.out.println(JSON.toJSONString(map, true));
            System.out.println("===================");
            aggregator.aggregate(map);
        }

        Map<Long, ContextIndexBuilder.ContextInfo> aggregatedCollection = aggregator.getAggregatedCollection();
        System.out.println(JSON.toJSONString(aggregatedCollection, true));
    }

    @Test
    public void keywordIndexAggregator() throws IOException {
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

        Map<String, Set<KeywordIndexBuilder.IndexInfo>> aggregatedCollection = aggregator.getAggregatedCollection();
        System.out.println(JSON.toJSONString(aggregatedCollection, true));
    }

    @Test
    public void keyValueIndexAggregator() {
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

        Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> aggregatedCollection = aggregator.getAggregatedCollection();
        System.out.println(JSON.toJSONString(aggregatedCollection, true));

    }
}
