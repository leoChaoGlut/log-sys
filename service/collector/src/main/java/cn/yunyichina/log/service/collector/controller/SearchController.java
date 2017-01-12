package cn.yunyichina.log.service.collector.controller;

import cn.yunyichina.log.common.entity.entity.dto.LogResult;
import cn.yunyichina.log.common.entity.entity.dto.Response;
import cn.yunyichina.log.common.entity.entity.dto.SearchCondition;
import cn.yunyichina.log.common.log.LoggerWrapper;
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
    public Response realtime(@RequestBody String json) {
        try {
            logger.contextBegin("实时日志搜索开始:" + json);
            SearchCondition condition = JSON.parseObject(json, SearchCondition.class);
            logger.info("obj:" + JSON.toJSONString(condition, true));
            List<LogResult> logResultList = searchService.realtime(condition);
            logger.contextEnd("实时日志搜索结束:" + JSON.toJSONString(logResultList, true));
            return Response.success(logResultList);
        } catch (Exception e) {
            logger.contextEnd("实时日志搜索失败:" + e.getLocalizedMessage());
            return Response.failure(e.getLocalizedMessage());
        }
    }

}
