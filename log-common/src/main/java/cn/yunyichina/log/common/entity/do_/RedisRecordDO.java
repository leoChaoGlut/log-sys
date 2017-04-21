package cn.yunyichina.log.common.entity.do_;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/19 15:26
 * @Description:
 */
@Getter
@Setter
public class RedisRecordDO implements Serializable {
    private static final long serialVersionUID = 1710778108230539023L;

    private Integer id;
    private String ip;
    private Integer port;
    private String password;
    private Date createTime;

    //    extra
    private List<CollectorDO> collectorList;
    private Integer collectorId;
}
