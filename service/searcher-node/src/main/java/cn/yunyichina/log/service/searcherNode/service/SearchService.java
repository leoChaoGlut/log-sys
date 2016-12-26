package cn.yunyichina.log.service.searcherNode.service;

import cn.yunyichina.log.common.constant.SearchEngineType;
import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.component.aggregator.log.LogAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.searchEngine.imp.KeyValueSearchEngine;
import cn.yunyichina.log.component.searchEngine.imp.KeywordSearchEngine;
import cn.yunyichina.log.service.searcherNode.util.IndexManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 14:49
 * @Description:
 */
@Service
public class SearchService {

    @Autowired
    IndexManager indexManager;

    public List<String> history(SearchCondition condition) throws Exception {
        Set<ContextIndexBuilder.ContextInfo> contextInfoSet = null;
        switch (condition.getSearchEngineType()) {
            case SearchEngineType.KEYWORD:
                contextInfoSet = new KeywordSearchEngine(indexManager.getKeywordIndexMap(), indexManager.getContextIndexMap(), condition).search();
                break;
            case SearchEngineType.KEY_VALUE:
                contextInfoSet = new KeyValueSearchEngine(indexManager.getKeyValueIndexMap(), indexManager.getContextIndexMap(), condition).search();
                break;
            case SearchEngineType.NO_INDEX:
//                new NoIndexSearchEngine().search();
                break;
            default:
                throw new Exception("不支持的搜索引擎类型:" + condition.getSearchEngineType());
        }
        if (contextInfoSet == null) {
            return new ArrayList<>();
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
