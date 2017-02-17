package cn.yunyichina.log.common.entity.entity.do_;

import java.util.Date;

/**
 * @Author: Leo
 * @Description: Reversal Script Written By Leo
 */
public class StoreRecord {
    private Integer id;
    private Integer collector_id;
    private String name;
    private String image;
    private String outer_ip;
    private String inner_ip;
    private Date create_time;
    private Integer outer_port;
    private Integer inner_port;

    public Integer getId() {
        return id;
    }

    public StoreRecord setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCollector_id() {
        return collector_id;
    }

    public StoreRecord setCollector_id(Integer collector_id) {
        this.collector_id = collector_id;
        return this;
    }

    public String getName() {
        return name;
    }

    public StoreRecord setName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public StoreRecord setImage(String image) {
        this.image = image;
        return this;
    }

    public String getOuter_ip() {
        return outer_ip;
    }

    public StoreRecord setOuter_ip(String outer_ip) {
        this.outer_ip = outer_ip;
        return this;
    }

    public String getInner_ip() {
        return inner_ip;
    }

    public StoreRecord setInner_ip(String inner_ip) {
        this.inner_ip = inner_ip;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public StoreRecord setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }

    public Integer getOuter_port() {
        return outer_port;
    }

    public StoreRecord setOuter_port(Integer outer_port) {
        this.outer_port = outer_port;
        return this;
    }

    public Integer getInner_port() {
        return inner_port;
    }

    public StoreRecord setInner_port(Integer inner_port) {
        this.inner_port = inner_port;
        return this;
    }
}
