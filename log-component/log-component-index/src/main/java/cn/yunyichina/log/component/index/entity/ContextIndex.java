package cn.yunyichina.log.component.index.entity;

import cn.yunyichina.log.component.index.base.AbstractIndex;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/27 18:56
 * @Description:
 */
public class ContextIndex extends AbstractIndex implements Serializable {

    private static final long serialVersionUID = 7464183768648638516L;

    public ContextIndex() {
    }

    public ContextIndex(File logFile, int indexOfLogFile) {
        this.logFile = logFile;
        this.indexOfLogFile = indexOfLogFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContextIndex contextIndex = (ContextIndex) o;
        return indexOfLogFile == contextIndex.indexOfLogFile &&
                Objects.equals(logFile, contextIndex.logFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logFile, indexOfLogFile);
    }

}