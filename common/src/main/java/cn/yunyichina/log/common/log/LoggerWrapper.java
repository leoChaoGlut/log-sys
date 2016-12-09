package cn.yunyichina.log.common.log;

import cn.yunyichina.log.common.constant.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/9 16:37
 * @Description: *************注意*************
 * 在每一个请求的开始和结束,   *
 * 一定要调用contextBegin和    *
 * contextEnd,否则无法进行     *
 * 请求计数,导致索引构建       *
 * 异常. 最好把contextEnd      *
 * 放在finally块里.            *
 * *************注意*************
 */
public class LoggerWrapper {
    private static final AtomicLong counter = new AtomicLong();
    private static final AtomicBoolean counterHasInit = new AtomicBoolean(false);

    private static final ConcurrentHashMap<String, Long> countMap = new ConcurrentHashMap<>(128);
    private Logger logger;
    private final int STACK_INDEX = 2;

    private Class<?> targetClass;

    public static LoggerWrapper newInstance(Class<?> targetClass) {
        return new LoggerWrapper(targetClass);
    }

    private LoggerWrapper(Class<?> targetClass) {
        logger = LoggerFactory.getLogger(targetClass);
    }

    public void contextBegin(String msg) {
        Long count = counter.getAndIncrement();
        countMap.put(Thread.currentThread().getName(), count);
        logger.info(getInvokeClassAndMethod() + msg + Tag.CONTEXT_BEGIN + count + Tag.CONTEXT_COUNT_END + Tag.ROW_END + count + Tag.CONTEXT_COUNT_END);
    }

    public void contextEnd(String msg) {
        Long count = countMap.get(Thread.currentThread().getName());
        logger.info(getInvokeClassAndMethod() + msg + Tag.CONTEXT_END + count + Tag.CONTEXT_COUNT_END + Tag.ROW_END + count + Tag.CONTEXT_COUNT_END);
    }

    public void info(String msg) {
        logger.info(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(Thread.currentThread().getName()) + Tag.CONTEXT_COUNT_END);
    }

    public void error(String msg) {
        logger.error(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(Thread.currentThread().getName()) + Tag.CONTEXT_COUNT_END);
    }

    public void error(String msg, Throwable t) {
        logger.error(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(Thread.currentThread().getName()) + Tag.CONTEXT_COUNT_END, t);
    }

    private String getInvokeClassAndMethod() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[STACK_INDEX];
        return stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
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

    public static void initCounter(long count) {
        if (counterHasInit.get()) {

        } else {
            counter.set(count);
            counterHasInit.set(true);
        }
    }

    public static AtomicLong getCounter() {
        return counter;
    }
}
