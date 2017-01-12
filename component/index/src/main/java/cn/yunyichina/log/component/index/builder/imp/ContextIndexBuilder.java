package cn.yunyichina.log.component.index.builder.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.index.builder.IndexBuilder;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/3 14:23
 * @Description: 上下文索引构造器
 */
public class ContextIndexBuilder implements IndexBuilder<Map<Long, ContextIndexBuilder.ContextInfo>>, Serializable {

    private static final long serialVersionUID = -6007560470667273849L;
    private File logFile;

    /**
     * 多线程标记tag的时候,要把 Map 改为 ConcurrentHashMap
     * 如果对顺序有需要,可用TreeMap,但是数据量大的时候维护TreeMap可能会耗费更多的内存和消耗更多的CPU时间
     */
    private Map<Long, ContextInfo> contextIndexMap = new HashMap<>(1024);
    private String logContent;


    public ContextIndexBuilder(File logFile) {
        this.logFile = logFile;
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
     * 如果改为2个线程去执行 markTag 也许效率会高一些.
     * 但是要将 Map 改为 ConcurrentHashMap
     * 暂时先用单线程
     */
    @Override
    public Map<Long, ContextInfo> build() {
        markTag(Tag.CONTEXT_BEGIN, true);
        markTag(Tag.CONTEXT_END, false);
        return contextIndexMap;
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
            ContextInfo contextInfo = contextIndexMap.get(count);
            if (null == contextInfo) {
                contextInfo = new ContextInfo();
            }
            if (isBeginTag) {
                contextInfo.setBegin(indexInfo);
            } else {
                contextInfo.setEnd(indexInfo);
            }
            contextIndexMap.put(count, contextInfo);//理论上 key( AtomicLong ) 不会有重复
            contextTagIndex = contextCountBeginTagIndex;
        }
    }

    /**
     * 注意,begin和end是有可能为null的!!!!!!!!!!
     * 注意,begin和end是有可能为null的!!!!!!!!!!
     * 注意,begin和end是有可能为null的!!!!!!!!!!
     *
     * @return
     */
    public static class ContextInfo implements Serializable {

        private static final long serialVersionUID = -8753201735866113930L;

        private IndexInfo begin;
        private IndexInfo end;

        public ContextInfo() {
        }

        public String getBeginLogAndEndLogName() {
            return getLogName(begin) + getLogName(end);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ContextInfo that = (ContextInfo) o;
            return Objects.equals(begin, that.begin) &&
                    Objects.equals(end, that.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(begin, end);
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

        private String getLogName(IndexInfo indexInfo) {
            if (null == indexInfo) {
                return "";
            } else {
                File endLogFile = indexInfo.getLogFile();
                if (null == endLogFile) {
                    return "";
                } else {
                    return endLogFile.getName();
                }
            }
        }
    }

    public static class IndexInfo implements Serializable {
        private static final long serialVersionUID = 7464183768648638516L;
        private File logFile;
        private int indexOfLogFile;

        public IndexInfo() {
        }

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

        public IndexInfo setLogFile(File logFile) {
            this.logFile = logFile;
            return this;
        }

        public IndexInfo setIndexOfLogFile(int indexOfLogFile) {
            this.indexOfLogFile = indexOfLogFile;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IndexInfo indexInfo = (IndexInfo) o;
            return indexOfLogFile == indexInfo.indexOfLogFile &&
                    Objects.equals(logFile, indexInfo.logFile);
        }

        @Override
        public int hashCode() {
            return Objects.hash(logFile, indexOfLogFile);
        }
    }
}
