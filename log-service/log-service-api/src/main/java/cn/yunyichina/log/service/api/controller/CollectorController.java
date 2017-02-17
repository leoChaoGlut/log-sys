package cn.yunyichina.log.service.api.controller;

import cn.yunyichina.log.common.entity.entity.dto.Response;
import cn.yunyichina.log.common.entity.entity.dto.TagSet;
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
    TagService service;

    @GetMapping("tag/{applicationName}")
    public Response getTagSet(
            @PathVariable String applicationName
    ) {
        try {
            logger.contextBegin("开始获取TagSet:" + applicationName);
            TagSet tagSet = service.getTagSet(applicationName);
            logger.contextEnd("获取到的TagSet:" + JSON.toJSONString(tagSet, true));
            return Response.success(tagSet);
        } catch (Exception e) {
            String errorMsg = "获取TagSet失败:" + e.getLocalizedMessage();
            logger.error(errorMsg, e);
            logger.contextEnd(errorMsg);
            return Response.failure(errorMsg);
        }
    }


    @PostMapping("addGroup")
    public Response addGroup(@RequestParam String groupName) {
        try {
            System.err.println("中文=======" + groupName);
            service.addGroup(groupName);
            return Response.success("新增分组成功");
        } catch (Exception e) {
            return Response.failure(e.getLocalizedMessage());
        }

    }

    @PostMapping("addCollector")
    public Response addCollector(@RequestParam String collectorName, Integer groupId, String serviceName) {
        try {
            service.addCollector(collectorName, groupId, serviceName);
            return Response.success("新增节点成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("addKeywordIndex")
    public Response addKeywordIndex(@RequestParam String keywordIndexName, Integer collectorId) {
        try {
            service.addKeywordIndex(keywordIndexName, collectorId);
            return Response.success("新增keyword索引成功");
        } catch (Exception e) {
            return Response.failure(e.getLocalizedMessage());
        }

    }

    @PostMapping("addKvIndex")
    public Response addKvIndex(@RequestParam String kvIndexName, Integer collectorId) {
        try {
            service.addKvIndex(kvIndexName, collectorId);
            return Response.success("新增keyValue索引成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("updateGroup")
    public Response updateGroup(@RequestParam Integer groupId, String groupName) {
        try {
            service.updateGroup(groupId, groupName);
            return Response.success("更改分组名称成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("updateCollector")
    public Response updateCollector(@RequestParam Integer collectorId, String collectorName) {
        try {
            service.updateCollector(collectorId, collectorName);
            return Response.success("更改节点名称成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("updateKeywordIndex")
    public Response updateKeywordIndex(@RequestParam Integer collectorId, Integer keywordIndexId, String keyword) {
        try {
            service.updateKeywordIndex(collectorId, keywordIndexId, keyword);
            return Response.success("更改keyword索引名称成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("updateKvIndex")
    public Response updateKvIndex(@RequestParam Integer collectorId, Integer kvIndexId, String key) {
        try {
            service.updateKvIndex(collectorId, kvIndexId, key);
            return Response.success("更改kv索引名称成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
    }

}
