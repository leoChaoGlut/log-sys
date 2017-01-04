package cn.yunyichina.log.service.collector.controller;

import cn.yunyichina.log.component.entity.dto.Response;
import cn.yunyichina.log.service.collector.entity.dto.TagSet;
import cn.yunyichina.log.service.collector.service.ConfigService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/29 16:46
 * @Description:
 */
@RestController
@RequestMapping("config")
public class ConfigController {

    @Autowired
    ConfigService configService;

    @PostMapping("refresh")
    Response refresh(@RequestBody String jsonParam) {
        try {
            TagSet tagSet = JSON.parseObject(jsonParam, TagSet.class);
            configService.resetTag(tagSet);
            return Response.success();
        } catch (Exception e) {
            return Response.failure(e.getLocalizedMessage());
        }
    }

}
