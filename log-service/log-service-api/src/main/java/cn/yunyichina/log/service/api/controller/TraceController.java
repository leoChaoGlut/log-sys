package cn.yunyichina.log.service.api.controller;

import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.api.service.TraceService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/6 16:13
 * @Description:
 */
@RestController
@RequestMapping("trace")
public class TraceController {

    final Logger logger = LoggerFactory.getLogger(TraceController.class);

    @Autowired
    TraceService traceService;

    @PostMapping("log/details")
    ResponseBodyDTO logDetails(
            LinkedTraceNode linkedTraceNode
    ) {
        logger.info(JSON.toJSONString(linkedTraceNode, true));
        String logDetails = traceService.logDetails(linkedTraceNode);
        return ResponseBodyDTO.ok(logDetails);
    }
}
