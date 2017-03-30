package cn.yunyichina.log.service.tracer.trace;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/29 15:01
 * @Description:
 */
public interface ITrace<T> {
    void addNode(T t);
}
