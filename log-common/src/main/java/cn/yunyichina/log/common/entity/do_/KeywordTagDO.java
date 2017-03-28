package cn.yunyichina.log.common.entity.do_;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 10:05
 * @Description:
 */
@Getter
@Setter
public class KeywordTagDO implements Serializable {
    private static final long serialVersionUID = 261632408038467660L;
    private Integer id;
    private String keyword;

}
