package cn.yunyichina.log.service.redisproxy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 10:45
 * @Description:
 */
@EnableAsync
@EnableEurekaClient
@MapperScan(basePackages = "cn.yunyichina.log.service.redisproxy.mapper")
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.service.redisproxy")
public class RedisProxyApp {

    public static void main(String[] args) {
        SpringApplication.run(RedisProxyApp.class, args);
    }

}
