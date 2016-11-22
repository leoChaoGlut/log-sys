package cn.yunyichina.log.component.searchEngine.imp;

import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.component.searchEngine.AbstractSearchEngine;
import cn.yunyichina.log.component.searchEngine.SearchEngine;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 15:43
 * @Description:
 */
public class NoIndexSearchEngine extends AbstractSearchEngine implements SearchEngine<SearchCondition> {
    @Override
    public SearchCondition search() throws Exception {
        return null;
    }
}
