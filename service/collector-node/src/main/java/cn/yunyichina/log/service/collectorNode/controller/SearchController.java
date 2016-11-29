package cn.yunyichina.log.service.collectorNode.controller;

import cn.yunyichina.log.common.entity.dto.Response;
import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.service.collectorNode.service.SearchService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 9:36
 * @Description:
 */
@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @PostMapping("realtime")
    public Response realtime(@RequestBody String jsonParam) {
        try {
            SearchCondition condition = JSON.parseObject(jsonParam, SearchCondition.class);
            List<String> contextList = searchService.realtime(condition);
            return Response.success(contextList);
        } catch (Exception e) {
            return Response.failure(e.getLocalizedMessage());
        }
    }

}
