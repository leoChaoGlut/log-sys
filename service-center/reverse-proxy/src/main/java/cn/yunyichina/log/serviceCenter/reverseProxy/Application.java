package cn.yunyichina.log.serviceCenter.reverseProxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/23 14:37
 * @Description:
 */
@EnableZuulProxy
@SpringBootApplication
//@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.serviceCenter.reverseProxy")
@EnableEurekaClient
public class Application {

//    @Bean
//    public RouteFilter routeFilter() {
//        return new RouteFilter();
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
