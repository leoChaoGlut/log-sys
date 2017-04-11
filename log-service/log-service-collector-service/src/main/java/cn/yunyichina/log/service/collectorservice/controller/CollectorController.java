package cn.yunyichina.log.service.collectorservice.controller;

import cn.yunyichina.log.common.base.AbstractController;
import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.common.exception.CollectorException;
import cn.yunyichina.log.service.collectorservice.service.CollectorService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 9:50
 * @Description:
 */
@RestController
@RequestMapping("collector")
public class CollectorController extends AbstractController {

    final Logger logger = LoggerFactory.getLogger(CollectorController.class);

    @Autowired
    CollectorService collectorService;

    @PostMapping("register")
    ResponseBodyDTO<CollectorDO> registerAndGetData(
            String ip,
            String port,
            String applicationName
    ) {
        logger.info(ip + " - " + port + " - " + applicationName);
        CollectorDO collector = collectorService.registerAndGetData(applicationName, ip, port);
        logger.info(JSON.toJSONString(collector, true));
        return ResponseBodyDTO.ok(collector);
    }

    @GetMapping("all")
    ResponseBodyDTO<List<CollectorDO>> listAllCollector() throws CollectorException {
        logger.info("listAllCollector");
        List<CollectorDO> collectorList = collectorService.listAllCollector();
        logger.info(JSON.toJSONString(collectorList, true));
        return ResponseBodyDTO.ok(collectorList);
    }

    @PostMapping("get/by/applicationName")
    ResponseBodyDTO<Map<String, Object>> getByApplicationName(
            String applicationName
    ) throws CollectorException {
        logger.info(applicationName);
        Map<String, Object> resultMap = collectorService.getByApplicationName(applicationName);
        logger.info(JSON.toJSONString(resultMap, true));
        return ResponseBodyDTO.ok(resultMap);
    }

}
