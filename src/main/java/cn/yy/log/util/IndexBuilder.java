package cn.yy.log.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/10/31 23:40
 * @Description:
 */
public class IndexBuilder {

    public static class KvIndexInfo {
        private String keyName;
        private String keyTag;
        private String valueBeginTag;
        private String valueEndTag;
        private int keyOffset;

        public KvIndexInfo(String keyName, String keyTag, String valueBeginTag, String valueEndTag) {
            this.keyName = keyName;
            this.keyTag = keyTag;
            this.valueBeginTag = valueBeginTag;
            this.valueEndTag = valueEndTag;
            this.keyOffset = keyTag.indexOf(keyName);// key OffSet
        }

        public String getKeyName() {
            return keyName;
        }

        public KvIndexInfo setKeyName(String keyName) {
            this.keyName = keyName;
            return this;
        }

        public String getKeyTag() {
            return keyTag;
        }

        public KvIndexInfo setKeyTag(String keyTag) {
            this.keyTag = keyTag;
            return this;
        }

        public String getValueBeginTag() {
            return valueBeginTag;
        }

        public KvIndexInfo setValueBeginTag(String valueBeginTag) {
            this.valueBeginTag = valueBeginTag;
            return this;
        }

        public String getValueEndTag() {
            return valueEndTag;
        }

        public KvIndexInfo setValueEndTag(String valueEndTag) {
            this.valueEndTag = valueEndTag;
            return this;
        }

        public int getKeyOffset() {
            return keyOffset;
        }

        public KvIndexInfo setKeyOffset(int keyOffset) {
            this.keyOffset = keyOffset;
            return this;
        }
    }

    private Map<String, TreeSet<Integer>> normalIndexMap = new HashMap<>();
    private Map<String, Map<String, TreeSet<Integer>>> accuratedIndexMap = new HashMap<>();
    private List<KvIndexInfo> kvIndexInfoList;
    private String fileContent;

    public IndexBuilder(List<KvIndexInfo> kvIndexInfoList, String fileContent) {
        for (KvIndexInfo kvIndexInfo : kvIndexInfoList) {
            normalIndexMap.put(kvIndexInfo.getKeyName(), new TreeSet<Integer>());
            accuratedIndexMap.put(kvIndexInfo.getKeyName(), new HashMap<String, TreeSet<Integer>>());
        }
        this.kvIndexInfoList = kvIndexInfoList;
        this.fileContent = fileContent;
    }

    public void build() {
        for (KvIndexInfo kvIndexInfo : kvIndexInfoList) {
            String keyName = kvIndexInfo.getKeyName();
            String keyTag = kvIndexInfo.getKeyTag();
            String valueBeginTag = kvIndexInfo.getValueBeginTag();
            String valueEndTag = kvIndexInfo.getValueEndTag();

            int keyOffset = kvIndexInfo.getKeyOffset();
            int valueBeginTagLength = valueBeginTag.length();
            int valueEndTagLength = valueEndTag.length();
            int index = 0;

            while ((index = fileContent.indexOf(keyTag, index)) != -1) {
                normalIndexMap.get(keyName).add(index + keyOffset);

                int valueBeginIndex = fileContent.indexOf(valueBeginTag, index) + valueBeginTagLength;
                int valueEndIndex = fileContent.indexOf(valueEndTag, valueBeginIndex);
                String value = fileContent.substring(valueBeginIndex, valueEndIndex);

                if (value != null && !"".equals(value.trim())) {
                    TreeSet<Integer> specialIndexSet = accuratedIndexMap.get(keyName).get(value);
                    if (specialIndexSet == null) {
                        specialIndexSet = new TreeSet<>();
                    }
                    specialIndexSet.add(valueBeginIndex);
                    accuratedIndexMap.get(keyName).put(value, specialIndexSet);
                }

                index = valueEndIndex + valueEndTagLength;
            }
        }
    }

    public Map<String, TreeSet<Integer>> getNormalIndexMap() {
        return normalIndexMap;
    }

    public Map<String, Map<String, TreeSet<Integer>>> getAccuratedIndexMap() {
        return accuratedIndexMap;
    }
}
