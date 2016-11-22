package cn.yunyichina.log.collection;

import cn.yunyichina.log.collection.config.SpringConfig;
import org.springframework.boot.SpringApplication;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 18:13
 * @Description:
 */

public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfig.class, args);
    }
}
