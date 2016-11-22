package cn.yunyichina.log.component.aggregator.index.imp;

import cn.yunyichina.log.component.aggregator.index.AbstractIndexAggregator;
import cn.yunyichina.log.component.indexBuilder.imp.KeywordIndexBuilder;
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
public class KeywordIndexAggregator extends AbstractIndexAggregator<Map<String, Set<KeywordIndexBuilder.IndexInfo>>> {

    public KeywordIndexAggregator() {
        this.aggregatedCollection = new HashMap<>(DEFAULT_CAPACITY);
    }

    @Override
    public Map<String, Set<KeywordIndexBuilder.IndexInfo>> aggregate(Map<String, Set<KeywordIndexBuilder.IndexInfo>> input) {
        if (!CollectionUtils.isEmpty(input)) {
            Set<Map.Entry<String, Set<KeywordIndexBuilder.IndexInfo>>> inputEntrySet = input.entrySet();
            for (Map.Entry<String, Set<KeywordIndexBuilder.IndexInfo>> inputEntry : inputEntrySet) {
                Set<KeywordIndexBuilder.IndexInfo> indexInfoSet = aggregatedCollection.get(inputEntry.getKey());
                if (indexInfoSet == null) {
                    indexInfoSet = inputEntry.getValue();
                } else {
                    indexInfoSet.addAll(inputEntry.getValue());
                }
                aggregatedCollection.put(inputEntry.getKey(), indexInfoSet);
            }
        }
        return aggregatedCollection;
    }
}
