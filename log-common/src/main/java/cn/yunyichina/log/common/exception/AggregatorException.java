package cn.yunyichina.log.common.exception;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/28 11:49
 * @Description:
 */
public class AggregatorException extends Exception {

    public AggregatorException() {
        super("Exception occured on module 'log-component-aggregator'");
    }

    public AggregatorException(String message) {
        super(message);
    }

    public AggregatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AggregatorException(Throwable cause) {
        super(cause);
    }

    public AggregatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
