package cn.yunyichina.log.index.aggregator.imp;

import cn.yunyichina.log.index.aggregator.AbstractAggregator;
import cn.yunyichina.log.index.builder.imp.KeywordIndexBuilder;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 15:26
 * @Description:
 */
public class KeywordIndexAggregator extends AbstractAggregator<Map<String, Set<KeywordIndexBuilder.IndexInfo>>> {

    public KeywordIndexAggregator() {
        this.aggregatedCollection = new HashMap<>(DEFAULT_CAPACITY);
    }

    @Override
    public Map<String, Set<KeywordIndexBuilder.IndexInfo>> aggregate(Map<String, Set<KeywordIndexBuilder.IndexInfo>> input) {
        if (!CollectionUtils.isEmpty(input)) {
            Set<Map.Entry<String, Set<KeywordIndexBuilder.IndexInfo>>> entrySet = input.entrySet();
            for (Map.Entry<String, Set<KeywordIndexBuilder.IndexInfo>> entry : entrySet) {
                Set<KeywordIndexBuilder.IndexInfo> indexInfoSet = aggregatedCollection.get(entry.getKey());
                if (indexInfoSet == null) {
                    indexInfoSet = entry.getValue();
                } else {
                    indexInfoSet.addAll(entry.getValue());
                }
                aggregatedCollection.put(entry.getKey(), indexInfoSet);
            }
        }
        return aggregatedCollection;
    }
}
