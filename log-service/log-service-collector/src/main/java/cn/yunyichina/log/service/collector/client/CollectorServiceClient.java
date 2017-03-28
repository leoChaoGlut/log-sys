package cn.yunyichina.log.service.collector.client;

import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.collector.client.fallback.factory.CollectorServiceFallbackFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
@FeignClient(
        name = "${collector-service-name:collector-service}",
        fallbackFactory = CollectorServiceFallbackFactory.class
)
@RequestMapping("collector")
public interface CollectorServiceClient {

    @RequestMapping(method = RequestMethod.POST, path = "register")
    ResponseBodyDTO<CollectorDO> registerAndGetData(
            @RequestParam("ip") String ip,
            @RequestParam("port") String port,
            @RequestParam("applicationName") String applicationName
    );

    @RequestMapping(method = RequestMethod.GET, path = "all")
    ResponseBodyDTO<List<CollectorDO>> listAllCollector();

}
