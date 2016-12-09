package cn.yunyichina.log.service.collectorNode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 10:45
 * @Description:
 */
@EnableWebMvc
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.service.collectorNode")
@EnableEurekaClient
@EnableScheduling
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
