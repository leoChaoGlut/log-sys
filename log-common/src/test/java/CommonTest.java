import cn.yunyichina.log.common.LoggerWrapper;
import cn.yunyichina.log.common.TracerClient;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        String url = "http://127.0.0.1:10600/trace/linked/append/linkednode/batch";
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
//        1493110352874
        Date date = new Date(1492425343808L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        String format = dateFormat.format(date);
        System.out.println(format);
    }


    @Test
    public void test2() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        2017-04-17 18:35:43
        long timestamp = 1492425343808L;

        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        begin.setTimeInMillis(timestamp);
        end.setTimeInMillis(timestamp);

        begin.add(Calendar.MINUTE, -1);
        end.add(Calendar.MINUTE, 1);

        System.out.println(dateFormat.format(begin.getTime()));
        System.out.println(dateFormat.format(end.getTime()));
    }


    @Test
    public void test3() {
        new Sort(new int[]{1, 3, 5, 7, 9}, 4).sort();
    }

    public static class Sort {
        private int[] arr;
        private int target;

        public Sort(int[] arr, int target) {
            this.arr = arr;
            this.target = target;
        }

        public int sort() {
            int index = -1;
            if (target < arr[0]) {

            } else if (true) {

            }
            for (int i = 0; i < arr.length; i++) {

            }
            return index;
        }

    }
}
