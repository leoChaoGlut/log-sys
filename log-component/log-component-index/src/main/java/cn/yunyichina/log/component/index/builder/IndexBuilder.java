package cn.yunyichina.log.component.index.builder;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/11 9:58
 * @Description:
 */
public interface IndexBuilder<T> {

    String LOG_SUFFIX = ".log";

    T build();
}
