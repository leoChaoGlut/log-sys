package cn.yunyichina.log.service.collector.controller;

import cn.yunyichina.log.common.entity.entity.dto.LogResult;
import cn.yunyichina.log.common.entity.entity.dto.ResponseDTO;
import cn.yunyichina.log.common.entity.entity.dto.SearchCondition;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.collector.constants.ServiceConfig;
import cn.yunyichina.log.service.collector.service.SearchService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    public ResponseDTO realtime(@RequestBody String json) {
        try {
            logger.contextBegin("实时日志搜索开始:" + json);
            SearchCondition condition = JSON.parseObject(json, SearchCondition.class);
            logger.info("obj:" + JSON.toJSONString(condition, true));
            List<LogResult> logResultList = searchService.realtime(condition);
            logger.contextEnd("实时日志搜索结束:" + JSON.toJSONString(logResultList, true));
            return ResponseDTO.success(logResultList);
        } catch (Exception e) {
            logger.contextEnd("实时日志搜索失败:" + e.getLocalizedMessage());
            return ResponseDTO.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("dir")
    public void getDirs() {
        Map<String, ServiceConfig.ConfigCollector> serviceConfigMap = searchService.getDirs();
        System.out.println("======" + JSON.toJSONString(serviceConfigMap, true));
    }

}
