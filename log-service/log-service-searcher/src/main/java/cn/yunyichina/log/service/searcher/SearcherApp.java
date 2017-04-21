package cn.yunyichina.log.service.searcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/04/21 15:22
 * @Description:
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.service.searcher")
public class SearcherApp {

    public static void main(String[] args) {
        SpringApplication.run(SearcherApp.class, args);
    }

}
