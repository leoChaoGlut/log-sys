package cn.yunyichina.log.common.exception;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 16:37
 * @Description:
 */
public class CollectorException extends Exception {
    public CollectorException() {
        super("Exception occured on module 'log-service-collector'");
    }

    public CollectorException(String message) {
        super(message);
    }

    public CollectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectorException(Throwable cause) {
        super(cause);
    }

    public CollectorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
