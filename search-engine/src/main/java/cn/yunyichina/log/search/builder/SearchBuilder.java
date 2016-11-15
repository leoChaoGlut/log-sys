package cn.yunyichina.log.search.builder;

import java.io.IOException;

/**
 * Created by Jonven on 2016/11/14.
 */
public interface SearchBuilder<T> {

    T builder() throws IOException;

}
