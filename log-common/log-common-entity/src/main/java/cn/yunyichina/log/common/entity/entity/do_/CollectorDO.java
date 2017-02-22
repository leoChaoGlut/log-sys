package cn.yunyichina.log.common.entity.entity.do_;

import java.util.Date;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/22 14:19
 * @Description:
 */
public class CollectorDO {
    private Integer id;
    private String name;
    private String applicationName;
    private String remark;
    private Date createTime;

    //    extra
    private List<CollectedItemDO> collectedItemDOList;

    public Integer getId() {
        return id;
    }

    public CollectorDO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CollectorDO setName(String name) {
        this.name = name;
        return this;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public CollectorDO setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public CollectorDO setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public CollectorDO setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public List<CollectedItemDO> getCollectedItemDOList() {
        return collectedItemDOList;
    }

    public CollectorDO setCollectedItemDOList(List<CollectedItemDO> collectedItemDOList) {
        this.collectedItemDOList = collectedItemDOList;
        return this;
    }
}
