package cn.yy.log.util.v2;

import cn.yy.log.util.IOUtil;

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
 * @Description:
 */
public class KeyValueIndexBuilder {

    public static class IndexInfo {
        private File logFile;
        private int indexOfLogFile;
        private Long requestCount;

        public IndexInfo(File logFile, int indexOfLogFile, Long requestCount) {
            this.logFile = logFile;
            this.indexOfLogFile = indexOfLogFile;
            this.requestCount = requestCount;
        }

        public File getLogFile() {
            return logFile;
        }

        public int getIndexOfLogFile() {
            return indexOfLogFile;
        }

        public Long getRequestCount() {
            return requestCount;
        }
    }

    public static class Tag {
        private String key;
        private String keyTag;
        private String valueEndTag;
        private int keyOffset;

        public Tag(String key, String keyTag, String valueEndTag) {
            this.key = key;
            this.keyTag = keyTag;
            this.valueEndTag = valueEndTag;
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

    private final String ROW_END_TAG = "";
    private final String REQUEST_COUNT_END_TAG = "";

    private List<Tag> tagList;
    private File logFile;

    private String logContent;
    private Map<String, Map<String, List<IndexInfo>>> keyValueIndex;

    public KeyValueIndexBuilder(List<Tag> tagList, File logFile) {
        this.tagList = tagList;
        this.logFile = logFile;
        try {
            logContent = IOUtil.read(logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void build() {
        for (Tag tag : tagList) {
            String key = tag.getKey();
            String keyTag = tag.getKeyTag();
            int keyTagIndex = 0;

            while (0 <= (keyTagIndex = logContent.indexOf(keyTag, keyTagIndex))) {
                int valueBeginIndex = keyTagIndex + keyTag.length();
                int valueEndIndex = logContent.indexOf(tag.getValueEndTag(), valueBeginIndex);
                String value = logContent.substring(valueBeginIndex, valueEndIndex);
                if (!"".equals(value.trim())) {
                    int rowEndTagIndex = logContent.indexOf(ROW_END_TAG, valueEndIndex);
                    int requestCountBeginTagIndex = rowEndTagIndex + ROW_END_TAG.length();
                    int requestCountEndTagIndex = logContent.indexOf(REQUEST_COUNT_END_TAG, requestCountBeginTagIndex);
                    String count = logContent.substring(requestCountBeginTagIndex, requestCountEndTagIndex);

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
            }
        }
    }
}
