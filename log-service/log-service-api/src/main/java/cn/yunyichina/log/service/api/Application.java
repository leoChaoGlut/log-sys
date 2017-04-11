package cn.yunyichina.log.service.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 10:45
 * @Description:
 */
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.service.api")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
