package cn.yunyichina.log.service.collectorservice.controller;

import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.dto.ResponseDTO;
import cn.yunyichina.log.service.collectorservice.service.CollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 9:50
 * @Description:
 */
@RestController
@RequestMapping("collector")
public class CollectorController {

    @Autowired
    CollectorService collectorService;

    @PostMapping("register")
    ResponseDTO registerAndGetData(
            String ip,
            String port,
            String applicationName
    ) {
        try {
            CollectorDO collector = collectorService.registerAndGetData(applicationName, ip, port);
            return ResponseDTO.ok(collector);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.error(e.getMessage());
        }
    }

    @GetMapping("all")
    ResponseDTO listAllCollector() {
        try {
            List<CollectorDO> collectorList = collectorService.listAllCollector();
            return ResponseDTO.ok(collectorList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.error(e.getMessage());
        }
    }
}
