package cn.yunyichina.log.service.searcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 18:13
 * @Description:
 */
@EnableWebMvc
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.service.searcher")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
