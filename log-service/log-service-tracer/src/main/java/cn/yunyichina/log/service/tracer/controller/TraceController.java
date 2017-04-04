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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 15:19
 * @Description:
 */
@RestController
@RequestMapping("trace")
public class TraceController extends AbstractController {
    final Logger logger = LoggerFactory.getLogger(TraceController.class);

    @Autowired
    TraceService traceService;

    @PostMapping("append/linkednode")
    ResponseBodyDTO appendLinkedNode(
            LinkedTraceNode linkedTraceNode
    ) {
        logger.info(JSON.toJSONString(linkedTraceNode, true));
        traceService.appendLinkedNode(linkedTraceNode);
        return ResponseBodyDTO.ok();
    }

    @PostMapping("get/by/contextId")
    ResponseBodyDTO getTraceByContextId(
            String contextId
    ) {
        logger.info(JSON.toJSONString(contextId, true));
        Set<String> contextIdSet = traceService.getTraceByContextId(contextId);
        return ResponseBodyDTO.ok(contextIdSet);
    }

    @PostMapping("get/by/traceId")
    ResponseBodyDTO getTraceByTraceId(
            String traceId
    ) {
        logger.info(JSON.toJSONString(traceId, true));
        Set<String> contextIdSet = traceService.getTraceByTraceId(traceId);
        return ResponseBodyDTO.ok(contextIdSet);
    }


}
