package cn.yunyichina.log.service.tracer.controller;

import cn.yunyichina.log.common.base.AbstractController;
import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.tracer.service.LinkedTraceService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 15:19
 * @Description: 由于tracer并发量会比较大, 所以需要部署2套实例, 一个只读, 一个只写.
 */
@RestController
@RequestMapping("trace/linked")
public class LinkedTraceController extends AbstractController {
    final Logger logger = LoggerFactory.getLogger(LinkedTraceController.class);

    @Autowired
    LinkedTraceService linkedTraceService;

    @PostMapping("read/by/contextId/collectorId")
    ResponseBodyDTO<TreeSet<LinkedTraceNode>> getTraceByContextId(
            String contextId,
            Integer collectorId
    ) {
        logger.info(JSON.toJSONString(contextId, true));
        TreeSet<LinkedTraceNode> traceNodeSet = linkedTraceService.getTraceByContextId(contextId, collectorId);
        return ResponseBodyDTO.ok(traceNodeSet);
    }

    @PostMapping("read/by/traceId/collectorId")
    ResponseBodyDTO<TreeSet<LinkedTraceNode>> getTraceByTraceId(
            String traceId,
            Integer collectorId
    ) {
        logger.info(JSON.toJSONString(traceId, true));
        TreeSet<LinkedTraceNode> traceNodeSet = linkedTraceService.getTraceByTraceId(traceId, collectorId);
        return ResponseBodyDTO.ok(traceNodeSet);
    }

    @PostMapping("write/linkednode/batch")
    ResponseBodyDTO appendLinkedNodeBatch(
            @RequestBody List<LinkedTraceNode> linkedTraceNodeList
    ) {
        logger.info(linkedTraceNodeList.size() + "");
        linkedTraceService.appendLinkedNodeBatch(linkedTraceNodeList);
        logger.info("before return");
        return null;//客户端不需要关心返回值
    }

}
