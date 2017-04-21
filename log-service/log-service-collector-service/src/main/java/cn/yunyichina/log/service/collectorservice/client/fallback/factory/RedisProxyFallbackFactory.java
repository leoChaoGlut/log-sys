package cn.yunyichina.log.service.collectorservice.client.fallback.factory;


import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.service.collectorservice.client.RedisProxyClient;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/23 13:41
 * @Description:
 */
@Component
public class RedisProxyFallbackFactory implements FallbackFactory<RedisProxyClient> {

    private final Logger logger = LoggerFactory.getLogger(RedisProxyFallbackFactory.class);

    @Override
    public RedisProxyClient create(final Throwable cause) {
        final String errorMsg = cause == null ? "" : cause.getMessage();
        logger.error(errorMsg);
        return new RedisProxyClient() {
            @Override
            public ResponseBodyDTO redisRecord(CollectedItemDO collectedItem) {
                return ResponseBodyDTO.error(errorMsg);
            }
        };
    }
}
