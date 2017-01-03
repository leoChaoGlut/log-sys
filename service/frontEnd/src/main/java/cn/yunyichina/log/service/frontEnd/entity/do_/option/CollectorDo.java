package cn.yunyichina.log.service.frontEnd.entity.do_.option;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/3 16:39
 * @Description:
 */
public class CollectorDo {
    private String name;
    private String serviceName;
    private String groupId;

    private List<KeywordDo> keywordList;
    private List<KeyValueDo> keyValueList;

    public String getName() {
        return name;
    }

    public CollectorDo setName(String name) {
        this.name = name;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public CollectorDo setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public CollectorDo setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public List<KeywordDo> getKeywordList() {
        return keywordList;
    }

    public CollectorDo setKeywordList(List<KeywordDo> keywordList) {
        this.keywordList = keywordList;
        return this;
    }

    public List<KeyValueDo> getKeyValueList() {
        return keyValueList;
    }

    public CollectorDo setKeyValueList(List<KeyValueDo> keyValueList) {
        this.keyValueList = keyValueList;
        return this;
    }
}
