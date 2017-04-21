package cn.yunyichina.log.service.collectorservice.client;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.RedisRecordDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.collectorservice.client.fallback.factory.RedisProxyFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/13 16:01
 * @Description:
 */
@FeignClient(
        name = "${redis-proxy-id:redis-proxy}",
        fallbackFactory = RedisProxyFallbackFactory.class
)
public interface RedisProxyClient {

    @RequestMapping(method = RequestMethod.POST, path = "redis/record")
    ResponseBodyDTO<RedisRecordDO> redisRecord(
            CollectedItemDO collectedItem
    );

}
