package cn.yunyichina.log.common.entity.entity.po;

import java.util.Date;

/**
 * @Author: Leo
 * @Description: Reverse Engine By Leo
 */
public class KeywordTag {
    private Integer id;
    private String collector_name;
    private String application_name;
    private String keyword;
    private Date create_time;

    public Integer getId() {
        return id;
    }

    public KeywordTag setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCollector_name() {
        return collector_name;
    }

    public KeywordTag setCollector_name(String collector_name) {
        this.collector_name = collector_name;
        return this;
    }

    public String getApplication_name() {
        return application_name;
    }

    public KeywordTag setApplication_name(String application_name) {
        this.application_name = application_name;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public KeywordTag setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public KeywordTag setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }
}
