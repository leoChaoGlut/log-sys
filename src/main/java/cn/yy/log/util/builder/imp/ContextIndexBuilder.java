package cn.yy.log.util.builder.imp;

import cn.yy.log.constant.Tag;
import cn.yy.log.util.builder.IndexBuilder;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/3 14:23
 * @Description: 上下文索引构造器
 */
public class ContextIndexBuilder implements IndexBuilder<Map<Long, ContextIndexBuilder.ContextInfo>> {

    private File logFile;

    /**
     * 多线程标记tag的时候,要把 Map 改为 ConcurrentHashMap
     */
    private Map<Long, ContextInfo> contextIndex = new HashMap<>(1024);
    private String logContent;


    public ContextIndexBuilder(File logFile) {
        this.logFile = logFile;
        try {
            logContent = Files.asCharSource(logFile, Charsets.UTF_8).read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果改为2个线程去执行 markTag 也许效率会高一些.
     * 但是要将 Map 改为 ConcurrentHashMap
     * 暂时先用单线程
     */
    @Override
    public Map<Long, ContextInfo> build() {
        markTag(Tag.CONTEXT_BEGIN, true);
        markTag(Tag.CONTEXT_END, false);
        return contextIndex;
    }

    private void markTag(String tag, boolean isBeginTag) {
        int contextTagIndex = 0;
        int contextCountBeginTagIndex;
        int contextCountEndTagIndex;
        while (0 <= (contextTagIndex = logContent.indexOf(tag, contextTagIndex))) {
            contextCountBeginTagIndex = contextTagIndex + tag.length();
            contextCountEndTagIndex = logContent.indexOf(Tag.CONTEXT_COUNT_END, contextCountBeginTagIndex);
            Long count = Long.valueOf(logContent.substring(contextCountBeginTagIndex, contextCountEndTagIndex));
            IndexInfo indexInfo = new IndexInfo(logFile, contextTagIndex);
            ContextInfo contextInfo = contextIndex.get(count);
            if (null == contextInfo) {
                contextInfo = new ContextInfo();
            }
            if (isBeginTag) {
                contextInfo.setBegin(indexInfo);
            } else {
                contextInfo.setEnd(indexInfo);
            }
            contextIndex.put(count, contextInfo);//理论上 key( AtomicLong ) 不会有重复
            contextTagIndex = contextCountBeginTagIndex;
        }
    }

    public static class ContextInfo {
        private IndexInfo begin;
        private IndexInfo end;

        public ContextInfo() {
        }

        public IndexInfo getBegin() {
            return begin;
        }

        public ContextInfo setBegin(IndexInfo begin) {
            this.begin = begin;
            return this;
        }

        public IndexInfo getEnd() {
            return end;
        }

        public ContextInfo setEnd(IndexInfo end) {
            this.end = end;
            return this;
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
