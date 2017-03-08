package cn.yunyichina.log.component.index.base;

import java.io.File;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/27 18:31
 * @Description:
 */
public abstract class AbstractBuilder {

    protected File logFile;
    protected String logContent;

    public File getLogFile() {
        return logFile;
    }

    public AbstractBuilder setLogFile(File logFile) {
        this.logFile = logFile;
        return this;
    }

    public String getLogContent() {
        return logContent;
    }

    public AbstractBuilder setLogContent(String logContent) {
        this.logContent = logContent;
        return this;
    }
}
