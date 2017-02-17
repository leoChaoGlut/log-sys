package cn.yunyichina.log.service.searcher.entity;

import cn.yunyichina.log.common.entity.entity.dto.SearchCondition;

/**
 * Created by Jonven on 2017/1/13.
 */
public class DownloadLog {

    private String[] logNames;

    private SearchCondition condition;

    public DownloadLog() {
    }

    public DownloadLog(String[] logNames) {
        this.logNames = logNames;
    }

    public String[] getLogNames() {
        return logNames;
    }

    public void setLogNames(String[] logNames) {
        this.logNames = logNames;
    }

    public SearchCondition getCondition() {
        return condition;
    }

    public void setCondition(SearchCondition condition) {
        this.condition = condition;
    }
}
