package cn.yunyichina.log.service.searcherGateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 14:26
 * @Description:
 */
@EnableWebMvc
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.service.searcherGateway")
@MapperScan(basePackages = "cn.yunyichina.log.service.searcherGateway.mapper")
@EnableEurekaClient
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
