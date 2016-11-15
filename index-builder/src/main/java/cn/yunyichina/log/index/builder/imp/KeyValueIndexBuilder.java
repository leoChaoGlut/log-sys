package cn.yunyichina.log.index.builder.imp;

import cn.yunyichina.log.index.builder.IndexBuilder;
import cn.yunyichina.log.index.builder.constant.Tag;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/6 0:33
 * @Description: 键值对索引构造器
 */
public class KeyValueIndexBuilder implements IndexBuilder<Map<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>>> {

    private List<KvTag> kvTagList;
    private File logFile;

    private String logContent;
    private Map<String, Map<String, List<IndexInfo>>> keyValueIndex = new HashMap<>(1024);

    public KeyValueIndexBuilder(List<KvTag> kvTagList, File logFile) {
        this.kvTagList = kvTagList;
        this.logFile = logFile;
        try {
            logContent = Files.asCharSource(logFile, Charsets.UTF_8).read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Map<String, List<IndexInfo>>> build() {
        for (KvTag kvTag : kvTagList) {
            String key = kvTag.getKey();
            String keyTag = kvTag.getKeyTag();
            int keyTagIndex = 0;

            while (0 <= (keyTagIndex = logContent.indexOf(keyTag, keyTagIndex))) {
                int valueBeginIndex = keyTagIndex + keyTag.length();
                int valueEndIndex = logContent.indexOf(kvTag.getValueEndTag(), valueBeginIndex);
                String value = logContent.substring(valueBeginIndex, valueEndIndex);
                if (!"".equals(value.trim())) {
                    int rowEndTagIndex = logContent.indexOf(Tag.ROW_END, valueEndIndex);
                    int contextCountBeginTagIndex = rowEndTagIndex + Tag.ROW_END.length();
                    int contextCountEndTagIndex = logContent.indexOf(Tag.CONTEXT_COUNT_END, contextCountBeginTagIndex);
                    String count = logContent.substring(contextCountBeginTagIndex, contextCountEndTagIndex);

                    Map<String, List<IndexInfo>> valueMap = keyValueIndex.get(key);
                    if (valueMap == null) {
                        valueMap = new HashMap<>();
                    }
                    List<IndexInfo> indexInfoList = valueMap.get(value);
                    if (indexInfoList == null) {
                        indexInfoList = new ArrayList<>();
                    }
                    indexInfoList.add(new IndexInfo(logFile, keyTagIndex, Long.valueOf(count)));
                    valueMap.put(value, indexInfoList);
                    keyValueIndex.put(key, valueMap);
                }
                keyTagIndex = valueEndIndex;
            }
        }
        return keyValueIndex;
    }

    public static class IndexInfo {
        private File logFile;
        private int indexOfLogFile;
        private Long contextCount;

        public IndexInfo(File logFile, int indexOfLogFile, Long contextCount) {
            this.logFile = logFile;
            this.indexOfLogFile = indexOfLogFile;
            this.contextCount = contextCount;
        }

        public File getLogFile() {
            return logFile;
        }

        public int getIndexOfLogFile() {
            return indexOfLogFile;
        }

        public Long getContextCount() {
            return contextCount;
        }
    }

    public static class KvTag {
        private String key;
        private String keyTag;
        private String valueEndTag;
        private int keyOffset;

        public KvTag(String key, String keyTag, String valueEndTag) {
            this.key = key;
            /**
             * Tag一定要足够有标识性,不要嫌它字符多.否则可能会将不必要的值作为索引.尽量大于2个字符,并且要与value的起始位置相连.
             * 如: "key":"value",
             * 那么:
             * key = "key"
             * keyTag = "\"key\":\""
             * valueEndTag = "\","
             */
            this.keyTag = keyTag;
            this.valueEndTag = valueEndTag;//Tag一定要足够有标识性,不要嫌它字符多.否则可能会将不必要的值作为索引.尽量大于2个字符.
            this.keyOffset = keyTag.indexOf(key);// key OffSet
        }

        public String getKey() {
            return key;
        }

        public String getKeyTag() {
            return keyTag;
        }

        public String getValueEndTag() {
            return valueEndTag;
        }

        public int getKeyOffset() {
            return keyOffset;
        }
    }
}
