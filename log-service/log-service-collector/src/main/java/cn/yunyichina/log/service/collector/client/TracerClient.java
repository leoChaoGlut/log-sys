package cn.yunyichina.log.service.collector.client;

import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.collector.client.fallback.factory.TracerFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/21 14:36
 * @Description:
 */
@FeignClient(
        name = "${tracer-id:tracer}",
        fallbackFactory = TracerFallbackFactory.class
)
@RequestMapping("trace/linked")
public interface TracerClient {

    @RequestMapping(method = RequestMethod.POST, path = "read/by/contextId/collectorId")
    ResponseBodyDTO<TreeSet<LinkedTraceNode>> getTraceByContextId(
            @RequestParam("contextId") String contextId,
            @RequestParam("collectorId") Integer collectorId
    );
}
