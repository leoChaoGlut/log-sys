package cn.yunyichina.log.component.entity.po;

import java.util.Date;

/**
 * @Author: Leo
 * @Description: Reverse Engine By Leo
 */
public class KeywordTag {
    private Integer id;
    private Integer collector_id;
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

    public Integer getCollector_id() {
        return collector_id;
    }

    public KeywordTag setCollector_id(Integer collector_id) {
        this.collector_id = collector_id;
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

    public String getApplication_name() {
        return application_name;
    }

    public KeywordTag setApplication_name(String application_name) {
        this.application_name = application_name;
        return this;
    }
}
