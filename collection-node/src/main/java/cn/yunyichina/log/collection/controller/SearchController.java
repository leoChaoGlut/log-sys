package cn.yunyichina.log.collection.controller;

import cn.yunyichina.log.collection.service.SearchService;
import cn.yunyichina.log.common.entity.dto.Response;
import cn.yunyichina.log.common.entity.dto.SearchCondition;
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
    SearchService searchService;

    @PostMapping("realTime")
    public Response realTime(String json) {
        try {
            System.err.println("==========");
            SearchCondition searchCondition = JSON.parseObject(json, SearchCondition.class);
            System.err.println("==========");
            System.out.println(JSON.toJSONString(searchCondition));
            System.err.println("==========");
            List<String> contextList = searchService.search(searchCondition);
            return Response.success(contextList);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
//        return null;
    }


}
