package cn.yunyichina.log.engine;

/**
 * @Author: Jonven
 * @ModifiyedBy: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 18:13
 * @Description: 搜索引擎接口
 */
public interface SearchEngine<T> {

    T search() throws Exception;

}
