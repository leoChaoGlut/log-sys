package cn.yy.log.entity.vo;

import java.io.File;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/1 17:38
 * @Description:
 */
public class LogPair {
    private File logFile;
    private File indexFile;

    public LogPair() {
    }

    public File getLogFile() {
        return logFile;
    }

    public LogPair setLogFile(File logFile) {
        this.logFile = logFile;
        return this;
    }

    public File getIndexFile() {
        return indexFile;
    }

    public LogPair setIndexFile(File indexFile) {
        this.indexFile = indexFile;
        return this;
    }
}
