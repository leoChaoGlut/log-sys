package cn.yunyichina.log.service.tracer.trace.linked;

import cn.yunyichina.log.service.tracer.trace.TraceNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 17:28
 * @Description:
 */
@Getter
@Setter
@ToString(callSuper = true)
public class LinkedTraceNode extends TraceNode implements Serializable, Comparable<LinkedTraceNode> {
    private static final long serialVersionUID = 3301244492367991817L;

    /**
     * desc
     *
     * @param that
     * @return
     */
    @Override
    public int compareTo(LinkedTraceNode that) {
        return this.timestamp.compareTo(that.timestamp);
    }

    @Override
    public LinkedTraceNode setContextCount(Long contextCount) {
        super.setContextCount(contextCount);
        return this;
    }

    @Override
    public LinkedTraceNode setServiceId(String serviceId) {
        super.setServiceId(serviceId);
        return this;
    }

    @Override
    public LinkedTraceNode setTimestamp(Long timestamp) {
        super.setTimestamp(timestamp);
        return this;
    }

    @Override
    public LinkedTraceNode setTraceId(String traceId) {
        super.setTraceId(traceId);
        return this;
    }
}
