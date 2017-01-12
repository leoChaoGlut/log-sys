package cn.yunyichina.log.service.searcher.controller;

import cn.yunyichina.log.common.entity.entity.dto.LogResult;
import cn.yunyichina.log.common.entity.entity.dto.Response;
import cn.yunyichina.log.common.entity.entity.dto.SearchCondition;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.searcher.service.SearchService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

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
            logger.contextBegin("请求搜索历史日志:" + json);
            SearchCondition condition = JSON.parseObject(json, SearchCondition.class);
            logger.info("obj:" + JSON.toJSONString(condition, true));
            List<LogResult> logResultList = searchService.history(condition);
            logger.contextEnd("搜索完成:" + logResultList.size());
            return Response.success(logResultList);
        } catch (Exception e) {
            String errorMsg = "搜索日志出错:" + e.getLocalizedMessage();
            logger.error(errorMsg, e);
            logger.contextEnd("搜索以异常结束," + errorMsg);
            return Response.failure(errorMsg);
        }
    }

    @PostMapping("logs")
    public Response logs(@RequestBody String json) {
        try {
            logger.contextBegin("获取可下载的日志:" + json);
            SearchCondition condition = JSON.parseObject(json, SearchCondition.class);
            logger.info("obj:" + JSON.toJSONString(condition, true));
            Set<String> logFileSet = searchService.getDownloadableLogs(condition);
            logger.contextEnd("获取完成:" + logFileSet.size());
            return Response.success(logFileSet);
        } catch (Exception e) {
            String errorMsg = "获取日志出错:" + e.getLocalizedMessage();
            logger.error(errorMsg, e);
            logger.contextEnd("获取以异常结束," + errorMsg);
            return Response.failure(errorMsg);
        }
    }

}
