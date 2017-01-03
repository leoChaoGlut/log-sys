package cn.yunyichina.log.service.frontEnd.entity.do_.option;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/3 16:40
 * @Description:
 */
public class KeyValueDo {
    private String key;
    private String value;

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
