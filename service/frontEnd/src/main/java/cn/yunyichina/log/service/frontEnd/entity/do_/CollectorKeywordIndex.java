package cn.yunyichina.log.service.frontEnd.entity.do_;

import cn.yunyichina.log.common.entity.po.Collector;
import cn.yunyichina.log.common.entity.po.KeywordIndex;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/3 10:43
 * @Description:
 */
public class CollectorKeywordIndex {
    private Collector collector;
    private KeywordIndex keywordIndex;

    public Collector getCollector() {
        return collector;
    }

    public CollectorKeywordIndex setCollector(Collector collector) {
        this.collector = collector;
        return this;
    }

    public KeywordIndex getKeywordIndex() {
        return keywordIndex;
    }

    public CollectorKeywordIndex setKeywordIndex(KeywordIndex keywordIndex) {
        this.keywordIndex = keywordIndex;
        return this;
    }
}
