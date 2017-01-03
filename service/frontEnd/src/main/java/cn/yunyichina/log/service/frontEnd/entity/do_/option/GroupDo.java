package cn.yunyichina.log.service.frontEnd.entity.do_.option;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/3 16:37
 * @Description:
 */
public class GroupDo {
    private String name;

    private List<CollectorDo> collectorList;

    public String getName() {
        return name;
    }

    public GroupDo setName(String name) {
        this.name = name;
        return this;
    }

    public List<CollectorDo> getCollectorList() {
        return collectorList;
    }

    public GroupDo setCollectorList(List<CollectorDo> collectorList) {
        this.collectorList = collectorList;
        return this;
    }
}
