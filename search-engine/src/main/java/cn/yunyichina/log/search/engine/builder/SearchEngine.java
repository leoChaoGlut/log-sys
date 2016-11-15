package cn.yunyichina.log.search.engine.builder;

import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;

import java.util.List;

/**
 * @Author: Jonven
 * @ModifiyedBy: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 18:13
 * @Description: 关键词搜索引擎
 */
public interface SearchEngine {

    List<ContextIndexBuilder.ContextInfo> search() throws Exception;

}
