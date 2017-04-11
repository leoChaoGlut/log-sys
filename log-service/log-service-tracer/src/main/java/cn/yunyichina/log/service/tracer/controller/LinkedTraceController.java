package cn.yunyichina.log.service.tracer.controller;

import cn.yunyichina.log.common.base.AbstractController;
import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.tracer.service.TraceService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 15:19
 * @Description:
 */
@RestController
@RequestMapping("trace/linked")
public class LinkedTraceController extends AbstractController {
    final Logger logger = LoggerFactory.getLogger(LinkedTraceController.class);

    @Autowired
    TraceService traceService;

    @PostMapping("append/linkednode")
    ResponseBodyDTO appendLinkedNode(
            LinkedTraceNode.DTO linkedTraceNodeDTO
    ) throws ParseException {
        logger.info(JSON.toJSONString(linkedTraceNodeDTO, true));
        traceService.appendLinkedNode(LinkedTraceNode.parseBy(linkedTraceNodeDTO));
        return ResponseBodyDTO.ok();
    }

    @PostMapping("get/by/contextId")
    ResponseBodyDTO getTraceByContextId(
            String contextId
    ) {
        logger.info(JSON.toJSONString(contextId, true));
        TreeSet<LinkedTraceNode> traceNodeSet = traceService.getTraceByContextId(contextId);
        return ResponseBodyDTO.ok(traceNodeSet);
    }

    @PostMapping("get/by/traceId")
    ResponseBodyDTO getTraceByTraceId(
            String traceId
    ) {
        logger.info(JSON.toJSONString(traceId, true));
        TreeSet<LinkedTraceNode> traceNodeSet = traceService.getTraceByTraceId(traceId);
        return ResponseBodyDTO.ok(traceNodeSet);
    }


}
