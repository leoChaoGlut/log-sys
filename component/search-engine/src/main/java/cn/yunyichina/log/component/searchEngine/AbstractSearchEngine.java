package cn.yunyichina.log.component.searchEngine;

import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 11:23
 * @Description:
 */
public abstract class AbstractSearchEngine {

    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

    /**
     * 是否模糊搜索
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
    protected Set<ContextIndexBuilder.ContextInfo> matchedContextInfoSet;

    /**
     * 时间区间交集判断
     *
     * @param contextInfo
     * @return
     * @throws ParseException
     */
    protected boolean inDateTimeRange(ContextIndexBuilder.ContextInfo contextInfo) {
        try {
            if (contextInfo == null) {
                return false;
            } else {
                ContextIndexBuilder.IndexInfo contextInfoBegin = contextInfo.getBegin();
                File contextInfoBeginLogFile = contextInfoBegin.getLogFile();
                String beginLogFileName = contextInfoBeginLogFile.getName();
                beginLogFileName = beginLogFileName.substring(0, beginLogFileName.lastIndexOf("."));

                ContextIndexBuilder.IndexInfo contextInfoEnd = contextInfo.getEnd();
                File contextInfoEndLogFile = contextInfoEnd.getLogFile();
                String endLogFileName = contextInfoEndLogFile.getName();
                endLogFileName = endLogFileName.substring(0, endLogFileName.lastIndexOf("."));

                Date conditionBeginDateTime = searchCondition.getBeginDateTime();
                Date conditionEndDateTime = searchCondition.getEndDateTime();
                Date contextBeginDateTime = sdf.parse(beginLogFileName);
                Date contextEndDateTime = sdf.parse(endLogFileName);

                if (conditionBeginDateTime.after(contextEndDateTime)) {
                    return false;
                } else if (conditionEndDateTime.before(contextBeginDateTime)) {
                    return false;
                } else if (0 >= conditionBeginDateTime.compareTo(contextBeginDateTime) || 0 <= conditionEndDateTime.compareTo(contextEndDateTime)) {
                    if (exactlyBetweenTimeRange) {
                        if (0 >= conditionBeginDateTime.compareTo(contextBeginDateTime) && 0 <= conditionEndDateTime.compareTo(contextEndDateTime)) {
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

        } catch (Exception e) {//有一些ContextInfo可能是由于上下文跨文件的缘故,导致在某个时期它是缺陷的,缺了begin或者end.所以可能会抛出NullPointerException.但是在索引聚合的时候,可以保证数据一致性.
            e.printStackTrace();
//            TODO 这里是否需要打印异常?如果出现异常,默认返回false
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
