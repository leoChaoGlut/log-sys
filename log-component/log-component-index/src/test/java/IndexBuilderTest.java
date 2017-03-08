import cn.yunyichina.log.common.entity.do_.KvTagDO;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KvIndexBuilder;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/28 9:57
 * @Description:
 */
public class IndexBuilderTest {

    public static final File LOG_FILE = new File("D:\\gitRepo\\yunyi\\src\\log\\log-sys\\log-resource\\test-resource\\log\\2017\\02\\27\\10\\33\\201702271033.log");

    @Test
    public void contextIndexBuilderTest() {
        ContextIndexBuilder contextIndexBuilder = new ContextIndexBuilder(LOG_FILE);
        ConcurrentHashMap<Long, ContextInfo> contextInfoMap = contextIndexBuilder.build();
        System.out.println(contextInfoMap.size());
    }

    @Test
    public void keywordIndexBuilderTest() {
        Set<String> keywordTagSet = new HashSet<>();
        keywordTagSet.add("pat_card_no");
        keywordTagSet.add("data");
        keywordTagSet.add("pat_mobile");
        keywordTagSet.add("pat_name");

        KeywordIndexBuilder keywordIndexBuilder = new KeywordIndexBuilder(LOG_FILE, keywordTagSet);
        ConcurrentHashMap<String, Set<KeywordIndex>> logIndexMap = keywordIndexBuilder.build();
        Set<Map.Entry<String, Set<KeywordIndex>>> entrySet = logIndexMap.entrySet();
        for (Map.Entry<String, Set<KeywordIndex>> entry : entrySet) {
            Set<KeywordIndex> keywordIndexSet = entry.getValue();
            System.out.println(entry.getKey());
            for (KeywordIndex li : keywordIndexSet) {
                System.out.println("\t" + li.getContextCount() + " - " + li.getIndexOfLogFile() + "\t");
            }
        }
    }

    @Test
    public void kvIndexBuilderTest() {
        Set<KvTagDO> kvTagSet = new HashSet<>();
        kvTagSet.add(new KvTagDO("data", "\"data\":\"", "\""));
        kvTagSet.add(new KvTagDO("hospital_code", "\"hospital_code\":\"", "\""));
        kvTagSet.add(new KvTagDO("patCardNo", "<patCardNo>", "</"));

        KvIndexBuilder kvIndexBuilder = new KvIndexBuilder(LOG_FILE, kvTagSet);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> kvIndexSetMap = kvIndexBuilder.build();
        Set<Map.Entry<String, ConcurrentHashMap<String, Set<KvIndex>>>> entrySet0 = kvIndexSetMap.entrySet();
        for (Map.Entry<String, ConcurrentHashMap<String, Set<KvIndex>>> entry0 : entrySet0) {
            ConcurrentHashMap<String, Set<KvIndex>> kvIndexMap = entry0.getValue();
            Set<Map.Entry<String, Set<KvIndex>>> entrySet1 = kvIndexMap.entrySet();
            System.out.println(entry0.getKey());
            for (Map.Entry<String, Set<KvIndex>> entry1 : entrySet1) {
                Set<KvIndex> kvIndexSet = entry1.getValue();
                System.out.println("\t" + entry1.getKey());
                for (KvIndex kvIndex : kvIndexSet) {
                    System.out.println("\t\t" + kvIndex.getContextCount() + " - " + kvIndex.getIndexOfLogFile() + " - " + kvIndex.getLogFile() + "\t");
                }
            }
        }
    }
}
