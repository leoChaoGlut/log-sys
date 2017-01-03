package cn.yunyichina.log.service.frontEnd.entity.dto;

import cn.yunyichina.log.common.entity.po.Collector;
import cn.yunyichina.log.common.entity.po.Group;
import cn.yunyichina.log.common.entity.po.KeywordIndex;
import cn.yunyichina.log.common.entity.po.KvIndex;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 10:36
 * @Description:
 */
public class SearchOption {

    private List<Group> groupList;
    private List<Collector> collectorList;
    private List<KeywordIndex> keywordIndexList;
    private List<KvIndex> kvIndexList;

    public List<Group> getGroupList() {
        return groupList;
    }

    public SearchOption setGroupList(List<Group> groupList) {
        this.groupList = groupList;
        return this;
    }

    public List<Collector> getCollectorList() {
        return collectorList;
    }

    public SearchOption setCollectorList(List<Collector> collectorList) {
        this.collectorList = collectorList;
        return this;
    }

    public List<KeywordIndex> getKeywordIndexList() {
        return keywordIndexList;
    }

    public SearchOption setKeywordIndexList(List<KeywordIndex> keywordIndexList) {
        this.keywordIndexList = keywordIndexList;
        return this;
    }

    public List<KvIndex> getKvIndexList() {
        return kvIndexList;
    }

    public SearchOption setKvIndexList(List<KvIndex> kvIndexList) {
        this.kvIndexList = kvIndexList;
        return this;
    }
}
