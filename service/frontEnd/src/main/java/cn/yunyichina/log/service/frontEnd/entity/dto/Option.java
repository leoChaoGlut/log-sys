package cn.yunyichina.log.service.frontEnd.entity.dto;

import cn.yunyichina.log.common.entity.po.Collector;

import java.util.List;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 10:36
 * @Description:
 */
public class Option {
    private List<Collector> collectorList;
    private List<String> keywordList;
    private Map<String, String> keyValueMap;

    public Option() {
    }

    public Option(List<Collector> collectorList, List<String> keywordList, Map<String, String> keyValueMap) {
        this.collectorList = collectorList;
        this.keywordList = keywordList;
        this.keyValueMap = keyValueMap;
    }

    public List<Collector> getCollectorList() {
        return collectorList;
    }

    public Option setCollectorList(List<Collector> collectorList) {
        this.collectorList = collectorList;
        return this;
    }

    public List<String> getKeywordList() {
        return keywordList;
    }

    public Option setKeywordList(List<String> keywordList) {
        this.keywordList = keywordList;
        return this;
    }

    public Map<String, String> getKeyValueMap() {
        return keyValueMap;
    }

    public Option setKeyValueMap(Map<String, String> keyValueMap) {
        this.keyValueMap = keyValueMap;
        return this;
    }
}
