package cn.yunyichina.log.component.common.constant;

import lombok.Getter;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/25 8:55
 * @Description:
 */
public enum IndexType {
    CONTEXT("ctx"),
    KEYWORD("kw"),
    KEY_VALUE("kv");

    @Getter
    private String val;

    IndexType(String val) {
        this.val = val;
    }
}
