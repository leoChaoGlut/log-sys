package cn.yunyichina.log.component.aggregator.index.imp;

import cn.yunyichina.log.component.aggregator.index.AbstractIndexAggregator;
import cn.yunyichina.log.component.index.entity.ContextIndex;
import cn.yunyichina.log.component.index.entity.ContextInfo;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 14:48
 * @Description:
 */
public class ContextIndexAggregator extends AbstractIndexAggregator<ConcurrentHashMap<Long, ContextInfo>> {

    /**
     * 是否替换旧值
     */
    protected boolean replaceOldValue = false;

    public ContextIndexAggregator() {
        this(false);
    }

    public ContextIndexAggregator(boolean replaceOldValue) {
        this.replaceOldValue = replaceOldValue;
        this.aggregatedCollection = new ConcurrentHashMap<>(DEFAULT_CAPACITY);
    }

    @Override
    public ConcurrentHashMap<Long, ContextInfo> aggregate(ConcurrentHashMap<Long, ContextInfo> input) {
        if (null == input || input.isEmpty()) {

        } else {
            Set<Entry<Long, ContextInfo>> inputEntrySet = input.entrySet();
            for (Entry<Long, ContextInfo> inputEntry : inputEntrySet) {
                ContextInfo contextInfo = aggregatedCollection.get(inputEntry.getKey());
                if (contextInfo == null) {
                    contextInfo = inputEntry.getValue();
                } else {
                    ContextInfo inputConextInfo = inputEntry.getValue();
                    if (inputConextInfo == null) {

                    } else {
                        ContextIndex inputBegin = inputConextInfo.getBegin();
                        ContextIndex inputEnd = inputConextInfo.getEnd();
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
                aggregatedCollection.put(inputEntry.getKey(), contextInfo);
            }
        }
        return aggregatedCollection;
    }

}
