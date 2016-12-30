package cn.yunyichina.log.common.entity.po;

import java.util.Date;

/**
 * @Author: Leo
 * @Description: Reverse Engine By Leo
 */
public class MidCollectorKeyword {
    private Integer id;
    private Integer collector_id;
    private Integer keyword_id;
    private Date create_time;

    public Integer getId() {
        return id;
    }

    public MidCollectorKeyword setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCollector_id() {
        return collector_id;
    }

    public MidCollectorKeyword setCollector_id(Integer collector_id) {
        this.collector_id = collector_id;
        return this;
    }

    public Integer getKeyword_id() {
        return keyword_id;
    }

    public MidCollectorKeyword setKeyword_id(Integer keyword_id) {
        this.keyword_id = keyword_id;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public MidCollectorKeyword setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }
}
