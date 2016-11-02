package cn.yy.log.constant;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/10/28 14:20
 * @Description:
 */
public enum CharSet {
    UTF8("UTF-8");

    private String value;

    CharSet(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
