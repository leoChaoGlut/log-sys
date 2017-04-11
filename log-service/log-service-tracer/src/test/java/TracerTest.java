import cn.yunyichina.log.common.LoggerWrapper;
import cn.yunyichina.log.common.TracerClient;
import org.junit.Test;

import java.text.SimpleDateFormat;
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
        String url = "http://log.yunyichina.cn:10500/trace/linked/append/linkednode";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        for (int i = 0; i < 5; i++) {
            String traceId = "t00" + i;
            for (int j = 0; j < 10; j++) {
                String serviceId = i + " - " + j + UUID.randomUUID().toString();
                tracerClient.aroundRPC(url, traceId, dateFormat.format(new Date()), serviceId, true);

            }
        }
    }

    @Test
    public void test1() {

    }

}
