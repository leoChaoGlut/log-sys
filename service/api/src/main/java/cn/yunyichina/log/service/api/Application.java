package cn.yunyichina.log.service.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/1/3 16:00
 * @Description:
 */
@EnableWebMvc
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.service.api")
//@EnableEurekaClient
@MapperScan(basePackages = "cn.yunyichina.log.service.api.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
