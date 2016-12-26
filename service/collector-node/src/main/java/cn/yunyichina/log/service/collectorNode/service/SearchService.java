package cn.yunyichina.log.service.collectorNode.service;

import cn.yunyichina.log.common.constant.SearchEngineType;
import cn.yunyichina.log.common.entity.dto.SearchCondition;
import cn.yunyichina.log.component.aggregator.log.LogAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.searchEngine.imp.KeyValueSearchEngine;
import cn.yunyichina.log.component.searchEngine.imp.KeywordSearchEngine;
import cn.yunyichina.log.service.collectorNode.constants.Config;
import cn.yunyichina.log.service.collectorNode.constants.Key;
import cn.yunyichina.log.service.collectorNode.util.IndexManager;
import cn.yunyichina.log.service.collectorNode.util.PropertiesUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
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
    Config config;

    @Autowired
    PropertiesUtil propUtil;

    public List<String> realtime(SearchCondition searchCondition) throws Exception {
        List<String> keywordList = JSON.parseArray(propUtil.get(Key.KEYWORD_SET), String.class);
        List<KeyValueIndexBuilder.KvTag> kvTagList = JSON.parseArray(propUtil.get(Key.KV_TAG_SET), KeyValueIndexBuilder.KvTag.class);
        if (CollectionUtils.isEmpty(keywordList) && CollectionUtils.isEmpty(kvTagList)) {
            throw new Exception("keyword和keyValue为空,请检查properties.");
        } else {
            if (CollectionUtils.isEmpty(keywordList)) {//防止hashset初始化null异常
                keywordList = new ArrayList<>();
            } else if (CollectionUtils.isEmpty(kvTagList)) {
                kvTagList = new ArrayList<>();
            }
            Set<String> keywordSet = new HashSet<>(keywordList);
            Set<KeyValueIndexBuilder.KvTag> kvTagSet = new HashSet<>(kvTagList);
            String beginDatetime = propUtil.get(Key.LAST_MODIFY_TIME);
            IndexManager indexManager = new IndexManager(searchCondition, kvTagSet, keywordSet, beginDatetime, config.getLogRootDir());
            Set<ContextIndexBuilder.ContextInfo> contextInfoSet = null;

            switch (searchCondition.getSearchEngineType()) {
                case SearchEngineType.KEYWORD:
                    contextInfoSet = new KeywordSearchEngine(indexManager.getKeywordIndexMap(), indexManager.getContextIndexMap(), searchCondition).search();
                    break;
                case SearchEngineType.KEY_VALUE:
                    contextInfoSet = new KeyValueSearchEngine(indexManager.getKeyValueIndexMap(), indexManager.getContextIndexMap(), searchCondition).search();
                    break;
                default:
                    throw new Exception("不支持的搜索引擎类型:" + searchCondition.getSearchEngineType());
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

}
