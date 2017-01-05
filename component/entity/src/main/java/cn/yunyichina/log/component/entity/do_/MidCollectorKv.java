package cn.yunyichina.log.component.entity.do_;

import java.util.Date;

/**
 * Created by Jonven on 2017/1/4.
 */
public class MidCollectorKv {

    private Integer id;
    private Integer collector_id;
    private Integer kv_id;
    private Date create_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCollector_id() {
        return collector_id;
    }

    public void setCollector_id(Integer collector_id) {
        this.collector_id = collector_id;
    }

    public Integer getKv_id() {
        return kv_id;
    }

    public void setKv_id(Integer kv_id) {
        this.kv_id = kv_id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
