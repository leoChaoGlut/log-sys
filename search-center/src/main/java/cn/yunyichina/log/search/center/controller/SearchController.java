package cn.yunyichina.log.search.center.controller;

import cn.yunyichina.log.common.entity.dto.Response;
import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.search.center.constant.SearchEngineType;
import cn.yunyichina.log.search.center.service.imp.KeyValueSsearchService;
import cn.yunyichina.log.search.center.service.imp.KeywordSearchService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/search")
public class SearchController {

    @Autowired
    KeywordSearchService keywordSearchService;
    @Autowired
    KeyValueSsearchService keyValueSsearchService;

    @PostMapping("/history")
    public Response search(SearchCondition searchCondition) {
        try {
            System.out.println(JSON.toJSONString(searchCondition));
            List<String> contextList;
            switch (searchCondition.getSearchEngineType()) {
                case SearchEngineType.KEYWORD:
                    contextList = keywordSearchService.search(searchCondition);
                    break;
                case SearchEngineType.KEY_VALUE:
                    contextList = keyValueSsearchService.search(searchCondition);
                    break;
                default:
                    throw new Exception("不支持的搜索引擎类型");
            }
            return Response.success(contextList);
        } catch (Exception e) {
            return Response.failure(e.getLocalizedMessage());
        }
    }


}
