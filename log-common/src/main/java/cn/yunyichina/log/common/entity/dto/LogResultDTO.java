package cn.yunyichina.log.common.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/3 20:07
 * @Description:
 */
@Getter
@Setter
@Deprecated
public class LogResultDTO implements Serializable {
    private static final long serialVersionUID = 4673943686711709115L;

    private String contextContent;
    private TreeSet<String> logRegionSet;
    private String contextId;

}
