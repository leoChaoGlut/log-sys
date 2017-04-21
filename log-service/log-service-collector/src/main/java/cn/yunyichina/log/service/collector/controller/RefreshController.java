package cn.yunyichina.log.service.collector.controller;

import cn.yunyichina.log.common.base.AbstractController;
import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.common.util.ResponseUtil;
import cn.yunyichina.log.service.collector.client.CollectorServiceClient;
import cn.yunyichina.log.service.collector.service.CacheService;
import cn.yunyichina.log.service.collector.task.ScheduleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Jonven on 2017/03/10.
 */
@RefreshScope
@RestController
@RequestMapping("refresh")
public class RefreshController extends AbstractController {

    final Logger logger = LoggerFactory.getLogger(RefreshController.class);

    @Autowired
    CollectorServiceClient collectorServiceClient;
    @Autowired
    CacheService cacheService;
    @Autowired
    ScheduleTask scheduleTask;

    @Value("${eureka.instance.ipAddress}")
    String ip;
    @Value("${server.port}")
    String port;
    @Value("${spring.application.name}")
    String applicationName;

    @PostMapping("collectorInfo")
    ResponseBodyDTO collectorInfo() {
        logger.info("刷新配置开始:" + ip + " - " + port + " - " + applicationName);
        ResponseBodyDTO<CollectorDO> responseBody = collectorServiceClient.registerAndGetData(ip, port, applicationName);
        CollectorDO collector = ResponseUtil.getResult(responseBody);
        cacheService.setCollector(collector);
        scheduleTask.buildCollectedItemList();
        logger.info("刷新配置成功");
        return ResponseBodyDTO.ok();
    }

}
