package cn.yunyichina.log.service.collector.client;

import cn.yunyichina.log.common.entity.entity.dto.ResponseDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/22 11:55
 * @Description:
 */
@FeignClient(name = "log-service-api")
public interface ApiClient {

    @RequestMapping(method = RequestMethod.GET, path = "list/collected/item/by/{applicationName}")
    ResponseDTO listCollectedItem(
            @PathVariable String applicationName
    );
}
