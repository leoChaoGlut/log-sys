package cn.yunyichina.log.service.searcherNode.controller;

import cn.yunyichina.log.common.entity.dto.Response;
import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.service.searcherNode.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @PostMapping("realTime")
    public Response realTime(SearchCondition condition) {
        try {
            List<String> conextList = searchService.realtime(condition);
            return Response.success(conextList);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
    }

    @PostMapping("history")
    public Response history(SearchCondition condition) {
        try {
            List<String> contextList = searchService.history(condition);
            return Response.success(contextList);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getLocalizedMessage());
        }
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

}
