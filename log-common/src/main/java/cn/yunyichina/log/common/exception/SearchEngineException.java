package cn.yunyichina.log.common.exception;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/3 19:28
 * @Description:
 */
public class SearchEngineException extends Exception {
    public SearchEngineException() {
    }

    public SearchEngineException(String message) {
        super(message);
    }

    public SearchEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchEngineException(Throwable cause) {
        super(cause);
    }

    public SearchEngineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
