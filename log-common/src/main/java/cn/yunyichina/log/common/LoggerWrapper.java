package cn.yunyichina.log.common;

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

    private static final ConcurrentHashMap<String, Long> countMap = new ConcurrentHashMap<String, Long>(128);
    private Logger logger;
    private final int STACK_INDEX = 3;

    private Class<?> targetClass;

    public static LoggerWrapper getLogger(Class<?> targetClass) {
        return new LoggerWrapper(targetClass);
    }

    private LoggerWrapper(Class<?> targetClass) {
        logger = LoggerFactory.getLogger(targetClass);
    }

    public Long contextBegin(String msg) {
        Long count = counter.getAndIncrement();
        countMap.put(buildThreadId(), count);
        logger.info(getInvokeClassAndMethod() + msg + Tag.CONTEXT_BEGIN + count + Tag.CONTEXT_COUNT_END + Tag.ROW_END + count + Tag.CONTEXT_COUNT_END);
        return count;
    }

    public Long contextEnd(String msg) {
        Long count = countMap.get(buildThreadId());
        logger.info(getInvokeClassAndMethod() + msg + Tag.CONTEXT_END + count + Tag.CONTEXT_COUNT_END + Tag.ROW_END + count + Tag.CONTEXT_COUNT_END);
        return count;
    }

    /**
     * 重写这些方法是为了适配以前的代码,减少改动量
     *
     * @param msg
     */
    public void warn(String msg) {
        logger.warn(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(buildThreadId()) + Tag.CONTEXT_COUNT_END);
    }

    public void warn(String msg, Object... args) {
        logger.warn(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(buildThreadId()) + Tag.CONTEXT_COUNT_END, args);
    }

    public void debug(String msg) {
        logger.debug(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(buildThreadId()) + Tag.CONTEXT_COUNT_END);
    }

    public void debug(String msg, Object... args) {
        logger.debug(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(buildThreadId()) + Tag.CONTEXT_COUNT_END, args);
    }

    public void info(String msg) {
        logger.info(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(buildThreadId()) + Tag.CONTEXT_COUNT_END);
    }

    public void info(String msg, Object... args) {
        logger.info(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(buildThreadId()) + Tag.CONTEXT_COUNT_END, args);
    }

    public void error(String msg) {
        logger.error(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(buildThreadId()) + Tag.CONTEXT_COUNT_END);
    }

    public void error(String msg, Throwable t) {
        logger.error(getInvokeClassAndMethod() + msg + Tag.ROW_END + countMap.get(buildThreadId()) + Tag.CONTEXT_COUNT_END, t);
    }

    /**
     * 获取调用 contextBegin ,contextEnd, error 等方法的调用者
     * 方法栈帧顺序:
     * 0: getInvokeClassAndMethod
     * 1: contextBegin或contextEnd或error
     * 2. 调用contextBegin或contextEnd或error的方法
     *
     * @return
     */
    private String getInvokeClassAndMethod() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[STACK_INDEX];
        return stackTraceElement.getClassName() + "." +
                stackTraceElement.getMethodName() + ":" +
                stackTraceElement.getLineNumber();
    }


    private String buildThreadId() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        String threadGroupName = "";
        if (threadGroup != null) {
            threadGroupName = threadGroup.getName();
        }
        return threadGroupName + "-" + Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
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

    public Long getContextCount() {
        return countMap.get(buildThreadId());
    }

    /**
     * 根据给定的count,初始化counter
     * 用途:采集结点崩溃后,恢复索引值
     *
     * @param count
     */
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
