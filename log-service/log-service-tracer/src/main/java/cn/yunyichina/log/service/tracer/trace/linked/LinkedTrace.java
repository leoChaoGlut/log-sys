package cn.yunyichina.log.service.tracer.trace.linked;

import cn.yunyichina.log.service.tracer.trace.ITrace;
import cn.yunyichina.log.service.tracer.trace.Trace;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 17:26
 * @Description:
 */
@Getter
@Setter
public class LinkedTrace extends Trace implements ITrace<LinkedTraceNode>, Serializable {
    private static final long serialVersionUID = 5423874889297882682L;

    protected String traceId;
    private PriorityBlockingQueue<LinkedTraceNode> priorityQueue = new PriorityBlockingQueue<>();

    @Override
    public void addNode(LinkedTraceNode linkedTraceNode) {
        this.priorityQueue.add(linkedTraceNode);
    }

    @Override
    public LinkedTrace setTraceId(String traceId) {
        super.setTraceId(traceId);
        return this;
    }

}
