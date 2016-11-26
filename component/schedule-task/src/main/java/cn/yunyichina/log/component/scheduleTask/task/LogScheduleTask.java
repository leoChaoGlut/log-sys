package cn.yunyichina.log.component.scheduleTask.task;

import cn.yunyichina.log.common.util.MergeFiles;
import cn.yunyichina.log.common.util.PropertiesFileUtil;
import cn.yunyichina.log.common.util.UploadUtil;
import cn.yunyichina.log.component.aggregator.util.AggregatorUtil;
import cn.yunyichina.log.component.index.scanner.imp.LogFileScanner;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by Jonven on 2016/11/25.
 */
@Service
public class LogScheduleTask {

    //    @Scheduled(cron="0/5 * *  * * ? ")
    public void getLog() {
        String cursorProp = "E:\\zTest\\cursor.properties";
        String filesProp = "E:\\zTest\\files.properties";
        String logDir = "E:\\zTest\\testLog1";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String beginTime = new PropertiesFileUtil(cursorProp).getValue("last_end_time");
        Date endDate = new Date();
        String endTime = sdf.format(endDate);
        if (beginTime == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(calendar.HOUR, -1);
            beginTime = sdf.format(calendar.getTime());
        }

        LogFileScanner logFileScanner = new LogFileScanner(beginTime, endTime, logDir);
        Map<String, File> fileMap = logFileScanner.scan();

        if (!CollectionUtils.isEmpty(fileMap)) {
            Collection<File> files = fileMap.values();
            File[] logFiles = new File[files.size()];
            files.toArray(logFiles);
            //整合上次失败上传的文件,需要重传
            File[] failFiles =  new PropertiesFileUtil(filesProp).getFilesProperties();
            if (failFiles!=null){
                logFiles = MergeFiles.merge(logFiles,failFiles);
            }
            logFiles = MergeFiles.merge(logFiles, AggregatorUtil.getAllIndex(logFiles));

            if(logFiles != null){
                try {
                    if (UploadUtil.uploadFile(logFiles, "E:\\zTest\\testLog.zip")) {
                        new PropertiesFileUtil(filesProp).clearFilesProperties();
                        new PropertiesFileUtil(cursorProp).setValue("last_end_time",endTime);
                    } else {
                        new PropertiesFileUtil(filesProp).updateFilesProperties(logFiles);
                    }
                } catch (Exception e) {
                    new PropertiesFileUtil(filesProp).updateFilesProperties(logFiles);
                    e.printStackTrace();
                }
            }
        }
    }
}
