package cn.yunyichina.log.service.api.entity.dto;


import cn.yunyichina.log.common.entity.entity.do_.Collector;
import cn.yunyichina.log.common.entity.entity.do_.KeywordIndex;
import cn.yunyichina.log.common.entity.entity.do_.KvIndex;

import java.util.List;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/7 21:50
 * @Description:
 */
public class SearchOption {
    private Map<String, List<Collector>> groupCollectorMap;
    private Map<String, List<KeywordIndex>> collectorKeywordMap;
    private Map<String, List<KvIndex>> collectorKeyMap;

    public Map<String, List<Collector>> getGroupCollectorMap() {
        return groupCollectorMap;
    }

    public SearchOption setGroupCollectorMap(Map<String, List<Collector>> groupCollectorMap) {
        this.groupCollectorMap = groupCollectorMap;
        return this;
    }

    public Map<String, List<KeywordIndex>> getCollectorKeywordMap() {
        return collectorKeywordMap;
    }

    public SearchOption setCollectorKeywordMap(Map<String, List<KeywordIndex>> collectorKeywordMap) {
        this.collectorKeywordMap = collectorKeywordMap;
        return this;
    }

    public Map<String, List<KvIndex>> getCollectorKeyMap() {
        return collectorKeyMap;
    }

    public SearchOption setCollectorKeyMap(Map<String, List<KvIndex>> collectorKeyMap) {
        this.collectorKeyMap = collectorKeyMap;
        return this;
    }
}
