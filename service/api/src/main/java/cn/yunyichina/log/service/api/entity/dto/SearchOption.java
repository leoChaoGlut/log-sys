package cn.yunyichina.log.service.api.entity.dto;


import cn.yunyichina.log.common.entity.entity.po.Collector;

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
    private Map<String, List<String>> collectorKeywordMap;
    private Map<String, List<String>> collectorKeyMap;

    public Map<String, List<Collector>> getGroupCollectorMap() {
        return groupCollectorMap;
    }

    public SearchOption setGroupCollectorMap(Map<String, List<Collector>> groupCollectorMap) {
        this.groupCollectorMap = groupCollectorMap;
        return this;
    }

    public Map<String, List<String>> getCollectorKeywordMap() {
        return collectorKeywordMap;
    }

    public SearchOption setCollectorKeywordMap(Map<String, List<String>> collectorKeywordMap) {
        this.collectorKeywordMap = collectorKeywordMap;
        return this;
    }

    public Map<String, List<String>> getCollectorKeyMap() {
        return collectorKeyMap;
    }

    public SearchOption setCollectorKeyMap(Map<String, List<String>> collectorKeyMap) {
        this.collectorKeyMap = collectorKeyMap;
        return this;
    }
}
