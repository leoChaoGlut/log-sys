package cn.yunyichina.log.service.collector.controller;

import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.component.entity.dto.Response;
import cn.yunyichina.log.component.entity.dto.SearchCondition;
import cn.yunyichina.log.service.collector.service.SearchService;
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

    final LoggerWrapper logger = LoggerWrapper.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;

    @PostMapping("realtime")
    public Response realtime(@RequestBody String jsonParam) {
        try {
            logger.contextBegin("实时日志搜索开始:" + jsonParam);
            SearchCondition condition = JSON.parseObject(jsonParam, SearchCondition.class);
            List<String> contextList = searchService.realtime(condition);
            logger.contextEnd("实时日志搜索结束:" + JSON.toJSONString(contextList, true));
            return Response.success(contextList);
        } catch (Exception e) {
            logger.contextEnd("实时日志搜索失败:" + e.getLocalizedMessage());
            return Response.failure(e.getLocalizedMessage());
        }
    }

}
