package cn.yunyichina.log.component.entity.do_;

import java.util.Date;
import java.util.Set;

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

    //    extra
    private Set<Collector> collectorSet;
    private Collector collector;

    public Collector getCollector() {
        return collector;
    }

    public Group setCollector(Collector collector) {
        this.collector = collector;
        return this;
    }

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

    public Set<Collector> getCollectorSet() {
        return collectorSet;
    }

    public Group setCollectorSet(Set<Collector> collectorSet) {
        this.collectorSet = collectorSet;
        return this;
    }
}
