package cn.yunyichina.log.service.tracer.service;

import cn.yunyichina.log.common.base.AbstractService;
import cn.yunyichina.log.service.tracer.trace.TraceManager;
import cn.yunyichina.log.service.tracer.trace.linked.LinkedTraceNode;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/29 18:41
 * @Description:
 */
@Service
public class TraceService extends AbstractService {

    @Autowired
    TraceManager traceManager;

    public void addLinkedTraceNode(LinkedTraceNode linkedTraceNode) {
        traceManager.addLinkedTraceNode(linkedTraceNode);
        System.err.println(JSON.toJSONString(traceManager.getLinkedTraceMap(), true));
    }
}
