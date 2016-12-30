import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/29 16:52
 * @Description:
 */
public class MyTest {

    public static class MyCallable implements Callable<String> {

        private String name;

        public MyCallable(String name) {
            this.name = name;
        }

        @Override
        public String call() throws Exception {
            return name;
        }
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        List<MyCallable> callableList = new ArrayList<>();
        List<Future> futureList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            callableList.add(new MyCallable(i + ""));
        }

        for (int i = 0; i < 10; i++) {
            Future<String> future = threadPool.submit(callableList.get(i));
            futureList.add(future);
        }

        for (int i = 0; i < 10; i++) {
            Future future = futureList.get(i);
            System.out.println(future.get());
        }

    }
}
