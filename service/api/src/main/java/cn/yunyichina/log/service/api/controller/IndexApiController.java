package cn.yunyichina.log.service.api.controller;

import cn.yunyichina.log.component.entity.dto.Response;
import cn.yunyichina.log.service.api.service.IndexApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Jonven on 2017/1/4.
 */
//@RestController
//@RequestMapping("/indexApi")
public class IndexApiController {

    @Autowired
    IndexApiService service;

    @PostMapping("addGroup")
    public Response addGroup(@RequestParam String groupName) {
        try {
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
