package cn.yunyichina.log.component.index.base;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/27 18:31
 * @Description:
 */
@Getter
@Setter
public abstract class AbstractBuilder {
    protected File logFile;
    protected String logContent;

}
