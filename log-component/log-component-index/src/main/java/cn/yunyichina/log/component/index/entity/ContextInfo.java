package cn.yunyichina.log.component.index.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/27 18:02
 * @Description: 注意, begin和end是有可能为null的!!!!!!!!!!
 * 注意,begin和end是有可能为null的!!!!!!!!!!
 * 注意,begin和end是有可能为null的!!!!!!!!!!
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"contextId", "begin", "end"})
public class ContextInfo implements Serializable {
    private static final long serialVersionUID = -8753201735866113930L;

    private String contextId;
    private ContextIndex begin;
    private ContextIndex end;

}
