package cn.yunyichina.log.component.aggregator.index;

import cn.yunyichina.log.component.aggregator.Aggregator;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 15:08
 * @Description:
 */
public abstract class AbstractIndexAggregator<T> implements Aggregator<T> {

    protected T aggregatedCollection;
    protected final int DEFAULT_CAPACITY = 1024;


    public AbstractIndexAggregator() {
    }

    public AbstractIndexAggregator(T aggregatedCollection) {
        this.aggregatedCollection = aggregatedCollection;
    }

    public T getAggregatedCollection() {
        return aggregatedCollection;
    }

    public AbstractIndexAggregator setAggregatedCollection(T aggregatedCollection) {
        this.aggregatedCollection = aggregatedCollection;
        return this;
    }

}
