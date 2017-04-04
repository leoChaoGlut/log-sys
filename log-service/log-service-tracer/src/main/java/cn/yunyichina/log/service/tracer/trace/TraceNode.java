package cn.yunyichina.log.service.tracer.trace;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 17:27
 * @Description:
 */
@Getter
@Setter
@ToString
public abstract class TraceNode implements Serializable {
    private static final long serialVersionUID = -2560457016778910734L;
    protected String contextId;
    /**
     * 整条日志链路的唯一id
     */
    protected String traceId;
    protected Long timestamp;
    /**
     * ip + port, 或 application name
     */
    protected String serviceId;

}
