package cn.yunyichina.log.common.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.TreeSet;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/3 20:07
 * @Description:
 */
@ToString
@Getter
@Setter
public class LogResultDTO {

    private String contextContent;
    private TreeSet<String> logRegionSet;

}
