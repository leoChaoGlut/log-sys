package cn.yunyichina.log.service.tracer.trace;

import cn.yunyichina.log.service.tracer.trace.linked.LinkedTrace;
import cn.yunyichina.log.service.tracer.trace.linked.LinkedTraceNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 16:52
 * @Description:
 */
@Getter
@Setter
@Component
public class TraceManager implements Serializable {
    private static final long serialVersionUID = 2281828414506633325L;
    /**
     * key: traceId
     * value: {@link LinkedTrace}
     */
    private ConcurrentHashMap<String, LinkedTrace> linkedTraceMap = new ConcurrentHashMap<>();

    public void addLinkedTraceNode(LinkedTraceNode linkedTraceNode) {
        String traceId = linkedTraceNode.getTraceId();
        LinkedTrace linkedTrace = linkedTraceMap.get(traceId);
        if (null == linkedTrace) {
            linkedTrace = new LinkedTrace()
                    .setTraceId(traceId);
            linkedTrace.addNode(linkedTraceNode);
            linkedTraceMap.put(traceId, linkedTrace);
        } else {
            linkedTrace.addNode(linkedTraceNode);
        }
    }
}
