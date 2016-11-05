package cn.yy.log.util.v2;

import cn.yy.log.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/3 15:53
 * @Description:
 */
public class KeywordIndexBuilder {

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


    private final String ROW_END_TAG = "";
    private final String REQUEST_COUNT_END_TAG = "";

    private Map<String, List<IndexInfo>> keywordIndex;
    private File logFile;
    private List<String> keywordList;

    private String logContent;


    public KeywordIndexBuilder(Map<String, List<IndexInfo>> keywordIndex, File logFile, List<String> keywordList) {
        this.keywordIndex = keywordIndex;
        this.logFile = logFile;
        this.keywordList = keywordList;
        try {
            logContent = IOUtil.read(logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void build() {
        for (String keyword : keywordList) {
            int keywordListSize = keywordList.size();
            for (int i = 0; i < keywordListSize; i++) {
                int keywordTagIndex = 0;
                while (0 <= (keywordTagIndex = logContent.indexOf(keyword, keywordTagIndex))) {
                    int rowEndTagIndex = logContent.indexOf(ROW_END_TAG, keywordTagIndex + keyword.length());
                    int requestCountBeginTagIndex = rowEndTagIndex + ROW_END_TAG.length();
                    int requestCountEndTagIndex = logContent.indexOf(REQUEST_COUNT_END_TAG, requestCountBeginTagIndex);
                    String count = logContent.substring(requestCountBeginTagIndex, requestCountEndTagIndex);
                    IndexInfo indexInfo = new IndexInfo(logFile, keywordTagIndex, Long.valueOf(count));
                    List<IndexInfo> indexInfoList = keywordIndex.get(keyword);
                    if (indexInfoList == null) {
                        indexInfoList = new ArrayList<>();
                    }
                    indexInfoList.add(indexInfo);
                    keywordIndex.put(keyword, indexInfoList);
                    keywordTagIndex = requestCountEndTagIndex;
                }
            }
        }
    }

}
