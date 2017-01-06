package cn.yunyichina.log.component.aggregator.index.imp;

import cn.yunyichina.log.component.aggregator.index.AbstractIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
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
public class ContextIndexAggregator extends AbstractIndexAggregator<Map<Long, ContextIndexBuilder.ContextInfo>> {

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
                ContextIndexBuilder.ContextInfo contextInfo = aggregatedCollection.get(entry.getKey());
                if (contextInfo == null) {
                    contextInfo = entry.getValue();
                } else {
                    ContextIndexBuilder.ContextInfo inputConextInfo = entry.getValue();
                    if (inputConextInfo == null) {

                    } else {
                        ContextIndexBuilder.IndexInfo inputBegin = inputConextInfo.getBegin();
                        ContextIndexBuilder.IndexInfo inputEnd = inputConextInfo.getEnd();
                        if (replaceOldValue) {
                            if (inputBegin != null) {
                                contextInfo.setBegin(inputBegin);
                            }
                            if (inputEnd != null) {
                                contextInfo.setEnd(inputEnd);
                            }
                        } else {
                            if (contextInfo.getBegin() == null) {
                                contextInfo.setBegin(inputBegin);
                            }
                            if (contextInfo.getEnd() == null) {
                                contextInfo.setEnd(inputEnd);
                            }
                        }
                    }
                }
                aggregatedCollection.put(entry.getKey(), contextInfo);
            }
        }
        return aggregatedCollection;
    }

}
