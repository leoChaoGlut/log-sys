package cn.yunyichina.log.service.logCenter.controller;

import cn.yunyichina.log.common.entity.dto.Response;
import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.service.logCenter.service.SearchService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @PostMapping("/history")
    public Response search(String json) {
        try {
            SearchCondition searchCondition = JSON.parseObject(json, SearchCondition.class);
            List<String> contextList = searchService.search(searchCondition);
            return Response.success(contextList);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("/realtime")
    public void realtime(String json, HttpServletResponse resp) throws IOException {
        SearchCondition searchCondition = JSON.parseObject(json, SearchCondition.class);
        String hospitalLetter = searchCondition.getHospitalLetter();
//        TODO 根据医院拼音首字母,转发到对应的前置机,让前置机来做实时日志查询
//        TODO 根据医院拼音首字母,转发到对应的前置机,让前置机来做实时日志查询
//        TODO 根据医院拼音首字母,转发到对应的前置机,让前置机来做实时日志查询
        resp.sendRedirect("");
    }

}
