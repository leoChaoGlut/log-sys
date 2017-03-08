package cn.yunyichina.log.component.aggregator.index.imp;

import cn.yunyichina.log.component.aggregator.index.AbstractIndexAggregator;
import cn.yunyichina.log.component.index.entity.KeywordIndex;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 15:26
 * @Description:
 */
public class KeywordIndexAggregator extends AbstractIndexAggregator<ConcurrentHashMap<String, Set<KeywordIndex>>> {

    public KeywordIndexAggregator() {
        this.aggregatedCollection = new ConcurrentHashMap<>(DEFAULT_CAPACITY);
    }

    @Override
    public ConcurrentHashMap<String, Set<KeywordIndex>> aggregate(ConcurrentHashMap<String, Set<KeywordIndex>> input) {
        if (null == input || input.isEmpty()) {

        } else {
            Set<Entry<String, Set<KeywordIndex>>> inputEntrySet = input.entrySet();
            for (Entry<String, Set<KeywordIndex>> inputEntry : inputEntrySet) {
                Set<KeywordIndex> keywordIndexSet = aggregatedCollection.get(inputEntry.getKey());
                if (keywordIndexSet == null) {
                    keywordIndexSet = inputEntry.getValue();
                } else {
                    keywordIndexSet.addAll(inputEntry.getValue());
                }
                aggregatedCollection.put(inputEntry.getKey(), keywordIndexSet);
            }
        }
        return aggregatedCollection;
    }
}
