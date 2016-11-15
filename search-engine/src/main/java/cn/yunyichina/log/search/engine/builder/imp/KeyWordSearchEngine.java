package cn.yunyichina.log.search.engine.builder.imp;

import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.search.engine.builder.SearchEngine;
import cn.yunyichina.log.search.engine.builder.entity.SearchCondition;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Author: Jonven
 * @ModifiyedBy: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 18:13
 * @Description: 关键词搜索引擎
 */
public class KeyWordSearchEngine implements SearchEngine {

    /**
     * TODO 模糊搜索
     */
    private boolean fuzzySearch = false;
    /**
     * 例: 若某个日志的上下文的时间段为[0,3],查询条件的时间段为[1,2],
     * if allowOverTimeRange -> match
     * else -> notMatch
     */
    private boolean allowOverTimeRange = false;
    /**
     * true:搜索条件的时间区间必须为上下文时间区间的父区间
     * false:反之.
     */
    private boolean exactlyBetweenTimeRange = false;
    private String baseDir;
    private SearchCondition searchCondition;
    private Map<String, List<KeywordIndexBuilder.IndexInfo>> keywordIndex;
    private Map<Long, ContextIndexBuilder.ContextInfo> contextIndex;


    private List<ContextIndexBuilder.ContextInfo> matchedContextInfoList;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");

    public KeyWordSearchEngine(SearchCondition searchCondition) {
        this.searchCondition = searchCondition;
    }


    @Override
    public List<ContextIndexBuilder.ContextInfo> search() throws Exception {
        List<KeywordIndexBuilder.IndexInfo> indexInfoList = keywordIndex.get(searchCondition.getKeyword());
        if (CollectionUtils.isEmpty(indexInfoList)) {

        } else {
            matchedContextInfoList = new ArrayList<>(indexInfoList.size());
            for (KeywordIndexBuilder.IndexInfo indexInfo : indexInfoList) {
                Long contextCount = indexInfo.getContextCount();
                ContextIndexBuilder.ContextInfo contextInfo = contextIndex.get(contextCount);
                if (inDateTimeRange(contextInfo)) {
                    matchedContextInfoList.add(contextInfo);
                } else {

                }
            }
        }
        return matchedContextInfoList;
    }

    /**
     * 时间区间交集判断
     *
     * @param contextInfo
     * @return
     * @throws ParseException
     */
    private boolean inDateTimeRange(ContextIndexBuilder.ContextInfo contextInfo) throws ParseException {
        String beginLogFileName = contextInfo.getBegin().getLogFile().getName();
        beginLogFileName = beginLogFileName.substring(0, beginLogFileName.lastIndexOf("."));
        String endLogFileName = contextInfo.getEnd().getLogFile().getName();
        endLogFileName = endLogFileName.substring(0, endLogFileName.lastIndexOf("."));

        Date conditionBeginDateTime = searchCondition.getBeginDateTime();
        Date conditionEndDateTime = searchCondition.getEndDateTime();
        Date contextBeginDateTime = sdf.parse(beginLogFileName);
        Date contextEndDateTime = sdf.parse(endLogFileName);

        if (conditionBeginDateTime.after(contextEndDateTime)) {
            return false;
        } else if (conditionEndDateTime.before(contextBeginDateTime)) {
            return false;
        } else if (conditionBeginDateTime.before(contextBeginDateTime) || conditionEndDateTime.after(contextEndDateTime)) {
            if (exactlyBetweenTimeRange) {
                if (conditionBeginDateTime.before(contextBeginDateTime) && conditionEndDateTime.after(contextEndDateTime)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else if (allowOverTimeRange && conditionBeginDateTime.after(contextBeginDateTime) && conditionEndDateTime.before(contextEndDateTime)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFuzzySearch() {
        return fuzzySearch;
    }

    public KeyWordSearchEngine setFuzzySearch(boolean fuzzySearch) {
        this.fuzzySearch = fuzzySearch;
        return this;
    }

    public boolean isAllowOverTimeRange() {
        return allowOverTimeRange;
    }

    public KeyWordSearchEngine setAllowOverTimeRange(boolean allowOverTimeRange) {
        this.allowOverTimeRange = allowOverTimeRange;
        return this;
    }

    public boolean isExactlyBetweenTimeRange() {
        return exactlyBetweenTimeRange;
    }

    public KeyWordSearchEngine setExactlyBetweenTimeRange(boolean exactlyBetweenTimeRange) {
        this.exactlyBetweenTimeRange = exactlyBetweenTimeRange;
        return this;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public KeyWordSearchEngine setBaseDir(String baseDir) {
        this.baseDir = baseDir;
        return this;
    }

    public SearchCondition getSearchCondition() {
        return searchCondition;
    }

    public KeyWordSearchEngine setSearchCondition(SearchCondition searchCondition) {
        this.searchCondition = searchCondition;
        return this;
    }

    public Map<String, List<KeywordIndexBuilder.IndexInfo>> getKeywordIndex() {
        return keywordIndex;
    }

    public KeyWordSearchEngine setKeywordIndex(Map<String, List<KeywordIndexBuilder.IndexInfo>> keywordIndex) {
        this.keywordIndex = keywordIndex;
        return this;
    }

    public Map<Long, ContextIndexBuilder.ContextInfo> getContextIndex() {
        return contextIndex;
    }

    public KeyWordSearchEngine setContextIndex(Map<Long, ContextIndexBuilder.ContextInfo> contextIndex) {
        this.contextIndex = contextIndex;
        return this;
    }
}
