package cn.yunyichina.log.service.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 10:45
 * @Description:
 */
@EnableAsync
@EnableScheduling
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.service.collector")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
