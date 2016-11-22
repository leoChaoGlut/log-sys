package cn.yunyichina.log.service.collectionNode.controller;

import cn.yunyichina.log.common.entity.dto.Response;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 10:24
 * @Description:
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @PutMapping("/update")
    public Response updateConfig() {

        return Response.failure("");
    }


}
