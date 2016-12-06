package cn.yunyichina.log.common.entity.po;

/**
 * @Author: Leo
 * @Description: Reversal Script Written By Leo 数据库表
 */
public class Kvtag {
    private Integer id;
    private String key;
    private String key_tag;
    private String value_end_tag;

    public Integer getId() {
        return id;
    }

    public Kvtag setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Kvtag setKey(String key) {
        this.key = key;
        return this;
    }

    public String getKey_tag() {
        return key_tag;
    }

    public Kvtag setKey_tag(String key_tag) {
        this.key_tag = key_tag;
        return this;
    }

    public String getValue_end_tag() {
        return value_end_tag;
    }

    public Kvtag setValue_end_tag(String value_end_tag) {
        this.value_end_tag = value_end_tag;
        return this;
    }
}
