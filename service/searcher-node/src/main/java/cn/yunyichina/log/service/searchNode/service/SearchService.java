package cn.yunyichina.log.service.searchNode.service;

import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.component.aggregator.log.LogAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.searchEngine.imp.KeyValueSearchEngine;
import cn.yunyichina.log.component.searchEngine.imp.KeywordSearchEngine;
import cn.yunyichina.log.service.searchNode.constant.SearchEngineType;
import cn.yunyichina.log.service.searchNode.util.IndexManager;
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

    public List<String> search(SearchCondition searchCondition) throws Exception {
        Set<ContextIndexBuilder.ContextInfo> contextInfoSet;
        switch (searchCondition.getSearchEngineType()) {
            case SearchEngineType.KEYWORD:
                contextInfoSet = new KeywordSearchEngine(indexManager.getKeywordIndexMap(), indexManager.getContextIndexMap(), searchCondition).search();
                break;
            case SearchEngineType.KEY_VALUE:
                contextInfoSet = new KeyValueSearchEngine(indexManager.getKeyValueIndexMap(), indexManager.getContextIndexMap(), searchCondition).search();
                break;
            default:
                throw new Exception("不支持的搜索引擎类型");
        }
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
