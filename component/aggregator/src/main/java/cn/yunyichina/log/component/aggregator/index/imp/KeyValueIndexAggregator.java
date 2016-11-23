package cn.yunyichina.log.component.aggregator.index.imp;

import cn.yunyichina.log.component.aggregator.index.AbstractIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 15:38
 * @Description:
 */
public class KeyValueIndexAggregator extends AbstractIndexAggregator<Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>>> {

    public KeyValueIndexAggregator() {
        this.aggregatedCollection = new HashMap<>(DEFAULT_CAPACITY);
    }

    @Override
    public Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> aggregate(Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> input) {
        if (!CollectionUtils.isEmpty(input)) {
            Set<Map.Entry<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>>> inputEntrySet0 = input.entrySet();
            for (Map.Entry<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> inputEntry0 : inputEntrySet0) {
                Map<String, Set<KeyValueIndexBuilder.IndexInfo>> valueIndex = aggregatedCollection.get(inputEntry0.getKey());
                if (valueIndex == null) {
                    valueIndex = inputEntry0.getValue();
                } else {
                    Set<Map.Entry<String, Set<KeyValueIndexBuilder.IndexInfo>>> inputEntrySet1 = inputEntry0.getValue().entrySet();
                    if (!CollectionUtils.isEmpty(inputEntrySet1)) {
                        for (Map.Entry<String, Set<KeyValueIndexBuilder.IndexInfo>> inputEntry1 : inputEntrySet1) {
                            Set<KeyValueIndexBuilder.IndexInfo> indexInfoSet = valueIndex.get(inputEntry1.getKey());
                            if (indexInfoSet == null) {
                                indexInfoSet = inputEntry1.getValue();
                            } else {
                                indexInfoSet.addAll(inputEntry1.getValue());
                            }
                            valueIndex.put(inputEntry1.getKey(), indexInfoSet);
                        }
                    }
                }
                aggregatedCollection.put(inputEntry0.getKey(), valueIndex);
            }
        }
        return aggregatedCollection;
    }
}
