import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.index.scanner.imp.LogFileScanner;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/23 16:44
 * @Description:
 */
public class AggregatorTest {


    @Test
    public void contextIndexAggregator() throws IOException {
        String beginDateTime = "2016-01-01 01:02";
        String endDateTime = "2016-11-15 14:23";
        String rootDir = "D:\\gitRepo\\gitHub\\log-sys\\component\\index\\src\\test\\resources\\log";
        LogFileScanner logFileScanner = new LogFileScanner(beginDateTime, endDateTime, rootDir);
        Map<String, File> logFileMap = logFileScanner.scan();
        Collection<File> logFiles = logFileMap.values();
        ContextIndexAggregator aggregator = new ContextIndexAggregator();

        for (File logFile : logFiles) {
            ContextIndexBuilder builder = new ContextIndexBuilder(logFile);
            Map<Long, ContextIndexBuilder.ContextInfo> contextInfoMap = builder.build();
            aggregator.aggregate(contextInfoMap);
        }

        Map<Long, ContextIndexBuilder.ContextInfo> aggregatedCollection = aggregator.getAggregatedCollection();
        System.out.println(JSON.toJSONString(aggregatedCollection, true));
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(rootDir + "\\context.index"));
            oos.writeObject(aggregatedCollection);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
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

    @Test
    public void test() {
        System.out.println(File.separator);
    }
}
