package cn.yunyichina.log.component.scheduleTask;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Jonven on 2016/11/25.
 */
@Configuration
@ComponentScan(basePackages = "cn.yunyichina.log.component.scheduleTask")
@EnableScheduling
public class Bootstrap {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Bootstrap.class);
    }
}
