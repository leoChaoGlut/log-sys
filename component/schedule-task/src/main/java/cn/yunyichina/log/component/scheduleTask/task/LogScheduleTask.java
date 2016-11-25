package cn.yunyichina.log.component.scheduleTask.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Jonven on 2016/11/25.
 */
@Service
public class LogScheduleTask {

    //    @Scheduled(cron = "0/5 * *  * * ? ")
    @Scheduled(fixedRate = 1000)
    public void execute() {
        System.out.println(System.currentTimeMillis());
    }

}
