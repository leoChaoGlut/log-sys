package cn.yunyichina.log.service.tracer.exception;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/2 17:01
 * @Description:
 */
public class TracerException extends RuntimeException {
    public TracerException(String message) {
        super(message);
    }
}
