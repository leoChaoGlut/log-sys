package cn.yunyichina.log.common.entity.entity.dto;

import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/8 1:19
 * @Description:
 */
public class LogResult {

    private TreeSet<String> logRegionSet;
    private String contextStr;

    public TreeSet<String> getLogRegionSet() {
        return logRegionSet;
    }

    public LogResult setLogRegionSet(TreeSet<String> logRegionSet) {
        this.logRegionSet = logRegionSet;
        return this;
    }

    public String getContextStr() {
        return contextStr;
    }

    public LogResult setContextStr(String contextStr) {
        this.contextStr = contextStr;
        return this;
    }
}
