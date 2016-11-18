package cn.yunyichina.log.search.center.service;

import cn.yunyichina.log.common.entity.dto.SearchCondition;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/18 10:40
 * @Description:
 */
public interface SearchService {

    List<String> search(SearchCondition searchCondition) throws Exception;

}
