package cn.yunyichina.log.service.collector.client.fallback.factory;

import cn.yunyichina.log.common.entity.do_.LinkedTraceNode;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.collector.client.TracerReadClient;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/23 13:41
 * @Description:
 */
@Component
public class TracerReadFallbackFactory implements FallbackFactory<TracerReadClient> {

    final Logger logger = LoggerFactory.getLogger(TracerReadFallbackFactory.class);

    @Override
    public TracerReadClient create(final Throwable cause) {
        final String errorMsg = cause == null ? "" : cause.getMessage();
        logger.error(errorMsg);
        return new TracerReadClient() {
            @Override
            public ResponseBodyDTO<TreeSet<LinkedTraceNode>> getTraceByContextId(@RequestParam("contextId") String contextId, @RequestParam("collectorId") Integer collectorId) {
                return ResponseBodyDTO.error(errorMsg);
            }
        };
    }
}
