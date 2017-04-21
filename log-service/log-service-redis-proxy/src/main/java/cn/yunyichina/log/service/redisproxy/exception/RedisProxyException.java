package cn.yunyichina.log.service.redisproxy.exception;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/14 10:07
 * @Description:
 */
public class RedisProxyException extends RuntimeException {
    public RedisProxyException(String message) {
        super(message);
    }
}
