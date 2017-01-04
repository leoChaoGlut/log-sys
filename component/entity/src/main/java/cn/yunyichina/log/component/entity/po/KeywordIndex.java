package cn.yunyichina.log.component.entity.po;

import java.util.Date;

/**
 * @Author: Leo
 * @Description: Reverse Engine By Leo
 */
public class KeywordIndex {
    private Integer id;
    private String keyword;
    private Date create_time;

    public Integer getId() {
        return id;
    }

    public KeywordIndex setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public KeywordIndex setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public KeywordIndex setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }
}
