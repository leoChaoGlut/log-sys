import cn.yunyichina.log.common.LoggerWrapper;
import cn.yunyichina.log.common.TracerClient;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 11:06
 * @Description:
 */
public class CommonTest {

    @Test
    public void tracerClientTest() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        TracerClient tracerClient = new TracerClient(LoggerWrapper.getLogger(CommonTest.class));
        String url = "http://127.0.0.1:10402/trace/linked/append/linkednode/batch";
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String traceId = "t-" + i;
            String dateTimeStr = dateFormat.format(new Date().getTime());
            String applicationName = "dubbo://127.0.0.1:20884/cn.yunyichina.provider.iface.service.IHisiface?";
            tracerClient.aroundRPC(url, traceId, dateTimeStr, applicationName, true);
            if (i % 1000 == 0) {
                LockSupport.parkNanos(2_000_000_000L);
            }
        }
    }

    @Test
    public void test0() {
        Date date = new Date(1492425343808L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        String format = dateFormat.format(date);
        System.out.println(format);
    }

}
