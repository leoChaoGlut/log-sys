package cn.yunyichina.log.schedule.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 18:00
 * @Description:
 */
@Configuration
@ComponentScan("cn.yunyichina.log.schedule.task")
@EnableScheduling
public class ScheduleTaskConfig {
}
