package cn.yunyichina.log.common.exception;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 14:52
 * @Description:
 */
public class CollectorServiceException extends RuntimeException {
    public CollectorServiceException() {
        super("Exception occured on module 'log-service-collector-service'");
    }

    public CollectorServiceException(String message) {
        super(message);
    }

    public CollectorServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectorServiceException(Throwable cause) {
        super(cause);
    }

    public CollectorServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
