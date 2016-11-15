package cn.yunyichina.log.index.aggregator.imp;

import cn.yunyichina.log.index.aggregator.AbstractAggregator;
import cn.yunyichina.log.index.builder.imp.KeyValueIndexBuilder;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 15:38
 * @Description:
 */
public class KeyValueIndexAggregator extends AbstractAggregator<Map<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>>> {

    public KeyValueIndexAggregator() {
        this.aggregatedCollection = new HashMap<>(DEFAULT_CAPACITY);
    }

    @Override
    public Map<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>> aggregate(Map<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>> input) {
        if (!CollectionUtils.isEmpty(input)) {
            Set<Map.Entry<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>>> kvIndexEntrySet = input.entrySet();
            for (Map.Entry<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>> kvIndexEntry : kvIndexEntrySet) {
                Map<String, List<KeyValueIndexBuilder.IndexInfo>> valueIndex = aggregatedCollection.get(kvIndexEntry.getKey());
                if (valueIndex == null) {
                    valueIndex = kvIndexEntry.getValue();
                } else {
                    Set<Map.Entry<String, List<KeyValueIndexBuilder.IndexInfo>>> valueIndexEntrySet = valueIndex.entrySet();
                    if (!CollectionUtils.isEmpty(valueIndexEntrySet)) {
                        for (Map.Entry<String, List<KeyValueIndexBuilder.IndexInfo>> valueIndexEntry : valueIndexEntrySet) {
                            List<KeyValueIndexBuilder.IndexInfo> indexInfoList = valueIndex.get(valueIndexEntry.getKey());
                            if (indexInfoList == null) {
                                indexInfoList = valueIndexEntry.getValue();
                            } else {
                                indexInfoList.addAll(valueIndexEntry.getValue());
                            }
                            valueIndex.put(valueIndexEntry.getKey(), indexInfoList);
                        }
                    }
                }
                aggregatedCollection.put(kvIndexEntry.getKey(), valueIndex);
            }
        }
        return aggregatedCollection;
    }
}
