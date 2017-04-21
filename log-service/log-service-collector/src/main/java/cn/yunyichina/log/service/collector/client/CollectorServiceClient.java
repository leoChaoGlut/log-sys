package cn.yunyichina.log.service.collector.client;

import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.collector.client.fallback.factory.CollectorServiceFallbackFactory;
import cn.yunyichina.log.service.common.entity.dto.RedisProxyIndexDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/21 16:03
 * @Description:
 */
@FeignClient(
        name = "${collector-service-id:collector-service}",
        fallbackFactory = CollectorServiceFallbackFactory.class
)
public interface CollectorServiceClient {

    @RequestMapping(method = RequestMethod.POST, path = "collector/register")
    ResponseBodyDTO<CollectorDO> registerAndGetData(
            @RequestParam("ip") String ip,
            @RequestParam("port") String port,
            @RequestParam("applicationName") String applicationName
    );

    @RequestMapping(method = RequestMethod.GET, path = "collector/all")
    ResponseBodyDTO<List<CollectorDO>> listAllCollector();

    @RequestMapping(method = RequestMethod.GET, path = "index/cache")
    ResponseBodyDTO cacheIndex(RedisProxyIndexDTO redisProxyIndexDTO);


}
