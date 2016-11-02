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

    private LinkedList<String> contextCache = new LinkedList<>();


    private final String END_OF_ROW_TAG = "End Of Row Tag";
    private final String END_OF_ROW_VALUE_END_TAG = "End Of Row Value Tag";

    private final String CONTEXT_BEGIN_TAG_PREFIX = "Context Begin Tag Prefix";
    private final String CONTEXT_BEGIN_TAG_SUFFIX = "Context Begin Tag Suffix";

    private final String CONTEXT_END_TAG_PREFIX = "Context End Tag Prefix";
    private final String CONTEXT_END_TAG_SUFFIX = "Context End Tag Suffix";

    private final int END_OF_ROW_TAG_LENGTH = END_OF_ROW_TAG.length();


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

            findContextHead();
            findContextTail();
        }
    }

    private void findContextHead() {

    }

    private void findContextTail() {

    }

    private void backTracking(int logPairIndex) throws IOException {
        if (0 <= logPairIndex && logPairIndex < logPairListSize) {
            File logFile = logPairList.get(logPairIndex).getValue().getLogFile();
            String logContent = IOUtil.read(logFile);
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


}
