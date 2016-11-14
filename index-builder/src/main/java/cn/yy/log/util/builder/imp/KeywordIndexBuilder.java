package cn.yy.log.util.builder.imp;

import cn.yy.log.constant.Tag;
import cn.yy.log.util.builder.IndexBuilder;
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
 * @CreateTime: 2016/11/3 15:53
 * @Description: 关键词索引构造器
 */
public class KeywordIndexBuilder implements IndexBuilder<Map<String, List<KeywordIndexBuilder.IndexInfo>>> {
    private File logFile;
    private List<String> keywordList;

    private Map<String, List<IndexInfo>> keywordIndex = new HashMap<>(1024);
    private String logContent;


    public KeywordIndexBuilder(File logFile, List<String> keywordList) {
        this.logFile = logFile;
        this.keywordList = keywordList;
        try {
            logContent = Files.asCharSource(logFile, Charsets.UTF_8).read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, List<IndexInfo>> build() {
        for (String keyword : keywordList) {
            int keywordTagIndex = 0;
            while (0 <= (keywordTagIndex = logContent.indexOf(keyword, keywordTagIndex))) {
                int rowEndTagIndex = logContent.indexOf(Tag.ROW_END, keywordTagIndex + keyword.length());
                int contextCountBeginTagIndex = rowEndTagIndex + Tag.ROW_END.length();
                int contextCountEndTagIndex = logContent.indexOf(Tag.CONTEXT_COUNT_END, contextCountBeginTagIndex);
                String count = logContent.substring(contextCountBeginTagIndex, contextCountEndTagIndex);
                IndexInfo indexInfo = new IndexInfo(logFile, keywordTagIndex, Long.valueOf(count));
                List<IndexInfo> indexInfoList = keywordIndex.get(keyword);
                if (indexInfoList == null) {
                    indexInfoList = new ArrayList<>();
                }
                indexInfoList.add(indexInfo);
                keywordIndex.put(keyword, indexInfoList);
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
    }

}
