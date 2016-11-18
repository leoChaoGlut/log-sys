package cn.yunyichina.log.search.center.service.imp;

import cn.yunyichina.log.aggregator.log.LogAggregator;
import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.search.center.service.SearchService;
import cn.yunyichina.log.search.center.util.IndexManager;
import cn.yunyichina.log.search.engine.imp.KeyValueSearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 10:58
 * @Description:
 */
@Service
public class KeyValueSearchService implements SearchService {

    @Autowired
    IndexManager indexManager;

    @Override
    public List<String> search(SearchCondition searchCondition) throws Exception {
        KeyValueSearchEngine searchEngine = new KeyValueSearchEngine(indexManager.getKeyValueIndexMap(), indexManager.getContextIndexMap(), searchCondition);
        Set<ContextIndexBuilder.ContextInfo> contextInfoSet = searchEngine.search();
        if (contextInfoSet == null) {
            return null;
        } else {
            List<String> contextList = new ArrayList<>(contextInfoSet.size());
            for (ContextIndexBuilder.ContextInfo contextInfo : contextInfoSet) {
                String contextStr = LogAggregator.aggregate(contextInfo);
                contextList.add(contextStr);
            }
            return contextList;
        }
    }
}
