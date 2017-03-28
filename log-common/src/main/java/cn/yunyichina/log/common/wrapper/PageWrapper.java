package cn.yunyichina.log.common.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/27 11:22
 * @Description:
 */
@Getter
@Setter
public class PageWrapper<T> {
    private long count;
    private List<T> list;
}
