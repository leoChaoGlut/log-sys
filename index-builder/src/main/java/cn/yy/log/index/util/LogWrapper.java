package cn.yy.log.index.util;

import cn.yy.log.index.constant.Tag;
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
public class LogWrapper {
    private static final AtomicLong counter = new AtomicLong();
    private static final ConcurrentHashMap<String, Long> countMap = new ConcurrentHashMap<>(128);
    private Logger logger;


    private Class<?> targetClass;

    public static LogWrapper newInstance(Class<?> targetClass) {
        return new LogWrapper(targetClass);
    }

    private LogWrapper(Class<?> targetClass) {
        logger = LoggerFactory.getLogger(targetClass);
    }

    public void contextBegin(String msg) {
        Long count = counter.getAndIncrement();
        countMap.put(Thread.currentThread().getName(), count);
        logger.info(msg + Tag.CONTEXT_BEGIN + count + Tag.CONTEXT_COUNT_END + Tag.ROW_END + count + Tag.CONTEXT_COUNT_END);
    }

    public void contextEnd(String msg) {
        Long count = countMap.get(Thread.currentThread().getName());
        logger.info(msg + Tag.CONTEXT_END + count + Tag.CONTEXT_COUNT_END + Tag.ROW_END + count + Tag.CONTEXT_COUNT_END);
    }

    public void info(String msg) {
        logger.info(msg + Tag.ROW_END + countMap.get(Thread.currentThread().getName()) + Tag.CONTEXT_COUNT_END);
    }

    public void error(String msg) {
        logger.error(msg + Tag.ROW_END + countMap.get(Thread.currentThread().getName()) + Tag.CONTEXT_COUNT_END);
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
