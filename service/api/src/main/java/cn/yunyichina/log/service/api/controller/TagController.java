package cn.yunyichina.log.service.api.controller;

import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.component.entity.dto.Response;
import cn.yunyichina.log.component.entity.dto.TagSet;
import cn.yunyichina.log.service.api.service.TagService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/6 17:39
 * @Description:
 */
@RestController
public class TagController {

    final LoggerWrapper logger = LoggerWrapper.getLogger(TagController.class);

    @Autowired
    TagService tagService;

    @GetMapping("tag/{applicationName}")
    public Response getTagSet(
            @PathVariable String applicationName
    ) {
        try {
            logger.contextBegin("开始获取TagSet:" + applicationName);
            TagSet tagSet = tagService.getTagSet(applicationName);
            logger.contextEnd("获取到的TagSet:" + JSON.toJSONString(tagSet, true));
            return Response.success(tagSet);
        } catch (Exception e) {
            String errorMsg = "获取TagSet失败:" + e.getLocalizedMessage();
            logger.error(errorMsg, e);
            logger.contextEnd(errorMsg);
            return Response.failure(errorMsg);
        }
    }

}
