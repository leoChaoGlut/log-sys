package cn.yunyichina.log.service.tracer.trace.linked;

import cn.yunyichina.log.service.tracer.trace.Trace;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.PriorityQueue;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 17:26
 * @Description:
 */
@Getter
@Setter
public class LinkedTrace extends Trace implements Serializable {
    private static final long serialVersionUID = 5423874889297882682L;

    private LinkedTraceNode rootTraceNode;
    private PriorityQueue<LinkedTraceNode> priorityQueue;

}
