package cn.yunyichina.log.service.common.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/27 11:06
 * @Description:
 */
@Getter
@Setter
public class LogResultDTO implements Serializable {
    private static final long serialVersionUID = 1033038504188556196L;

    private String contextId;
    private TreeSet<String> logRegionSet;
    private ContextInfoDTO contextInfoDTO;

}
