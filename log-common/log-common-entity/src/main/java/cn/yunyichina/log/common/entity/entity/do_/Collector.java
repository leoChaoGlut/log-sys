package cn.yunyichina.log.common.entity.entity.do_;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: Leo
 * @Description: Reversal Script Written By Leo
 */
public class Collector {
    private Integer id;
    private String name;
    private Integer group_id;
    private Date create_time;
    private String service_name;

    //extra
    private Set<KeywordIndex> keywordIndexSet;
    private Set<KvIndex> kvIndexSet;
    private KeywordIndex keywordIndex;
    private KvIndex kvIndex;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collector collector = (Collector) o;
        return Objects.equals(id, collector.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public KeywordIndex getKeywordIndex() {
        return keywordIndex;
    }

    public Collector setKeywordIndex(KeywordIndex keywordIndex) {
        this.keywordIndex = keywordIndex;
        return this;
    }

    public KvIndex getKvIndex() {
        return kvIndex;
    }

    public Collector setKvIndex(KvIndex kvIndex) {
        this.kvIndex = kvIndex;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Collector setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Collector setName(String name) {
        this.name = name;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public Collector setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }

    public String getService_name() {
        return service_name;
    }

    public Collector setService_name(String service_name) {
        this.service_name = service_name;
        return this;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public Collector setGroup_id(Integer group_id) {
        this.group_id = group_id;
        return this;
    }

    public Set<KeywordIndex> getKeywordIndexSet() {
        return keywordIndexSet;
    }

    public Collector setKeywordIndexSet(Set<KeywordIndex> keywordIndexSet) {
        this.keywordIndexSet = keywordIndexSet;
        return this;
    }

    public Set<KvIndex> getKvIndexSet() {
        return kvIndexSet;
    }

    public Collector setKvIndexSet(Set<KvIndex> kvIndexSet) {
        this.kvIndexSet = kvIndexSet;
        return this;
    }


}
