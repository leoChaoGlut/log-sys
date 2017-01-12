package cn.yunyichina.log.service.api.controller;

import cn.yunyichina.log.common.entity.entity.dto.Response;
import cn.yunyichina.log.common.entity.entity.dto.TagSet;
import cn.yunyichina.log.common.entity.entity.po.KeyValueTag;
import cn.yunyichina.log.common.entity.entity.po.KeywordTag;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.api.service.TagService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/6 17:39
 * @Description:
 */
@RestController
@RequestMapping("collector")
public class CollectorController {

    final LoggerWrapper logger = LoggerWrapper.getLogger(CollectorController.class);

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

    @PostMapping("addKeyword")
    public Response addKeyword(@RequestBody String json) {
        try {
            KeywordTag keywordTag = JSON.parseObject(json, KeywordTag.class);
            logger.contextBegin("开始新增keyword:" + JSON.toJSONString(keywordTag, true));
            TagSet tagSet = tagService.addKeyword(keywordTag);
            logger.contextEnd("获取到的TagSet:" + JSON.toJSONString(tagSet, true));
            return Response.success(tagSet);
        } catch (Exception e) {
            String errorMsg = "获取TagSet失败:" + e.getLocalizedMessage();
            logger.error(errorMsg, e);
            logger.contextEnd(errorMsg);
            return Response.failure(errorMsg);
        }
    }

    @PostMapping("addKvTag")
    public Response addKvTag(@RequestBody String json) {
        try {
            KeyValueTag keyValueTag = JSON.parseObject(json, KeyValueTag.class);
            logger.contextBegin("开始新增kvTag:" + JSON.toJSONString(keyValueTag, true));
            TagSet tagSet = tagService.addKvTag(keyValueTag);
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
