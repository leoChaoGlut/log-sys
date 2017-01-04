package cn.yunyichina.log.service.frontEnd.entity.do_;

import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/4 11:30
 * @Description:
 */
public class CollectorDo {
    private Integer id;
    private String name;
    private Integer group_id;
    private String service_name;

    private Set<KeywordDo> keywordDoSet;
    private Set<KeyValueDo> keyValueDoSet;

    public Integer getId() {
        return id;
    }

    public CollectorDo setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CollectorDo setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public CollectorDo setGroup_id(Integer group_id) {
        this.group_id = group_id;
        return this;
    }

    public String getService_name() {
        return service_name;
    }

    public CollectorDo setService_name(String service_name) {
        this.service_name = service_name;
        return this;
    }

    public Set<KeywordDo> getKeywordDoSet() {
        return keywordDoSet;
    }

    public CollectorDo setKeywordDoSet(Set<KeywordDo> keywordDoSet) {
        this.keywordDoSet = keywordDoSet;
        return this;
    }

    public Set<KeyValueDo> getKeyValueDoSet() {
        return keyValueDoSet;
    }

    public CollectorDo setKeyValueDoSet(Set<KeyValueDo> keyValueDoSet) {
        this.keyValueDoSet = keyValueDoSet;
        return this;
    }
}
