package cn.yy.log.util.v2;

import cn.yy.log.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/3 14:23
 * @Description: 采集节点使用
 */
public class RequestIndexBuilder {

    private final String REQUEST_BEGIN_TAG = "";
    private final String REQUEST_END_TAG = "";
    private final String REQUEST_COUNT_END_TAG = "";

    /**
     * 多线程标记tag的时候,要把 Map 改为 ConcurrentHashMap
     */
    private Map<Long, IndexInfo> requestIndex;
    private File logFile;

    private String logContent;


    public RequestIndexBuilder(Map<Long, IndexInfo> requestIndex, File logFile) {
        this.requestIndex = requestIndex;
        if (this.requestIndex == null) {
            this.requestIndex = new HashMap<>();
        }
        this.logFile = logFile;
        try {
            this.logContent = IOUtil.read(logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果改为2个线程去执行 markTag 也许效率会高一些.
     * 但是要将 Map 改为 ConcurrentHashMap
     * 暂时先用单线程
     */
    public void build() {
        markTag(REQUEST_BEGIN_TAG);
        markTag(REQUEST_END_TAG);
    }

    private void markTag(String tag) {
        int requestTagIndex = 0;
        int requestCountBeginTagIndex;
        int requestCountEndTagIndex;
        while (0 <= (requestTagIndex = logContent.indexOf(tag, requestTagIndex))) {
            requestCountBeginTagIndex = requestTagIndex + tag.length();
            requestCountEndTagIndex = logContent.indexOf(REQUEST_COUNT_END_TAG, requestCountBeginTagIndex);
            String count = logContent.substring(requestCountBeginTagIndex, requestCountEndTagIndex);
            IndexInfo indexInfo = new IndexInfo(logFile, requestTagIndex);
            requestIndex.put(Long.valueOf(count), indexInfo);//理论上 key( AtomicLong ) 不会有重复
            requestTagIndex = requestCountBeginTagIndex;
        }
    }

    public static class IndexInfo {
        private File logFile;
        private int indexOfLogFile;

        public IndexInfo(File logFile, int indexOfLogFile) {
            this.logFile = logFile;
            this.indexOfLogFile = indexOfLogFile;
        }

        public File getLogFile() {
            return logFile;
        }

        public int getIndexOfLogFile() {
            return indexOfLogFile;
        }
    }
}
