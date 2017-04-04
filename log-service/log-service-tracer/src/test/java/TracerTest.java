import cn.yunyichina.log.common.LoggerWrapper;
import cn.yunyichina.log.common.TracerClient;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 16:46
 * @Description:
 */
public class TracerTest {

    @Test
    public void test0() {
        LoggerWrapper logger = LoggerWrapper.getLogger(TracerTest.class).setSuffix("sdfs");
        TracerClient tracerClient = new TracerClient(logger);
        String url = "http://localhost:10402/trace/get/by/traceId";
        for (int i = 0; i < 5; i++) {
            String traceId = "t00" + i;
            for (int j = 0; j < 10; j++) {
                long timestamp = new Date().getTime();
                String serviceId = i + " - " + j + UUID.randomUUID().toString();
                tracerClient.aroundRPC(url, traceId, timestamp + "", serviceId, true);

            }
        }
    }

}
