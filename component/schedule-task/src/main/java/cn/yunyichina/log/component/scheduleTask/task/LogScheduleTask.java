package cn.yunyichina.log.component.scheduleTask.task;

import cn.yunyichina.log.service.collectorNode.util.PropertiesFileUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jonven on 2016/11/25.
 */
@Service
public class LogScheduleTask {

//    @Scheduled(cron="0/5 * *  * * ? ")
    public void getLog(){
        String propertiesPath = "E:\\uploads\\cursor.properties";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String beginTime = new PropertiesFileUtil(propertiesPath).getValue("last_end_time");

        Date endDate = new Date();
        String endTime = sdf.format(endDate);

        if(beginTime == null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(calendar.HOUR, -2);
            beginTime = sdf.format(calendar.getTime());
        }

        System.err.println(beginTime);
        System.err.println(endTime);

//        LogFileScanner logFileScanner = new LogFileScanner(beginTime, endTime, propertiesPath);
//        Map<String, File> fileMap = logFileScanner.scan();
//        Collection<File> files = fileMap.values();
    }

}
