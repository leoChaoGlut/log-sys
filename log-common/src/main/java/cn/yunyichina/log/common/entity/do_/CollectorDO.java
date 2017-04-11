package cn.yunyichina.log.common.entity.do_;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 10:02
 * @Description:
 */
@Getter
@Setter
public class CollectorDO implements Serializable {
    private static final long serialVersionUID = -8466348104442220979L;

    private Integer id;
    private String ip;
    private String port;
    private String name;
    private String remark;
    private String reachable;

    //    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date createTime;

    private String applicationName;
    //    extra
    private List<CollectedItemDO> collectedItemList;
}
