package cn.yunyichina.log.service.tracer.controller;

import cn.yunyichina.log.common.base.AbstractController;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.tracer.service.TraceService;
import cn.yunyichina.log.service.tracer.trace.linked.LinkedTraceNode;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 15:19
 * @Description:
 */
@RestController
public class TraceController extends AbstractController {
    final Logger logger = LoggerFactory.getLogger(TraceController.class);

    @Autowired
    TraceService traceService;

    @PostMapping
    ResponseBodyDTO addLinkedTraceNode(
            LinkedTraceNode linkedTraceNode
    ) {
        logger.info(JSON.toJSONString(linkedTraceNode, true));
        traceService.addLinkedTraceNode(linkedTraceNode);
        return ResponseBodyDTO.ok();
    }
}
