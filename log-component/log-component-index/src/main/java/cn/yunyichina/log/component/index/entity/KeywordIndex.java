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
public class KeywordIndex extends AbstractIndex implements Serializable {

    private static final long serialVersionUID = 5922961444464197133L;

    private Long contextCount;

    public KeywordIndex() {
    }

    public KeywordIndex(File logFile, int indexOfLogFile, Long contextCount) {
        this.logFile = logFile;
        this.indexOfLogFile = indexOfLogFile;
        this.contextCount = contextCount;
    }

    /**
     * 相同上下文count,相同文件 即视为相同
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeywordIndex keywordIndex = (KeywordIndex) o;
        return Objects.equals(logFile, keywordIndex.logFile) &&
                Objects.equals(contextCount, keywordIndex.contextCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logFile, contextCount);
    }

}
