import cn.yunyichina.log.common.LoggerWrapper;
import cn.yunyichina.log.common.TracerClient;
import cn.yunyichina.log.common.entity.do_.KvTagDO;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 11:06
 * @Description:
 */
public class CommonTest {

    @Test
    public void commonTest() {
        KvTagDO kvTagDO = new KvTagDO()
                .setKey("")
                .setId(1)
                .setKeyOffset(1);

    }

    @Test
    public void test() {
        long begin = System.nanoTime();
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        System.out.println(BigDecimal.valueOf(System.nanoTime() - begin, 9));
    }

    @Test
    public void tracerClientTest() {
        LoggerWrapper loggerWrapper = LoggerWrapper.getLogger(CommonTest.class);
        TracerClient tracerClient = new TracerClient(loggerWrapper);
        String url = "http://localhost:10402/tracer-localhost/";
        tracerClient.aroundRPC(url, "traceId", new Date().getTime() + "", "serviceId", true);
    }

    @Test
    public void test0() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        Worker worker0 = new Worker(latch, 0);
        Worker worker1 = new Worker(latch, 1);
        new Thread(worker0).start();
        new Thread(worker1).start();
        latch.await();
        System.err.println("all jobs are done");
    }


    public static class Worker implements Runnable {

        private CountDownLatch countDownLatch;
        private int id;

        public Worker(CountDownLatch countDownLatch, int id) {
            this.countDownLatch = countDownLatch;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.err.println("begin");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.err.println("end");
                countDownLatch.countDown();
            }
        }
    }
}
