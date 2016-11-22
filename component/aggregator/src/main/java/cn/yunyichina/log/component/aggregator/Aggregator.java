package cn.yunyichina.log.component.aggregator;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 14:44
 * @Description:
 */
public interface Aggregator<T> {

    T aggregate(T input);

}
