package cn.yunyichina.log.schedule;

import cn.yunyichina.log.schedule.config.ScheduleTaskConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 17:57
 * @Description:
 */
public class Bootstrap {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ScheduleTaskConfig.class);
    }
}
