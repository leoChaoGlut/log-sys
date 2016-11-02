package cn.yy.log.util;

import cn.yy.log.entity.vo.LogIndex;
import cn.yy.log.entity.vo.LogPair;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/1 11:46
 * @Description: 精确查找必须提供精确的key和value, 不提供模糊查询功能.
 */
public class AccurateSearchEngine {


    private String key;
    private String value;
    private List<Map.Entry<String, LogPair>> logPairList;

    private List<Integer> valueIndexList;
    private int logPairIndex;
    private int logPairListSize;
    private String contextBeginTag;
    private String contextEndTag;

    private LinkedList<String> contextCache;
    private List<String> contextList = new ArrayList<>();

    private final String END_OF_ROW_TAG = "$RowEnd$";
    private final String END_OF_ROW_VALUE_END_TAG = "$";

    private final String CONTEXT_BEGIN_TAG_PREFIX = "$ReqBegin$";
    private final String CONTEXT_BEGIN_TAG_SUFFIX = "$";

    private final String CONTEXT_END_TAG_PREFIX = "$ReqEnd$";
    private final String CONTEXT_END_TAG_SUFFIX = "$";

    private final int END_OF_ROW_TAG_LENGTH = END_OF_ROW_TAG.length();
    /**
     * 找上下文的开始结束标识的最大偏移,如2016-01-01 01:01的最大偏移为2016-01-01 01:06,不再查找此后的文件.
     * 因为一般一个请求不会超过 MAX_COUNT 分钟.
     */
    private final int MAX_COUNT = 5;

    public static List<String> search(String key, String value, Map<String, LogPair> logPairMap) {
        AccurateSearchEngine accurateSearchEngine = new AccurateSearchEngine(key, value, logPairMap);
        accurateSearchEngine.search();
        return accurateSearchEngine.getContextList();
    }

    private AccurateSearchEngine(String key, String value, Map<String, LogPair> logPairMap) {
        this.key = key;
        this.value = value;
        this.logPairList = new ArrayList<>(logPairMap.entrySet());
        this.logPairListSize = logPairList.size();
    }

