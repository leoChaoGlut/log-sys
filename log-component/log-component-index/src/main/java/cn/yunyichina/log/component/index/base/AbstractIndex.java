package cn.yunyichina.log.component.index.base;

import java.io.File;
import java.io.Serializable;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/28 11:01
 * @Description:
 */
public abstract class AbstractIndex implements Serializable {

    private static final long serialVersionUID = 3382721186738490900L;
    
    protected File logFile;
    protected int indexOfLogFile;

    public File getLogFile() {
        return logFile;
    }

    public AbstractIndex setLogFile(File logFile) {
        this.logFile = logFile;
        return this;
    }

    public int getIndexOfLogFile() {
        return indexOfLogFile;
    }

    public AbstractIndex setIndexOfLogFile(int indexOfLogFile) {
        this.indexOfLogFile = indexOfLogFile;
        return this;
    }
}
