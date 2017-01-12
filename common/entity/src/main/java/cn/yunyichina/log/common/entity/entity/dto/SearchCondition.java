package cn.yunyichina.log.common.entity.entity.dto;


import cn.yunyichina.log.common.entity.entity.po.Collector;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 9:44
 * @Description:
 */
public class SearchCondition {
    /**
     * @See cn.yunyichina.log.search.center.constant.SearchEngineType
     */
    private int searchEngineType;
    private boolean fuzzy;
    private Date beginDateTime;
    private Date endDateTime;
    private String noIndexKeyword;
    private String keyword;
    private String key;
    private String value;

    //Not been used yet
    private String threadName;
    private String className;
    private String methodName;

    private String hospitalLetter;

    private Collector collector;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public String toString() {
        String beginDateTimeStr = beginDateTime == null ? "" : sdf.format(beginDateTime);
        String endDateTimeStr = endDateTime == null ? "" : sdf.format(endDateTime);
        String collectorName = collector == null ? "" : collector.getName();
        return "" + searchEngineType +
                fuzzy +
                beginDateTimeStr +
                endDateTimeStr +
                noIndexKeyword +
                keyword +
                key +
                value +
                collectorName;
    }

    public String getBeginDateTimeStr() {
        return sdf.format(this.beginDateTime);
    }

    public String getEndDateTimeStr() {
        return sdf.format(this.endDateTime);
    }


    public Date getBeginDateTime() {
        return beginDateTime;
    }

    public SearchCondition setBeginDateTime(Date beginDateTime) {
        this.beginDateTime = beginDateTime;
        return this;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public SearchCondition setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public SearchCondition setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public String getKey() {
        return key;
    }

    public SearchCondition setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public SearchCondition setValue(String value) {
        this.value = value;
        return this;
    }

    public String getThreadName() {
        return threadName;
    }

    public SearchCondition setThreadName(String threadName) {
        this.threadName = threadName;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public SearchCondition setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public SearchCondition setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public int getSearchEngineType() {
        return searchEngineType;
    }

    public SearchCondition setSearchEngineType(int searchEngineType) {
        this.searchEngineType = searchEngineType;
        return this;
    }

    public boolean isFuzzy() {
        return fuzzy;
    }

    public SearchCondition setFuzzy(boolean fuzzy) {
        this.fuzzy = fuzzy;
        return this;
    }

    public String getHospitalLetter() {
        return hospitalLetter;
    }

    public SearchCondition setHospitalLetter(String hospitalLetter) {
        this.hospitalLetter = hospitalLetter;
        return this;
    }

    public Collector getCollector() {
        return collector;
    }

    public SearchCondition setCollector(Collector collector) {
        this.collector = collector;
        return this;
    }

    public String getNoIndexKeyword() {
        return noIndexKeyword;
    }

    public SearchCondition setNoIndexKeyword(String noIndexKeyword) {
        this.noIndexKeyword = noIndexKeyword;
        return this;
    }

}
