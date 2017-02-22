package cn.yunyichina.log.service.api.controller;

import cn.yunyichina.log.common.entity.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.entity.dto.ResponseDTO;
import cn.yunyichina.log.common.entity.entity.dto.TagSet;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.api.service.CollectorService;
import cn.yunyichina.log.service.api.service.TagService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    CollectorService collectorService;

    @GetMapping("list/collected/item/by/{applicationName}")
    ResponseDTO listCollectedItem(
            @PathVariable String applicationName
    ) {
        try {
            logger.contextBegin("listCollectedItem, applicationName:" + applicationName);
            List<CollectedItemDO> collectedItemDOList = collectorService.listCollectedItem(applicationName);
            logger.contextEnd("listCollectedItem 成功:" + JSON.toJSONString(collectedItemDOList, true));
            return ResponseDTO.success(collectedItemDOList);
        } catch (Exception e) {
            String errorMsg = "listCollectedItem 失败:" + e.getLocalizedMessage();
            logger.error(errorMsg, e);
            logger.contextEnd(errorMsg);
            return ResponseDTO.failure(errorMsg);
        }
    }

    @GetMapping("tag/{applicationName}")
    public ResponseDTO getTagSet(
            @PathVariable String applicationName
    ) {
        try {
            logger.contextBegin("开始获取TagSet:" + applicationName);
            TagSet tagSet = tagService.getTagSet(applicationName);
            logger.contextEnd("获取到的TagSet:" + JSON.toJSONString(tagSet, true));
            return ResponseDTO.success(tagSet);
        } catch (Exception e) {
            String errorMsg = "获取TagSet失败:" + e.getLocalizedMessage();
            logger.error(errorMsg, e);
            logger.contextEnd(errorMsg);
            return ResponseDTO.failure(errorMsg);
        }
    }


    @PostMapping("addGroup")
    public ResponseDTO addGroup(@RequestParam String groupName) {
        try {
            System.err.println("中文=======" + groupName);
            tagService.addGroup(groupName);
            return ResponseDTO.success("新增分组成功");
        } catch (Exception e) {
            return ResponseDTO.failure(e.getLocalizedMessage());
        }

    }

    @PostMapping("addCollector")
    public ResponseDTO addCollector(@RequestParam String collectorName, Integer groupId, String serviceName) {
        try {
            tagService.addCollector(collectorName, groupId, serviceName);
            return ResponseDTO.success("新增节点成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("addKeywordIndex")
    public ResponseDTO addKeywordIndex(@RequestParam String keywordIndexName, Integer collectorId) {
        try {
            tagService.addKeywordIndex(keywordIndexName, collectorId);
            return ResponseDTO.success("新增keyword索引成功");
        } catch (Exception e) {
            return ResponseDTO.failure(e.getLocalizedMessage());
        }

    }

    @PostMapping("addKvIndex")
    public ResponseDTO addKvIndex(@RequestParam String kvIndexName, Integer collectorId) {
        try {
            tagService.addKvIndex(kvIndexName, collectorId);
            return ResponseDTO.success("新增keyValue索引成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("updateGroup")
    public ResponseDTO updateGroup(@RequestParam Integer groupId, String groupName) {
        try {
            tagService.updateGroup(groupId, groupName);
            return ResponseDTO.success("更改分组名称成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("updateCollector")
    public ResponseDTO updateCollector(@RequestParam Integer collectorId, String collectorName) {
        try {
            tagService.updateCollector(collectorId, collectorName);
            return ResponseDTO.success("更改节点名称成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("updateKeywordIndex")
    public ResponseDTO updateKeywordIndex(@RequestParam Integer collectorId, Integer keywordIndexId, String keyword) {
        try {
            tagService.updateKeywordIndex(collectorId, keywordIndexId, keyword);
            return ResponseDTO.success("更改keyword索引名称成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("updateKvIndex")
    public ResponseDTO updateKvIndex(@RequestParam Integer collectorId, Integer kvIndexId, String key) {
        try {
            tagService.updateKvIndex(collectorId, kvIndexId, key);
            return ResponseDTO.success("更改kv索引名称成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.failure(e.getLocalizedMessage());
        }
    }

}
