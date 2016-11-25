package cn.yunyichina.log.component.scheduleTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Jonven on 2016/11/25.
 */
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.component.scheduleTask")
public class Bootstrap {
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class,args);
    }
}
