package cn.yunyichina.log.index.builder.imp;

import cn.yunyichina.log.index.builder.IndexBuilder;
import cn.yunyichina.log.index.constant.Tag;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/3 15:53
 * @Description: 关键词索引构造器
 */
public class KeywordIndexBuilder implements IndexBuilder<Map<String, Set<KeywordIndexBuilder.IndexInfo>>> {
    private File logFile;
    private Set<String> keywordSet;

    private Map<String, Set<IndexInfo>> keywordIndex = new HashMap<>(1024);
    private String logContent;


    public KeywordIndexBuilder(File logFile, Set<String> keywordSet) {
        this.logFile = logFile;
        this.keywordSet = keywordSet;
        try {
            logContent = Files.asCharSource(logFile, Charsets.UTF_8).read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Set<IndexInfo>> build() {
        for (String keyword : keywordSet) {
            int keywordTagIndex = 0;
            while (0 <= (keywordTagIndex = logContent.indexOf(keyword, keywordTagIndex))) {
                int rowEndTagIndex = logContent.indexOf(Tag.ROW_END, keywordTagIndex + keyword.length());
                int contextCountBeginTagIndex = rowEndTagIndex + Tag.ROW_END.length();
                int contextCountEndTagIndex = logContent.indexOf(Tag.CONTEXT_COUNT_END, contextCountBeginTagIndex);
                String count = logContent.substring(contextCountBeginTagIndex, contextCountEndTagIndex);
                IndexInfo indexInfo = new IndexInfo(logFile, keywordTagIndex, Long.valueOf(count));
                Set<IndexInfo> indexInfoSet = keywordIndex.get(keyword);
                if (indexInfoSet == null) {
                    indexInfoSet = new HashSet<>();
                }
                indexInfoSet.add(indexInfo);
                keywordIndex.put(keyword, indexInfoSet);
                keywordTagIndex = contextCountEndTagIndex;
            }
        }
        return keywordIndex;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IndexInfo indexInfo = (IndexInfo) o;
            return indexOfLogFile == indexInfo.indexOfLogFile &&
                    Objects.equals(logFile, indexInfo.logFile) &&
                    Objects.equals(contextCount, indexInfo.contextCount);
        }

        @Override
        public int hashCode() {
            return Objects.hash(logFile, indexOfLogFile, contextCount);
        }
    }

}
