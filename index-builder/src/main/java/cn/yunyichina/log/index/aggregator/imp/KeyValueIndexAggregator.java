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
            Set<Map.Entry<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>>> entrySet = input.entrySet();
            if (!CollectionUtils.isEmpty(entrySet)) {
                for (Map.Entry<String, Map<String, List<KeyValueIndexBuilder.IndexInfo>>> entry : entrySet) {

                }
            }
        }
        return null;
    }
}
