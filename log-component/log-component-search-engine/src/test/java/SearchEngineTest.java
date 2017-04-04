import cn.yunyichina.log.common.constant.SearchEngineType;
import cn.yunyichina.log.common.entity.do_.KvTagDO;
import cn.yunyichina.log.common.entity.dto.SearchConditionDTO;
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
import cn.yunyichina.log.component.searchengine.imp.KeywordSearchEngine;
import cn.yunyichina.log.component.searchengine.imp.KvSearchEngine;
import cn.yunyichina.log.component.searchengine.imp.MultNoIndexSearchEngine;
import cn.yunyichina.log.component.searchengine.imp.NoIndexSearchEngine;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/28 17:12
 * @Description:
 */
public class SearchEngineTest {

    final String LOG_DIR = "D:\\gitRepo\\yunyi\\src\\log\\log-sys\\log-resource\\test-resource\\log";
    //    final String LOG_DIR = "E:\\log";
    final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");
    String beginDatetime = "2016-11-15 14:21";
    String endDatetime = "2018-11-15 14:25";

    @Test
    public void noIndexSearchEngineTest() throws Exception {
        String keyword = "Exception1";
        Collection<File> logs = getLogs();
        long beginTime = System.nanoTime();
        NoIndexSearchEngine noIndexSearchEngine = new NoIndexSearchEngine(logs, getContextInfoMap(logs), keyword);
        Set<ContextInfo> contextInfoSet = noIndexSearchEngine.search();
        long endTime = System.nanoTime();
        System.out.println(JSON.toJSONString(contextInfoSet, true));
        System.out.println("耗时:" + BigDecimal.valueOf(endTime - beginTime, 9) + " 秒");
    }

    @Test
    public void multNoIndexSearchEngineTest() throws Exception {
        String keyword = "Exception1";
        Collection<File> logs = getLogs();
        long beginTime = System.nanoTime();
        MultNoIndexSearchEngine multNoIndexSearchEngine = new MultNoIndexSearchEngine(logs, getContextInfoMap(logs), keyword);
        Set<ContextInfo> contextInfoSet = multNoIndexSearchEngine.search();
        long endTime = System.nanoTime();
//        System.out.println(JSON.toJSONString(contextInfoSet, true));
        System.out.println("耗时:" + BigDecimal.valueOf(endTime - beginTime, 9) + " 秒");
    }

    @Test
    public void keywordSearchEngineTest() throws Exception {
        String keyword = "pat_card_no";

        SearchConditionDTO condition = new SearchConditionDTO()
                .setBeginDateTime(dateFormat.parse(beginDatetime))
                .setEndDateTime(dateFormat.parse(endDatetime))
                .setKeyword(keyword)
                .setSearchEngineType(SearchEngineType.KEYWORD);

        Collection<File> logs = getLogs();
        Set<ContextInfo> contextInfoSet = new KeywordSearchEngine(getKeywordIndexMap(logs), getContextInfoMap(logs), condition).search();
        System.out.println(contextInfoSet.size());
        System.out.println(JSON.toJSONString(contextInfoSet, true));
    }

    @Test
    public void kvSearchEngineTest() throws Exception {
        String key = "pat_card_no";
        String value = "0000013797";
        boolean fuzzy = false;

        SearchConditionDTO condition = new SearchConditionDTO()
                .setSearchEngineType(SearchEngineType.KEY_VALUE)
                .setBeginDateTime(dateFormat.parse(beginDatetime))
                .setEndDateTime(dateFormat.parse(endDatetime))
                .setKey(key)
                .setValue(value)
                .setFuzzy(fuzzy);

        Collection<File> logs = getLogs();
        Set<ContextInfo> contextInfoSet = new KvSearchEngine(getKvIndexMap(logs), getContextInfoMap(logs), condition).search();
        System.out.println(contextInfoSet.size());
        System.out.println(JSON.toJSONString(contextInfoSet, true));
    }

    private ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> getKvIndexMap(Collection<File> logs) {
        Set<KvTagDO> kvTagSet = new HashSet<>();
        kvTagSet.add(new KvTagDO("data", "\"data\":\"", "\""));
        kvTagSet.add(new KvTagDO("hospital_code", "\"hospital_code\":\"", "\""));
        kvTagSet.add(new KvTagDO("patCardNo", "<patCardNo>", "</"));
        kvTagSet.add(new KvTagDO("pat_card_no", "\"pat_card_no\":\"", "\""));

        KvIndexAggregator kvIndexAggregator = new KvIndexAggregator();
        for (File log : logs) {
            ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexSetMap = new KvIndexBuilder(log, kvTagSet).build();
            kvIndexAggregator.aggregate(kvIndexSetMap);
        }
        return kvIndexAggregator.getAggregatedCollection();
    }

    private ConcurrentHashMap<String, Set<KeywordIndex>> getKeywordIndexMap(Collection<File> logs) {
        Set<String> keywordTagSet = new HashSet<>();
        keywordTagSet.add("pat_card_no");
        keywordTagSet.add("data");
        keywordTagSet.add("pat_mobile");
        keywordTagSet.add("pat_name");

        KeywordIndexAggregator keywordIndexAggregator = new KeywordIndexAggregator();
        for (File log : logs) {
            ConcurrentHashMap<String, Set<KeywordIndex>> keywordIndexSetMap = new KeywordIndexBuilder(log, keywordTagSet).build();
            keywordIndexAggregator.aggregate(keywordIndexSetMap);
        }
        return keywordIndexAggregator.getAggregatedCollection();
    }


    private Collection<File> getLogs() {
        Map<String, File> logMap = LogScanner.scan(beginDatetime, endDatetime, LOG_DIR);
        Collection<File> logs = logMap.values();
        return logs;
    }

    public ConcurrentHashMap<String, ContextInfo> getContextInfoMap(Collection<File> logs) {
        ContextIndexAggregator contextIndexAggregator = new ContextIndexAggregator();
        for (File log : logs) {
            ConcurrentHashMap<String, ContextInfo> contextInfoMap = new ContextIndexBuilder(log).build();
            contextIndexAggregator.aggregate(contextInfoMap);
        }
        return contextIndexAggregator.getAggregatedCollection();
    }
}
