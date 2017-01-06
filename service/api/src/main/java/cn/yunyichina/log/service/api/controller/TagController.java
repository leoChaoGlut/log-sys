package cn.yunyichina.log.service.api.controller;

import cn.yunyichina.log.component.entity.dto.Response;
import cn.yunyichina.log.component.entity.dto.TagSet;
import cn.yunyichina.log.service.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/6 17:39
 * @Description:
 */
@RestController
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping("tag/{collectorId}")
    public Response getTagSet(
            @PathVariable Integer collectorId
    ) {
        try {
            TagSet tagSet = tagService.getTagSet(collectorId);
            return Response.success(tagSet);
        } catch (Exception e) {
            return Response.failure(e.getLocalizedMessage());
        }
    }

}
