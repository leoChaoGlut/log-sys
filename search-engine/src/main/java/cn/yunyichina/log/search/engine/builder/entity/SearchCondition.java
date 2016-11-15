package cn.yunyichina.log.search.engine.builder.entity;

import java.util.Date;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 9:44
 * @Description:
 */
public class SearchCondition {
    private Date beginDateTime;
    private Date endDateTime;
    private String keyword;
    private String key;
    private String value;

    private String threadName;
    private String className;
    private String methodName;


    private int searchWay;

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
}