    /**
     * 入口
     */
    private void search() {
        try {
            boolean located = accurateLocate();
            if (located) {
                findContext();
            } else {
//             TODO  未在精确索引中查找到key:" + key + " , value:" + value + " 的位置,是否要进行全文查找?
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findContext() throws Exception {
        int valueIndexListSize = valueIndexList.size();
        for (int i = 0; i < valueIndexListSize; i++) {
            contextCache = new LinkedList<>();//重置上下文缓存
            Integer valueIndex = valueIndexList.get(i);
            Map.Entry<String, LogPair> logPairEntry = logPairList.get(logPairIndex);
            LogPair logPair = logPairEntry.getValue();
            File logFile = logPair.getLogFile();
            String logContent = IOUtil.read(logFile);
            int endOfRowIndex = logContent.indexOf(END_OF_ROW_TAG, valueIndex);
            if (endOfRowIndex < 0) {
//               TODO "找不到日志行结束标记.可能单行日志太大,导致打印到了下一行.(小概率事件,一般不会发生.只能手动找日志了)
            }
            int endOfRowValueBeginIndex = endOfRowIndex + END_OF_ROW_TAG_LENGTH;
            int endOfRowValueEndIndex = logContent.indexOf(END_OF_ROW_VALUE_END_TAG, endOfRowValueBeginIndex);
            if (endOfRowValueEndIndex < 0) {
//                TODO "找不到日志行结束标记.可能单行日志太大,导致打印到了下一行.(小概率事件,一般不会发生.只能手动找日志了)
            }
            String requestCount = logContent.substring(endOfRowValueBeginIndex, endOfRowValueEndIndex);
            contextBeginTag = CONTEXT_BEGIN_TAG_PREFIX + requestCount + CONTEXT_BEGIN_TAG_SUFFIX;
            contextEndTag = CONTEXT_END_TAG_PREFIX + requestCount + CONTEXT_END_TAG_SUFFIX;

            findContextHead(logContent, 0, valueIndex);
            findContextTail(logContent, valueIndex + 1, logContent.length());

            if (contextCache.isEmpty()) {
//               TODO "查找上下文失败"
            } else {
                String context = buildContext();
                contextList.add(context);
            }
        }
    }

    private String buildContext() {
        int capacity = 0;
        for (String contextSegment : contextCache) {
            capacity += contextSegment.length();
        }
        StringBuilder contextBuilder = new StringBuilder(capacity);
        for (String contextSegment : contextCache) {
            contextBuilder.append(contextSegment);
        }
        return contextBuilder.toString();
    }

    private void findContextHead(String logContent, int contentBeginIndex, int contentEndIndex) throws IOException {
        backTracking(logContent, logPairIndex, contentBeginIndex, contentEndIndex, true, 0);
    }

    private void findContextTail(String logContent, int contentBeginIndex, int contentEndIndex) throws IOException {
        backTracking(logContent, logPairIndex, contentBeginIndex, contentEndIndex, false, 0);
    }

    private void backTracking(String logContent, int logIndex, int contentBeginIndex, int contentEndIndex, boolean isForward, int count) throws IOException {
        if (count <= MAX_COUNT && 0 <= logIndex && logIndex < logPairListSize) {
            String contextTag = isForward ? contextBeginTag : contextEndTag;
            int contextTagIndex = logContent.indexOf(contextTag, contentBeginIndex);
            if (contextTagIndex >= 0) {//如果找到了上下文标记
                if (isForward) {
                    contextCache.addFirst(logContent.substring(contextTagIndex, contentEndIndex));
                } else {
                    contextCache.addLast(logContent.substring(contentBeginIndex, contextTagIndex + contextTag.length()));
                }
            } else {//如果没有找到了上下文标记
                if (isForward) {
                    contextCache.addFirst(logContent.substring(contentBeginIndex, contentEndIndex));
                    if (logIndex - 1 >= 0) {
                        File logFile = logPairList.get(logIndex - 1).getValue().getLogFile();
                        String prevLogContent = IOUtil.read(logFile);
                        backTracking(prevLogContent, logIndex - 1, 0, prevLogContent.length(), isForward, count + 1);
                    } else {
//                       TODO 指定时间段内的所有log文件,找不到key,value的上下文的开头
                    }
                } else {
                    contextCache.addLast(logContent.substring(contentBeginIndex, contentEndIndex));
                    if (logIndex + 1 < logPairListSize) {
                        File logFile = logPairList.get(logIndex + 1).getValue().getLogFile();
                        String nextLogContent = IOUtil.read(logFile);
                        backTracking(nextLogContent, logIndex + 1, 0, nextLogContent.length(), isForward, count + 1);
                    } else {
//                       TODO 指定时间段内的所有log文件,找不到key,value的上下文的结尾
                    }
                }
            }
        }
    }

    /**
     * 对key和value定位
     *
     * @return
     * @throws Exception
     */
    private boolean accurateLocate() throws Exception {
        for (int i = 0; i < logPairListSize; i++) {
            Map<String, Map<String, TreeSet<Integer>>> accurateIndexMap = getAccurateIndexMap(i);
            Map<String, TreeSet<Integer>> valueIndexMap = accurateIndexMap.get(key);

            if (valueIndexMap == null) {
//                TODO "不存在key为:" + key + " 的索引,是否要进行全文查找?
            } else {
                TreeSet<Integer> valueIndexSet = valueIndexMap.get(value);
                if (valueIndexSet == null) {
//                 TODO  存在key为:" + key + " 的索引,但不存在value为:" + value + " 的索引.是否要进行全文查找?
                } else {
                    valueIndexList = new ArrayList<>(valueIndexSet);//一个匹配项在log中可能多次出现
                    logPairIndex = i;
                    return true;//因为是精确查找,所以只会有一个匹配项
                }
            }
        }
        return false;
    }

    private Map<String, Map<String, TreeSet<Integer>>> getAccurateIndexMap(int i) throws IOException {
        Map.Entry<String, LogPair> logPairEntry = logPairList.get(i);
        LogPair logPair = logPairEntry.getValue();
        File indexFile = logPair.getIndexFile();
        String indexFileContent = IOUtil.read(indexFile);
        LogIndex logIndex = JSON.parseObject(indexFileContent, LogIndex.class);
        return logIndex.getAccuratedIndexMap();
    }

    public List<String> getContextList() {
        return contextList;
    }
}
