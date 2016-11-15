package cn.yunyichina.log.search.engine;

import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.search.engine.entity.SearchCondition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 11:23
 * @Description:
 */
public abstract class AbstractSearchEngine {

    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");

    /**
     * TODO 模糊搜索
     */
    protected boolean fuzzySearch = false;
    /**
     * 例: 若某个日志的上下文的时间段为[0,3],查询条件的时间段为[1,2],
     * if allowOverTimeRange -> match
     * else -> notMatch
     */
    protected boolean allowOverTimeRange = false;
    /**
     * true:搜索条件的时间区间必须为上下文时间区间的父区间
     * false:反之.
     */
    protected boolean exactlyBetweenTimeRange = false;
    protected String baseDir;
    protected SearchCondition searchCondition;
    protected List<ContextIndexBuilder.ContextInfo> matchedContextInfoList;

    /**
     * 时间区间交集判断
     *
     * @param contextInfo
     * @return
     * @throws ParseException
     */
    protected boolean inDateTimeRange(ContextIndexBuilder.ContextInfo contextInfo) throws ParseException {
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

    public AbstractSearchEngine setFuzzySearch(boolean fuzzySearch) {
        this.fuzzySearch = fuzzySearch;
        return this;
    }

    public boolean isAllowOverTimeRange() {
        return allowOverTimeRange;
    }

    public AbstractSearchEngine setAllowOverTimeRange(boolean allowOverTimeRange) {
        this.allowOverTimeRange = allowOverTimeRange;
        return this;
    }

    public boolean isExactlyBetweenTimeRange() {
        return exactlyBetweenTimeRange;
    }

    public AbstractSearchEngine setExactlyBetweenTimeRange(boolean exactlyBetweenTimeRange) {
        this.exactlyBetweenTimeRange = exactlyBetweenTimeRange;
        return this;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public AbstractSearchEngine setBaseDir(String baseDir) {
        this.baseDir = baseDir;
        return this;
    }

    public SearchCondition getSearchCondition() {
        return searchCondition;
    }

    public AbstractSearchEngine setSearchCondition(SearchCondition searchCondition) {
        this.searchCondition = searchCondition;
        return this;
    }
}
