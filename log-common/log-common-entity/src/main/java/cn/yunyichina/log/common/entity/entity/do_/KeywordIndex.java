package cn.yunyichina.log.common.entity.entity.do_;

import java.util.Date;
import java.util.Objects;

/**
 * @Author: Leo
 * @Description: Reverse Engine By Leo
 */
public class KeywordIndex {
    private Integer id;
    private String keyword;
    private Date create_time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeywordIndex that = (KeywordIndex) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

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
