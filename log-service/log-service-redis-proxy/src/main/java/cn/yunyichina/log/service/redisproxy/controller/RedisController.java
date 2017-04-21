package cn.yunyichina.log.service.redisproxy.controller;

import cn.yunyichina.log.common.base.AbstractController;
import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.RedisRecordDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.redisproxy.service.RedisService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/13 15:46
 * @Description:
 */
@RestController
@RequestMapping("redis")
public class RedisController extends AbstractController {
    private final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    RedisService redisService;

    @PostMapping("record")
    ResponseBodyDTO<RedisRecordDO> redisRecord(
            @RequestBody CollectedItemDO collectedItem
    ) throws Exception {
        logger.info(JSON.toJSONString(collectedItem, true));
        RedisRecordDO redisRecord = redisService.getRedisRecordBy(collectedItem);
        logger.info(JSON.toJSONString(redisRecord, true));
        return ResponseBodyDTO.ok(redisRecord);
    }


}
