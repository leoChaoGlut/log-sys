package cn.yunyichina.log.component.entity.po;

import java.util.Date;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 9:45
 * @Description:
 */
public class Group {
    private Integer id;
    private String name;
    private Date create_time;

    public Integer getId() {
        return id;
    }

    public Group setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Group setName(String name) {
        this.name = name;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public Group setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }
}
