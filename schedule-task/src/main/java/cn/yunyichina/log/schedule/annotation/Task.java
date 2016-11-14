package cn.yunyichina.log.schedule.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 17:52
 * @Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Task {
}
