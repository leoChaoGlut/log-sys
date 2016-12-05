package cn.yunyichina.log.service.searcherNode.util;

import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 11:10
 * @Description: TODO 这个地方的设计,我感觉比较糟糕,待优化
 */
@Component
public class IndexManager {

    private volatile Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap;
    private volatile Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap;
    private volatile Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap;
    /**
     * 假设有一个线程正在读索引数据,此时如果正好有upload请求,并且upload在update index,就可能会出现Hashmap并发问题
     * Hashmap在存在写线程的情况下,可能会出现读死循环的问题,所以不能使用ReadWriteLock,只能强行加锁.
     * 或者,可以把数据结构改为ConcurrentHashMap....有缘人看到此处可以改改~ 前提是你了解了整个模块的职责.
     */
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    @Value("${constants.index.rootDir}")
    private String INDEX_ROOT_DIR;

    @PostConstruct
    public void init() {
        appendContextIndex(null);
        appendKeywordIndex(null);
        appendKeyValueIndex(null);
    }


    public void appendKeyValueIndex(Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap) {
        writeLock.lock();
        try {
            if (this.keyValueIndexMap == null) {
                if (keyValueIndexMap == null) {
                    this.keyValueIndexMap = new HashMap<>();
                } else {
                    this.keyValueIndexMap = keyValueIndexMap;
                }
            } else {
                KeyValueIndexAggregator aggregator = new KeyValueIndexAggregator();
                aggregator.aggregate(this.keyValueIndexMap);
                aggregator.aggregate(keyValueIndexMap);
                this.keyValueIndexMap = aggregator.getAggregatedCollection();
            }
        } finally {
            writeLock.unlock();
        }
    }

    public void appendKeywordIndex(Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap) {
        writeLock.lock();
        try {
            if (this.keywordIndexMap == null) {
                if (keywordIndexMap == null) {
                    this.keywordIndexMap = new HashMap<>();
                } else {
                    this.keywordIndexMap = keywordIndexMap;
                }
            } else {
                KeywordIndexAggregator aggregator = new KeywordIndexAggregator();
                aggregator.aggregate(this.keywordIndexMap);
                aggregator.aggregate(keywordIndexMap);
                this.keywordIndexMap = aggregator.getAggregatedCollection();
            }
        } finally {
            writeLock.unlock();
        }
    }

    public void appendContextIndex(Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap) {
        writeLock.lock();
        try {
            if (this.contextIndexMap == null) {
                if (contextIndexMap == null) {
                    this.contextIndexMap = new HashMap<>();
                } else {
                    this.contextIndexMap = contextIndexMap;
                }
            } else {
                ContextIndexAggregator aggregator = new ContextIndexAggregator();
                aggregator.aggregate(this.contextIndexMap);
                aggregator.aggregate(contextIndexMap);
                this.contextIndexMap = aggregator.getAggregatedCollection();
            }
        } finally {
            writeLock.unlock();
        }
    }

    public Map<Long, ContextIndexBuilder.ContextInfo> getContextIndexMap() {
        readLock.lock();
        try {
            return contextIndexMap;
        } finally {
            readLock.unlock();
        }
    }

    public Map<String, Set<KeywordIndexBuilder.IndexInfo>> getKeywordIndexMap() {
        readLock.lock();
        try {
            return keywordIndexMap;
        } finally {
            readLock.unlock();
        }
    }

    public Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> getKeyValueIndexMap() {
        readLock.lock();
        try {
            return keyValueIndexMap;
        } finally {
            readLock.unlock();
        }
    }
}
