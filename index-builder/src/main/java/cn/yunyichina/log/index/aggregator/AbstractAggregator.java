package cn.yunyichina.log.index.aggregator;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/15 15:08
 * @Description:
 */
public abstract class AbstractAggregator<T> implements Aggregator<T> {

    protected T aggregatedCollection;
    protected final int DEFAULT_CAPACITY = 1024;


    public AbstractAggregator() {
    }

    public AbstractAggregator(T aggregatedCollection) {
        this.aggregatedCollection = aggregatedCollection;
    }

    public T getAggregatedCollection() {
        return aggregatedCollection;
    }

    public AbstractAggregator setAggregatedCollection(T aggregatedCollection) {
        this.aggregatedCollection = aggregatedCollection;
        return this;
    }

}
