package cn.yunyichina.log.common.entity.entity.po;

import java.util.Date;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/7 21:54
 * @Description:
 */
public class Collector {
    private String name;
    private String application_name;
    private String group;
    private Date create_time;

    public String getName() {
        return name;
    }

    public Collector setName(String name) {
        this.name = name;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public Collector setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getApplication_name() {
        return application_name;
    }

    public Collector setApplication_name(String application_name) {
        this.application_name = application_name;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public Collector setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }
}
