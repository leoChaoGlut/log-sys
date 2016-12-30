package cn.yunyichina.log.common.entity.po;

import java.util.Date;

/**
 * @Author: Leo
 * @Description: Reverse Engine By Leo
 */
public class MidCollectorKv {
    private Integer id;
    private Integer collector_id;
    private Integer kv_id;
    private Date create_time;

    public Integer getId() {
        return id;
    }

    public MidCollectorKv setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCollector_id() {
        return collector_id;
    }

    public MidCollectorKv setCollector_id(Integer collector_id) {
        this.collector_id = collector_id;
        return this;
    }

    public Integer getKv_id() {
        return kv_id;
    }

    public MidCollectorKv setKv_id(Integer kv_id) {
        this.kv_id = kv_id;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public MidCollectorKv setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }
}
