import cn.yunyichina.log.service.tracer.trace.linked.LinkedTraceNode;
import org.junit.Test;

import java.util.PriorityQueue;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/28 16:46
 * @Description:
 */
public class TracerTest {

    @Test
    public void test1() {
        PriorityQueue<LinkedTraceNode> pq = new PriorityQueue<>();
        pq.add(new LinkedTraceNode().setTimestamp(1L));
        pq.add(new LinkedTraceNode().setTimestamp(3L));
        pq.add(new LinkedTraceNode().setTimestamp(2L));
        System.out.println(pq.poll().toString());
        System.out.println(pq.poll().toString());
        System.out.println(pq.poll().toString());
    }
}
