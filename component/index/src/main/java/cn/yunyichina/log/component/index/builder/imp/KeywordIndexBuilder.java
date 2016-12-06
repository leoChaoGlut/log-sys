package cn.yunyichina.log.component.index.builder.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.index.builder.IndexBuilder;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/3 15:53
 * @Description: 关键词索引构造器
 */
public class KeywordIndexBuilder implements IndexBuilder<Map<String, Set<KeywordIndexBuilder.IndexInfo>>>, Serializable {
    private File logFile;
    /**
     * set保证关键词不重复
     */
    private Set<String> keywordSet;

    private Map<String, Set<IndexInfo>> keywordIndexMap = new HashMap<>(1024);
    private String logContent;


    public KeywordIndexBuilder(File logFile, Set<String> keywordSet) {
        this.logFile = logFile;
        this.keywordSet = keywordSet;
        try {
            String logFileName = this.logFile.getName();
            if (logFileName.lastIndexOf(".log") == -1) {
                logContent = "";
            } else {
                logContent = Files.asCharSource(logFile, Charsets.UTF_8).read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 假设现在要搜索patCardNo关键词,文件里一共有6个,但是返回的结果可能是3个.这是正确的结果.
     * 原因:当搜索到关键词的时候,就会标记这一条完整的logger.info,就算这条logger.info里
     * 还有相同的关键词,他们都已经被标记在这一条logger.info里了.
     *
     * @return
     */
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
                Set<IndexInfo> indexInfoSet = keywordIndexMap.get(keyword);
                if (indexInfoSet == null) {
                    indexInfoSet = new HashSet<>();
                }
                indexInfoSet.add(indexInfo);
                keywordIndexMap.put(keyword, indexInfoSet);
                keywordTagIndex = contextCountEndTagIndex;
            }
        }
        return keywordIndexMap;
    }

    public static class IndexInfo implements Serializable {
        private File logFile;
        private int indexOfLogFile;
        private Long contextCount;

        public IndexInfo() {
        }

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

        public IndexInfo setLogFile(File logFile) {
            this.logFile = logFile;
            return this;
        }

        public IndexInfo setIndexOfLogFile(int indexOfLogFile) {
            this.indexOfLogFile = indexOfLogFile;
            return this;
        }

        public IndexInfo setContextCount(Long contextCount) {
            this.contextCount = contextCount;
            return this;
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
