package cn.yunyichina.log.service.frontEnd.entity.do_;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/4 11:32
 * @Description:
 */
public class KeyValueDo {
    private Integer id;
    private String key;
    private String value;

    public Integer getId() {
        return id;
    }

    public KeyValueDo setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public KeyValueDo setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public KeyValueDo setValue(String value) {
        this.value = value;
        return this;
    }
}
