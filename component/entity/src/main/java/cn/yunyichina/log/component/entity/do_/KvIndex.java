package cn.yunyichina.log.component.entity.do_;

import java.util.Date;
import java.util.Objects;

/**
 * @Author: Leo
 * @Description: Reverse Engine By Leo
 */
public class KvIndex {
    private Integer id;
    private String key;
    private String value;
    private Date create_time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KvIndex kvIndex = (KvIndex) o;
        return Objects.equals(id, kvIndex.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Integer getId() {
        return id;
    }

    public KvIndex setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public KvIndex setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public KvIndex setValue(String value) {
        this.value = value;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public KvIndex setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }
}
