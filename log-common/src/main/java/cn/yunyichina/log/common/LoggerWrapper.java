package cn.yunyichina.log.common;

import cn.yunyichina.log.common.constant.Tag;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/9 16:37
 * @Description: 注意, 在每一个请求的开始和结束, 一定要调用contextBegin和contextEnd, 否则无法正常索引上下文, 导致该段上下文失效.
 */
public class LoggerWrapper {
    /**
     * key: current thread id
     * value: context id (UUID)
     */
    private static final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    /**
     * 实际的调用方法的栈深
     */
    @Getter
    private final int STACK_DEPTH = 3;
    public static final String SUFFIX_SEPARATOR = "_";
    @Getter
    private Logger logger;
    @Getter
    @Setter
    private String suffix;//可以用作子系统的区分标识
    @Getter
    private Class<?> targetClass;

    public static LoggerWrapper getLogger(Class<?> targetClass) {
        return new LoggerWrapper(targetClass);
    }

    public static LoggerWrapper getLogger(Class<?> targetClass, String prefix) {
        return new LoggerWrapper(targetClass, prefix);
    }

    public static String getSuffixBy(String contextId) {
        String[] split = contextId.split(SUFFIX_SEPARATOR);
        if (2 == split.length) {
            String suffix = split[1];
            if (suffix == null || suffix.trim().isEmpty()) {
                return "null";
            } else {
                return suffix;
            }
        } else {
            return "null";
        }
    }

    private LoggerWrapper(Class<?> targetClass) {
        this(targetClass, null);
    }

    private LoggerWrapper(Class<?> targetClass, String suffix) {
        logger = LoggerFactory.getLogger(targetClass);
        this.suffix = suffix;
    }

    public String contextBegin(String msg) {
        String contextId;
        if (null == suffix) {
            contextId = UUID.randomUUID().toString();
        } else {
            contextId = UUID.randomUUID().toString() + SUFFIX_SEPARATOR + suffix;
        }
        map.put(buildThreadId(), contextId);
        logger.info(getInvokeClassAndMethod() + msg + Tag.CONTEXT_BEGIN + contextId + Tag.CONTEXT_ID_END + Tag.ROW_END + contextId + Tag.CONTEXT_ID_END);
        return contextId;
    }

    public String contextEnd(String msg) {
        String contextId = map.get(buildThreadId());
        logger.info(getInvokeClassAndMethod() + msg + Tag.CONTEXT_END + contextId + Tag.CONTEXT_ID_END + Tag.ROW_END + contextId + Tag.CONTEXT_ID_END);
        return contextId;
    }

    /**
     * 重写这些方法是为了适配以前的代码,减少改动量
     *
     * @param msg
     */
    public void warn(String msg) {
        logger.warn(getInvokeClassAndMethod() + msg + Tag.ROW_END + map.get(buildThreadId()) + Tag.CONTEXT_ID_END);
    }

    public void warn(String msg, Object... args) {
        logger.warn(getInvokeClassAndMethod() + msg + Tag.ROW_END + map.get(buildThreadId()) + Tag.CONTEXT_ID_END, args);
    }

    public void debug(String msg) {
        logger.debug(getInvokeClassAndMethod() + msg + Tag.ROW_END + map.get(buildThreadId()) + Tag.CONTEXT_ID_END);
    }

    public void debug(String msg, Object... args) {
        logger.debug(getInvokeClassAndMethod() + msg + Tag.ROW_END + map.get(buildThreadId()) + Tag.CONTEXT_ID_END, args);
    }

    public void info(String msg) {
        logger.info(getInvokeClassAndMethod() + msg + Tag.ROW_END + map.get(buildThreadId()) + Tag.CONTEXT_ID_END);
    }

    public void info(String msg, Object... args) {
        logger.info(getInvokeClassAndMethod() + msg + Tag.ROW_END + map.get(buildThreadId()) + Tag.CONTEXT_ID_END, args);
    }

    public void error(String msg) {
        logger.error(getInvokeClassAndMethod() + msg + Tag.ROW_END + map.get(buildThreadId()) + Tag.CONTEXT_ID_END);
    }

    public void error(String msg, Throwable t) {
        logger.error(getInvokeClassAndMethod() + msg + Tag.ROW_END + map.get(buildThreadId()) + Tag.CONTEXT_ID_END, t);
    }

    /**
     * 获取调用 contextBegin ,contextEnd, error 等方法的调用者
     * 方法栈帧顺序:
     * 0: getInvokeClassAndMethod
     * 1: contextBegin或contextEnd或error
     * 2. 调用contextBegin或contextEnd或error的方法
     * 有了这一段,就不需要在logback.xml中配置打印类名方法名了.
     *
     * @return
     */
    private String getInvokeClassAndMethod() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[STACK_DEPTH];
        return "[" + stackTraceElement.getClassName() + "." +
                stackTraceElement.getMethodName() + ":" +
                stackTraceElement.getLineNumber() + "]";
    }


    private String buildThreadId() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        String threadGroupName = "";
        if (threadGroup != null) {
            threadGroupName = threadGroup.getName();
        }
        return threadGroupName + "-" + Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
    }

    public String getCurrentThreadContextId() {
        return map.get(buildThreadId());
    }


}
