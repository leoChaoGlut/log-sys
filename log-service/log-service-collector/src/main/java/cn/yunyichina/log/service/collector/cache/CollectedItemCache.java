package cn.yunyichina.log.service.collector.cache;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/4 18:13
 * @Description:
 */
@Getter
@Setter
public class CollectedItemCache implements Serializable {
    private static final long serialVersionUID = -5359229848606435316L;

    private int collectedItemId;
    private String lastModifyDatetimeStr;


}
