package cn.yunyichina.log.schedule.task;

import cn.yunyichina.log.schedule.annotation.Task;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 17:50
 * @Description:
 */
@Task
public class PushLogTask {

    @Scheduled(fixedRate = 5000)
    public void execute() {
    }

}
