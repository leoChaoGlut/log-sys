package cn.yunyichina.log.service.searcher.entity;

/**
 * Created by Jonven on 2017/1/13.
 */
public class DownloadLog {

    private String[] logNames;

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
}
