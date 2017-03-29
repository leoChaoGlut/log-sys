package cn.yunyichina.log.service.tracer.trace;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 17:25
 * @Description:
 */
@Getter
@Setter
public abstract class Trace implements Serializable {
    private static final long serialVersionUID = 6962616252300303690L;

    protected String traceId;
}
