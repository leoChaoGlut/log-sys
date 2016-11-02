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
 * @Description:
 */
public class AccuratedSearchEngine {


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

    public static List<String> search(String key, String value, Map<String, LogPair> logPairMap) {
        try {
            AccuratedSearchEngine accuratedSearchEngine = new AccuratedSearchEngine(key, value, logPairMap);
            accuratedSearchEngine.search();
            return accuratedSearchEngine.getContextList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AccuratedSearchEngine(String key, String value, Map<String, LogPair> logPairMap) throws Exception {
        this.key = key;
        this.value = value;
        this.logPairList = new ArrayList<>(logPairMap.entrySet());
        this.logPairListSize = logPairList.size();
    }

    public void search() {
        try {
            boolean located = locate();
            if (located) {
                findContext();
            } else {
                throw new Exception("未在精确索引中查找到key:" + key + " , value:" + value + " 的位置,是否要进行全文查找?");
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
                throw new Exception("找不到日志行结束标记.可能单行日志太大,导致打印到了下一行.(小概率事件,一般不会发生.只能手动找日志了)");
            }
            int endOfRowValueBeginIndex = endOfRowIndex + END_OF_ROW_TAG_LENGTH;
            int endOfRowValueEndIndex = logContent.indexOf(END_OF_ROW_VALUE_END_TAG, endOfRowValueBeginIndex);
            if (endOfRowValueEndIndex < 0) {
                throw new Exception("找不到日志行结束标记.可能单行日志太大,导致打印到了下一行.(小概率事件,一般不会发生.只能手动找日志了)");
            }
            String requestCount = logContent.substring(endOfRowValueBeginIndex, endOfRowValueEndIndex);
            contextBeginTag = CONTEXT_BEGIN_TAG_PREFIX + requestCount + CONTEXT_BEGIN_TAG_SUFFIX;
            contextEndTag = CONTEXT_END_TAG_PREFIX + requestCount + CONTEXT_END_TAG_SUFFIX;

            findContextHead(logContent);
            findContextTail(logContent);

            if (contextCache.isEmpty()) {
                throw new Exception("查找上下文失败");
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

    private void findContextHead(String logContent) throws IOException {
        backTracking(logContent, logPairIndex, 0, logContent.length(), true);
    }

    private void findContextTail(String logContent) throws IOException {
        backTracking(logContent, logPairIndex, 0, logContent.length(), false);
    }

    private void backTracking(String logContent, int logPairIndex, int contentBeginIndex, int contentEndIndex, boolean isForward) throws IOException {
        if (0 <= logPairIndex && logPairIndex < logPairListSize) {
            String contextTag = isForward ? contextBeginTag : contextEndTag;
            int contextTagIndex = logContent.indexOf(contextTag, contentBeginIndex);
            if (contextTagIndex >= 0) {//如果找到了上下文标记
                if (isForward) {
                    contextCache.addFirst(logContent.substring(contextTagIndex, contentEndIndex));
                } else {
                    contextCache.addLast(logContent.substring(contentBeginIndex, contextTagIndex));
                }
            } else {//如果没有找到了上下文标记
                if (isForward) {
                    contextCache.addFirst(logContent.substring(contentBeginIndex, contentEndIndex));
                    File logFile = logPairList.get(logPairIndex - 1).getValue().getLogFile();
                    String prevLogContent = IOUtil.read(logFile);
                    backTracking(prevLogContent, logPairIndex - 1, 0, prevLogContent.length(), isForward);
                } else {
                    contextCache.addLast(logContent.substring(contentBeginIndex, contentEndIndex));
                    File logFile = logPairList.get(logPairIndex + 1).getValue().getLogFile();
                    String nextLogContent = IOUtil.read(logFile);
                    backTracking(nextLogContent, logPairIndex + 1, 0, nextLogContent.length(), isForward);
                }
            }
        }
    }

    private boolean locate() throws Exception {
        for (int i = 0; i < logPairListSize; i++) {
            Map<String, Map<String, TreeSet<Integer>>> accuratedIndexMap = getAccuratedIndexMap(i);
            Map<String, TreeSet<Integer>> valueIndexMap = accuratedIndexMap.get(key);

            if (valueIndexMap == null) {
                throw new Exception("不存在key为:" + key + " 的索引,是否要进行全文查找?");
            } else {
                TreeSet<Integer> valueIndexSet = valueIndexMap.get(value);
                if (valueIndexSet == null) {
                    throw new Exception("存在key为:" + key + " 的索引,但不存在value为:" + value + " 的索引.是否要进行全文查找?");
                } else {
                    valueIndexList = new ArrayList<>(valueIndexSet);
                    logPairIndex = i;
                    return true;
                }
            }
        }
        return false;
    }

    private Map<String, Map<String, TreeSet<Integer>>> getAccuratedIndexMap(int i) throws IOException {
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
