package cn.yunyichina.log.component.aggregator.index.imp;

import cn.yunyichina.log.component.aggregator.index.AbstractIndexAggregator;
import cn.yunyichina.log.component.index.entity.KvIndex;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 15:38
 * @Description:
 */
public class KvIndexAggregator extends AbstractIndexAggregator<ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>>> {

    public KvIndexAggregator() {
        this.aggregatedCollection = new ConcurrentHashMap<>(DEFAULT_CAPACITY);
    }

    @Override
    public ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> aggregate(ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>> input) {
        if (null == input || input.isEmpty()) {

        } else {
            Set<Entry<String, ConcurrentHashMap<String, Set<KvIndex>>>> inputEntrySet0 = input.entrySet();
            for (Entry<String, ConcurrentHashMap<String, Set<KvIndex>>> inputEntry0 : inputEntrySet0) {
                ConcurrentHashMap<String, Set<KvIndex>> valueIndexMap = aggregatedCollection.get(inputEntry0.getKey());
                if (valueIndexMap == null) {
                    valueIndexMap = inputEntry0.getValue();
                } else {
                    Set<Entry<String, Set<KvIndex>>> inputEntrySet1 = inputEntry0.getValue().entrySet();
                    if (null == inputEntrySet1 || inputEntrySet1.isEmpty()) {

                    } else {
                        for (Entry<String, Set<KvIndex>> inputEntry1 : inputEntrySet1) {
                            Set<KvIndex> kvIndexSet = valueIndexMap.get(inputEntry1.getKey());
                            if (kvIndexSet == null) {
                                kvIndexSet = inputEntry1.getValue();
                            } else {
                                kvIndexSet.addAll(inputEntry1.getValue());
                            }
                            valueIndexMap.put(inputEntry1.getKey(), kvIndexSet);
                        }
                    }
                }
                aggregatedCollection.put(inputEntry0.getKey(), valueIndexMap);
            }
        }
        return aggregatedCollection;
    }
}
