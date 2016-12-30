package cn.yunyichina.log.service.searcher.controller;

import cn.yunyichina.log.common.entity.dto.Response;
import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.searcher.service.SearchService;
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

    @PostMapping("history")
    public Response history(@RequestBody String json) {
        try {
            logger.info("搜索历史日志开始:" + json);
            SearchCondition condition = JSON.parseObject(json, SearchCondition.class);
            logger.info("obj:" + JSON.toJSONString(condition, true));
            List<String> contextList = searchService.history(condition);
            logger.info("搜索完成:" + JSON.toJSONString(contextList, true));
            return Response.success(contextList);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
    }

}
