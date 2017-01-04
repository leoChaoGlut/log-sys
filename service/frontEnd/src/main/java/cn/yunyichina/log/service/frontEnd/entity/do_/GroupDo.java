package cn.yunyichina.log.service.frontEnd.entity.do_;

import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/4 11:26
 * @Description:
 */
public class GroupDo {
    private Integer id;
    private String name;

    private Set<CollectorDo> collectorDoSet;

    public Integer getId() {
        return id;
    }

    public GroupDo setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GroupDo setName(String name) {
        this.name = name;
        return this;
    }

    public Set<CollectorDo> getCollectorDoSet() {
        return collectorDoSet;
    }

    public GroupDo setCollectorDoSet(Set<CollectorDo> collectorDoSet) {
        this.collectorDoSet = collectorDoSet;
        return this;
    }
}
