package cn.yunyichina.log.service.common.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/27 10:55
 * @Description:
 */
@Getter
@Setter
public class ContextInfoDTO implements Serializable {
    private static final long serialVersionUID = 3371613342123699687L;

    private String contextId;
    private TreeSet<String> absLogFilePathSet;
    private int indexOfBeginFile;
    private int indexOfEndFile;


}
