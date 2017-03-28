package cn.yunyichina.log.common.base;

import cn.yunyichina.log.common.wrapper.PageWrapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/28 10:08
 * @Description:
 */
public interface BaseMapper<T> {

    T selectOne(T t);

    List<T> selectAll();

    List<T> selectList(T t);

    PageWrapper<T> selectPaging(@Param("item") T item, @Param("beginIndex") int beginIndex, @Param("pageSize") int pageSize);

    int insertOne(T t);

    int insertList(List<T> tList);

    int update(T t);

    int delete(T t);

    int count(T t);

}
