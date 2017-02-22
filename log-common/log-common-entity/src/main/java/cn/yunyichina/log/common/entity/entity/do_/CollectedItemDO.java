package cn.yunyichina.log.common.entity.entity.do_;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/22 14:20
 * @Description:
 */
public class CollectedItemDO {
    private Integer id;
    private String name;
    private Integer collectorId;

    //    extra
    private List<KvTagDO> kvTagDOList;
    private List<KeywordTagDO> keywordTagDOList;

    public Integer getId() {
        return id;
    }

    public CollectedItemDO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CollectedItemDO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCollectorId() {
        return collectorId;
    }

    public CollectedItemDO setCollectorId(Integer collectorId) {
        this.collectorId = collectorId;
        return this;
    }

    public List<KvTagDO> getKvTagDOList() {
        return kvTagDOList;
    }

    public CollectedItemDO setKvTagDOList(List<KvTagDO> kvTagDOList) {
        this.kvTagDOList = kvTagDOList;
        return this;
    }

    public List<KeywordTagDO> getKeywordTagDOList() {
        return keywordTagDOList;
    }

    public CollectedItemDO setKeywordTagDOList(List<KeywordTagDO> keywordTagDOList) {
        this.keywordTagDOList = keywordTagDOList;
        return this;
    }
}
