package cn.yunyichina.log.common.entity.po;

import java.util.Date;

/**
 * @Author: Leo
 * @Description: Reversal Script Written By Leo
 */
public class Collector {
    private Integer id;
    private String name;
    private Integer group_id;
    private Date create_time;
    private String service_name;

    public Integer getId() {
        return id;
    }

    public Collector setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Collector setName(String name) {
        this.name = name;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public Collector setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }

    public String getService_name() {
        return service_name;
    }

    public Collector setService_name(String service_name) {
        this.service_name = service_name;
        return this;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public Collector setGroup_id(Integer group_id) {
        this.group_id = group_id;
        return this;
    }
}
