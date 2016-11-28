package cn.yunyichina.log.common.base;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/28 10:08
 * @Description:
 */
public interface BaseMapper<T> {

    T selectOne(T t);


}
