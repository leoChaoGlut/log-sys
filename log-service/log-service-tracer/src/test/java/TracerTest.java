import cn.yunyichina.log.common.LoggerWrapper;
import cn.yunyichina.log.common.TracerClient;
import cn.yunyichina.log.service.tracer.trace.linked.LinkedTraceNode;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 16:46
 * @Description:
 */
public class TracerTest {

    @Test
    public void test1() {
        PriorityBlockingQueue<LinkedTraceNode> pq = new PriorityBlockingQueue<>();
        pq.add(new LinkedTraceNode().setTimestamp(1L));
        pq.add(new LinkedTraceNode().setTimestamp(3L));
        pq.add(new LinkedTraceNode().setTimestamp(2L));
        System.out.println(pq.poll().toString());
        System.out.println(pq.poll().toString());
        System.out.println(pq.poll().toString());
    }

    @Test
    public void test0() {
        LoggerWrapper logger = LoggerWrapper.getLogger(TracerTest.class);
        TracerClient tracerClient = new TracerClient(logger);
        String url = "http://localhost:10402";
        String traceId = "t002";
        long timestamp = new Date().getTime();
        String serviceId = UUID.randomUUID().toString();

        tracerClient.aroundRPC(url, traceId, timestamp + "", serviceId, true);
    }

}
