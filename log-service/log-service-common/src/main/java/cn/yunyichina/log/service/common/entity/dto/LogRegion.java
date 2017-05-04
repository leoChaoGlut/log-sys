package cn.yunyichina.log.service.common.entity.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/27 11:58
 * @Description:
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"datetimeStr"})
public class LogRegion implements Serializable {
    private static final long serialVersionUID = -83050698219286068L;

    /**
     * yyyy-MM-dd HH:mm
     */
    private String datetimeStr;
    private String absFilePath;
}
