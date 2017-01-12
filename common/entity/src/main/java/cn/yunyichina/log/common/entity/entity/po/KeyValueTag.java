package cn.yunyichina.log.common.entity.entity.po;

import java.util.Date;

/**
 * @Author: Leo
 * @Description: Reverse Engine By Leo
 */
public class KeyValueTag {
    private Integer id;
    private String collector_name;
    private String application_name;
    private String key;
    private String key_tag;
    private String value_end_tag;
    private Date create_time;

    public Integer getId() {
        return id;
    }

    public KeyValueTag setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCollector_name() {
        return collector_name;
    }

    public KeyValueTag setCollector_name(String collector_name) {
        this.collector_name = collector_name;
        return this;
    }

    public String getApplication_name() {
        return application_name;
    }

    public KeyValueTag setApplication_name(String application_name) {
        this.application_name = application_name;
        return this;
    }

    public String getKey() {
        return key;
    }

    public KeyValueTag setKey(String key) {
        this.key = key;
        return this;
    }

    public String getKey_tag() {
        return key_tag;
    }

    public KeyValueTag setKey_tag(String key_tag) {
        this.key_tag = key_tag;
        return this;
    }

    public String getValue_end_tag() {
        return value_end_tag;
    }

    public KeyValueTag setValue_end_tag(String value_end_tag) {
        this.value_end_tag = value_end_tag;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public KeyValueTag setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }
}
