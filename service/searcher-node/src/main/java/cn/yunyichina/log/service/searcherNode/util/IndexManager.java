package cn.yunyichina.log.service.searcherNode.util;

import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 11:10
 * @Description:
 */
@Component
public class IndexManager {

    Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap;
    Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap;
    Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap;

    @Value("${constants.index.rootDir}")
    private String ROOT_DIR;

    @PostConstruct
    public void init() {
        appendContextIndex(null);
        appendKeywordIndex(null);
        appendKeyValueIndex(null);
    }


    public void appendKeyValueIndex(Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap) {
        if (this.keyValueIndexMap == null) {
            if (keyValueIndexMap == null) {
                this.keyValueIndexMap = new HashMap<>();
            } else {
                this.keyValueIndexMap = keyValueIndexMap;
            }
        } else {
            KeyValueIndexAggregator aggregator = new KeyValueIndexAggregator();
            aggregator.aggregate(this.keyValueIndexMap);
            aggregator.aggregate(keyValueIndexMap);
            this.keyValueIndexMap = aggregator.getAggregatedCollection();
        }
    }

    public void appendKeywordIndex(Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap) {
        if (this.keywordIndexMap == null) {
            if (keywordIndexMap == null) {
                this.keywordIndexMap = new HashMap<>();
            } else {
                this.keywordIndexMap = keywordIndexMap;
            }
        } else {
            KeywordIndexAggregator aggregator = new KeywordIndexAggregator();
            aggregator.aggregate(this.keywordIndexMap);
            aggregator.aggregate(keywordIndexMap);
            this.keywordIndexMap = aggregator.getAggregatedCollection();
        }
    }

    public void appendContextIndex(Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap) {
        if (this.contextIndexMap == null) {
            if (contextIndexMap == null) {
                this.contextIndexMap = new HashMap<>();
            } else {
                this.contextIndexMap = contextIndexMap;
            }
        } else {
            ContextIndexAggregator aggregator = new ContextIndexAggregator();
            aggregator.aggregate(this.contextIndexMap);
            aggregator.aggregate(contextIndexMap);
            this.contextIndexMap = aggregator.getAggregatedCollection();
        }
    }

    public Map<Long, ContextIndexBuilder.ContextInfo> getContextIndexMap() {
        return contextIndexMap;
    }

    public Map<String, Set<KeywordIndexBuilder.IndexInfo>> getKeywordIndexMap() {
        return keywordIndexMap;
    }

    public Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> getKeyValueIndexMap() {
        return keyValueIndexMap;
    }
}
