import cn.yunyichina.log.common.entity.do_.KvTagDO;
import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KvIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KvIndexBuilder;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import cn.yunyichina.log.component.scanner.imp.LogScanner;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/28 12:21
 * @Description:
 */
public class AggregatorText {

    public static final String LOG_DIR = "D:\\gitRepo\\yunyi\\src\\log\\log-sys\\log-resource\\test-resource\\log";
    public static final File LOG_FILE = new File(LOG_DIR + "\\2017\\02\\27\\10\\33\\201702271033.log");


    @Test
    public void contextIndexAggregatorTest() {
        ContextIndexAggregator contextIndexAggregator = new ContextIndexAggregator();
        Collection<File> logs = getLogs();

        for (File log : logs) {
            ConcurrentHashMap<String, ContextInfo> contextInfoMap = new ContextIndexBuilder(log).build();
            System.out.println(contextInfoMap.size());
            contextIndexAggregator.aggregate(contextInfoMap);
        }
        ConcurrentHashMap<String, ContextInfo> aggregatedCollection = contextIndexAggregator.getAggregatedCollection();
        System.out.println(aggregatedCollection.size());

    }

    @Test
    public void keywordIndexAggregatorTest() {
        Set<String> keywordTagSet = new HashSet<>();
        keywordTagSet.add("pat_card_no");
        keywordTagSet.add("data");
        keywordTagSet.add("pat_mobile");
        keywordTagSet.add("pat_name");

        KeywordIndexAggregator keywordIndexAggregator = new KeywordIndexAggregator();
        Collection<File> logs = getLogs();

        for (File log : logs) {
            ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexMap = new KeywordIndexBuilder(log, keywordTagSet).build();
            keywordIndexAggregator.aggregate(keywordIndexMap);
            int size = 0;
            Set<Entry<String, Set<KeywordIndex>>> entrySet = keywordIndexMap.entrySet();
            for (Entry<String, Set<KeywordIndex>> entry : entrySet) {
                size += entry.getValue().size();
            }
            System.out.println(size);//单次总数
        }
        ConcurrentHashMap<String, Set<KeywordIndex>> aggregatedCollection = keywordIndexAggregator.getAggregatedCollection();

        int size = 0;
        Set<Entry<String, Set<KeywordIndex>>> entrySet = aggregatedCollection.entrySet();
        for (Entry<String, Set<KeywordIndex>> entry : entrySet) {
            size += entry.getValue().size();
        }
        System.out.println(size);//聚合总数
    }

    @Test
    public void kvIndexBuilderTest() {
        Set<KvTagDO> kvTagSet = new HashSet<>();
        kvTagSet.add(new KvTagDO("data", "\"data\":\"", "\""));
        kvTagSet.add(new KvTagDO("hospital_code", "\"hospital_code\":\"", "\""));
        kvTagSet.add(new KvTagDO("patCardNo", "<patCardNo>", "</"));

        KvIndexAggregator kvIndexAggregator = new KvIndexAggregator();
        Collection<File> logs = getLogs();

        for (File log : logs) {
            ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexSetMap = new KvIndexBuilder(log, kvTagSet).build();
            kvIndexAggregator.aggregate(kvIndexSetMap);
            int size = 0;
            Set<Entry<String, ConcurrentHashMap<String, Set<KvIndex>>>> entrySet0 = kvIndexSetMap.entrySet();
            for (Entry<String, ConcurrentHashMap<String, Set<KvIndex>>> entry0 : entrySet0) {
                ConcurrentHashMap<String, Set<KvIndex>> valueIndexMap = entry0.getValue();
                Set<Entry<String, Set<KvIndex>>> entrySet1 = valueIndexMap.entrySet();
                for (Entry<String, Set<KvIndex>> entry1 : entrySet1) {
                    size += entry1.getValue().size();
                }
            }
            System.out.println(size);
        }
        ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> aggregatedCollection = kvIndexAggregator.getAggregatedCollection();
        int size = 0;
        Set<Entry<String, ConcurrentHashMap<String, Set<KvIndex>>>> entrySet0 = aggregatedCollection.entrySet();
        for (Entry<String, ConcurrentHashMap<String, Set<KvIndex>>> entry0 : entrySet0) {
            ConcurrentHashMap<String, Set<KvIndex>> valueIndexMap = entry0.getValue();
            Set<Entry<String, Set<KvIndex>>> entrySet1 = valueIndexMap.entrySet();
            for (Entry<String, Set<KvIndex>> entry1 : entrySet1) {
                size += entry1.getValue().size();
            }
        }
        System.out.println(size);

    }

    private Collection<File> getLogs() {
        String beginDatetime = "2017-02-22 18:11";
        String endDatetime = "2017-02-23 10:05";
        Map<String, File> logMap = LogScanner.scan(beginDatetime, endDatetime, LOG_DIR);
        Collection<File> logs = logMap.values();
        return logs;
    }
}
