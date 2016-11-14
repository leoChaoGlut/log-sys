package cn.yy.log.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/9/30 10:12
 * @Description:
 */
public class ThreadPool {
    /**
     * 据我了解,我们的业务类型,偏I/O.
     * 套用I/O密集型任务的线程数与CPU核心数的公式:线程数 = CPU核心数/(1-阻塞系数)
     * 可以得出大致的最优线程数.
     * 由于阻塞系数需要通过大量数据统计得出,所以这里只给出一个估计值,0.9.
     */
    private final float BLOCK_FACTOR = 0.9f;
    private final int THREAD_COUNT = (int) (Runtime.getRuntime().availableProcessors() / (1 - BLOCK_FACTOR));

    private ExecutorService threadPool;

    public void init() {
        threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }


}
