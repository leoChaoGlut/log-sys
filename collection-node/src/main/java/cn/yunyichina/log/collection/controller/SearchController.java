package cn.yunyichina.log.collection.controller;

import cn.yunyichina.log.collection.entity.dto.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 9:36
 * @Description:
 */
@RestController("/search")
public class SearchController {


    @PostMapping
    public Response search() {
        return null;
    }


}
