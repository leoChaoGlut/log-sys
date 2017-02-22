package cn.yunyichina.log.common.entity.entity.do_;

import java.util.Objects;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/22 14:14
 * @Description:
 */
public class KvTagDO {
    private Integer id;
    private String key;
    private String keyTag;
    private String valueEndTag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KvTagDO kvTagDO = (KvTagDO) o;
        return Objects.equals(id, kvTagDO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Integer getId() {
        return id;
    }

    public KvTagDO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public KvTagDO setKey(String key) {
        this.key = key;
        return this;
    }

    public String getKeyTag() {
        return keyTag;
    }

    public KvTagDO setKeyTag(String keyTag) {
        this.keyTag = keyTag;
        return this;
    }

    public String getValueEndTag() {
        return valueEndTag;
    }

    public KvTagDO setValueEndTag(String valueEndTag) {
        this.valueEndTag = valueEndTag;
        return this;
    }
}
