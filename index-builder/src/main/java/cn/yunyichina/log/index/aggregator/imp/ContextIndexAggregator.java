package cn.yunyichina.log.index.aggregator.imp;

import cn.yunyichina.log.index.aggregator.AbstractAggregator;
import cn.yunyichina.log.index.builder.imp.ContextIndexBuilder;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 14:48
 * @Description:
 */
public class ContextIndexAggregator extends AbstractAggregator<Map<Long, ContextIndexBuilder.ContextInfo>> {

    /**
     * 是否替换旧值
     */
    protected boolean replaceOldValue = false;

    public ContextIndexAggregator() {
        this.aggregatedCollection = new HashMap<>(DEFAULT_CAPACITY);
    }

    public ContextIndexAggregator(boolean replaceOldValue) {
        this.replaceOldValue = replaceOldValue;
        this.aggregatedCollection = new HashMap<>(DEFAULT_CAPACITY);
    }

    @Override
    public Map<Long, ContextIndexBuilder.ContextInfo> aggregate(Map<Long, ContextIndexBuilder.ContextInfo> input) {
        if (!CollectionUtils.isEmpty(input)) {
            Set<Map.Entry<Long, ContextIndexBuilder.ContextInfo>> entrySet = input.entrySet();
            for (Map.Entry<Long, ContextIndexBuilder.ContextInfo> entry : entrySet) {
                if (replaceOldValue) {
                    aggregatedCollection.put(entry.getKey(), entry.getValue());
                } else {
                    if (!aggregatedCollection.containsKey(entry.getKey())) {
                        aggregatedCollection.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return aggregatedCollection;
    }

}
