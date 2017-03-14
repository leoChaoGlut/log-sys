package cn.yunyichina.log.service.collector.controller;

import cn.yunyichina.log.common.entity.dto.ResponseDTO;
import cn.yunyichina.log.service.collector.lifecircle.ApplicationReadyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Jonven on 2017/03/10.
 */
@RestController
@RequestMapping("refresh")
public class RefreshController {

    final Logger logger = LoggerFactory.getLogger(RefreshController.class);

    @Autowired
    ApplicationReadyListener applicationReadyListener;

    @PostMapping("collectorInfo")
    ResponseDTO collectorInfo() {
        try {
            logger.info("刷新配置开始");
            applicationReadyListener.registerAndGetData();
            logger.info("刷新配置成功");
            return ResponseDTO.ok();
        } catch (Exception e) {
            logger.error("刷新collector配置失败", e);
            return ResponseDTO.error(e.getMessage());
        }
    }
}
