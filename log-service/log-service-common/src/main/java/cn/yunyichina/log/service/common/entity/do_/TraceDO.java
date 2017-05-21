package cn.yunyichina.log.service.common.entity.do_;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/21 23:20
 * @Description:
 */
@Getter
@Setter
public class TraceDO {
    private String traceId;
    private List<String> contextList;
    //
    private String contextId;
    private String tableName;
}
