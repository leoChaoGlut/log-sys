package cn.yunyichina.log.service.collector.client.fallback.factory;

import cn.yunyichina.log.common.entity.do_.CollectorDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.collector.client.CollectorServiceClient;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/23 13:41
 * @Description:
 */
@Component
public class CollectorServiceFallbackFactory implements FallbackFactory<CollectorServiceClient> {

    final Logger logger = LoggerFactory.getLogger(CollectorServiceFallbackFactory.class);

    @Override
    public CollectorServiceClient create(final Throwable cause) {
        final String errorMsg = cause == null ? "" : cause.getMessage();
        logger.error(errorMsg);
        return new CollectorServiceClient() {
            @Override
            public ResponseBodyDTO<CollectorDO> registerAndGetData(@RequestParam("ip") String ip, @RequestParam("port") String port, @RequestParam("applicationName") String applicationName) {
                logger.error(ip + " - " + port + " - " + applicationName);
                return ResponseBodyDTO.error(errorMsg);
            }

            @Override
            public ResponseBodyDTO<List<CollectorDO>> listAllCollector() {
                return ResponseBodyDTO.error(errorMsg);
            }

        };
    }
}
