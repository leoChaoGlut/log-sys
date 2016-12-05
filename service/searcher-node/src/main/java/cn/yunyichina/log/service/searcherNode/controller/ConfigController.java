package cn.yunyichina.log.service.searcherNode.controller;

import cn.yunyichina.log.common.entity.dto.Response;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/update")
    public Response updateConfig() {
        return Response.success("2222222222222");
    }


}
