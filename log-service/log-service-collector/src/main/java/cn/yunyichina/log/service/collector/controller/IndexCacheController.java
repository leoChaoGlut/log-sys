package cn.yunyichina.log.service.collector.controller;

import cn.yunyichina.log.common.base.AbstractController;
import cn.yunyichina.log.service.collector.util.CacheUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/28 18:33
 * @Description:
 */
@RestController
@RequestMapping("indexCache")
public class IndexCacheController extends AbstractController {

    final Logger logger = LoggerFactory.getLogger(RefreshController.class);

    @PostMapping("details")
    String showIndexCacheDetailsBy(
            Integer collectedItemId,
            String cacheFileName
    ) throws IOException, ClassNotFoundException {
        logger.info(collectedItemId + " - " + cacheFileName);
        Object cacheObject = CacheUtil.read(collectedItemId, cacheFileName);
        String json = JSON.toJSONString(cacheObject, true);
        logger.info(json);
        return json;
    }

}
