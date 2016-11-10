package cn.yy.log.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/9 16:37
 * @Description:
 */
public class LogUtil {
    private static final AtomicLong counter = new AtomicLong();
    private static final ConcurrentHashMap<String, Long> countMap = new ConcurrentHashMap<>(128);
    private Logger logger;

    private final String REQUEST_BEGIN_TAG = "$ReqBegin$";
    private final String REQUEST_END_TAG = "$ReqEnd$";
    private final String ROW_END_TAG = "@RowEnd@";
    private final String COUNT_END_TAG = "&";

    private Class<?> targetClass;

    public static LogUtil newInstance(Class<?> targetClass) {
        return new LogUtil(targetClass);
    }

    private LogUtil(Class<?> targetClass) {
        logger = LoggerFactory.getLogger(targetClass);
    }

    public void requestBegin(String msg) {
        Long count = counter.getAndIncrement();
        countMap.put(Thread.currentThread().getName(), count);
        logger.info(msg + REQUEST_BEGIN_TAG + count + COUNT_END_TAG);
    }

    public void requestEnd(String msg) {
        logger.info(msg + REQUEST_END_TAG + countMap.get(Thread.currentThread().getName()) + COUNT_END_TAG);
    }

    public void info(String msg) {
        logger.info(msg + ROW_END_TAG + countMap.get(Thread.currentThread().getName()) + COUNT_END_TAG);
    }

    public void error(String msg) {
        logger.error(msg + ROW_END_TAG + countMap.get(Thread.currentThread().getName()) + COUNT_END_TAG);
    }

    /**
     * 根据线程名,获取当前线程的count值.
     * 当线程执行结束后,下一次接任务的时候,count值会更新
     *
     * @param threadName
     * @return
     */
    public static Long getCount(String threadName) {
        return countMap.get(threadName);
    }

    public static AtomicLong getCounter() {
        return counter;
    }
}
