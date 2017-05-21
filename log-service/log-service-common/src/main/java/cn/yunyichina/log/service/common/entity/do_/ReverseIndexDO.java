package cn.yunyichina.log.service.common.entity.do_;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/21 22:58
 * @Description:
 */
@Getter
@Setter
public class ReverseIndexDO {
    private String contextId;
    private String traceId;
    //
    private String tableName;
}
