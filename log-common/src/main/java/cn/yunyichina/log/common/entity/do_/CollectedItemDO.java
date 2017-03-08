package cn.yunyichina.log.common.entity.do_;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 10:03
 * @Description:
 */
@Getter
@Setter
@ToString
public class CollectedItemDO implements Serializable {
    private static final long serialVersionUID = 685370116673816671L;
    private Integer id;
    private String name;
    private Integer collectorId;
    private String collectedLogDir;

    //    extra
    private List<KeywordTagDO> keywordTagList;
    private List<KvTagDO> kvTagList;

    private String stdoutFilePath;

}
