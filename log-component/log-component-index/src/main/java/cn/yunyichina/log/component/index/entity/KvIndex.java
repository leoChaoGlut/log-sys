package cn.yunyichina.log.component.index.entity;

import cn.yunyichina.log.component.index.base.AbstractIndex;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/27 18:20
 * @Description:
 */
@Getter
@Setter
public class KvIndex extends AbstractIndex implements Serializable {

    private static final long serialVersionUID = 3099478568862317302L;

    private Long contextCount;

    public KvIndex() {
    }

    public KvIndex(File logFile, int indexOfLogFile, Long contextCount) {
        this.logFile = logFile;
        this.indexOfLogFile = indexOfLogFile;
        this.contextCount = contextCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KvIndex kvIndex = (KvIndex) o;
        return indexOfLogFile == kvIndex.indexOfLogFile &&
                Objects.equals(logFile, kvIndex.logFile) &&
                Objects.equals(contextCount, kvIndex.contextCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logFile, indexOfLogFile, contextCount);
    }

}
