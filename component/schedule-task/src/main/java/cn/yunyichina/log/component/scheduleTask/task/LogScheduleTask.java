package cn.yunyichina.log.component.scheduleTask.task;

import cn.yunyichina.log.common.util.MergeFiles;
import cn.yunyichina.log.common.util.PropertiesFileUtil;
import cn.yunyichina.log.common.util.UploadUtil;
import cn.yunyichina.log.component.aggregator.util.AggregatorUtil;
import cn.yunyichina.log.component.index.scanner.imp.LogFileScanner;
import org.springframework.scheduling.annotation.Scheduled;
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

    // TODO: 2016/11/26 测试定时为为5秒
    @Scheduled(cron = "0/5 * *  * * ? ")
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
            // TODO: 2016/11/26 暂时定义为获取前一个小时
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
            File[] failFiles = new PropertiesFileUtil(filesProp).getFilesProperties();
            if (failFiles != null) {
                logFiles = MergeFiles.merge(logFiles, failFiles);
            }
            logFiles = MergeFiles.merge(logFiles, AggregatorUtil.getAllIndex(logFiles));

            if (logFiles != null) {
                try {
                    if (UploadUtil.uploadFile(logFiles, "E:\\zTest\\testLog.zip")) {//上传成功
                        //清空记录上传失败日志的properties文件
                        new PropertiesFileUtil(filesProp).clearFilesProperties();
                        //更新上传的时间游标
                        new PropertiesFileUtil(cursorProp).setValue("last_end_time", endTime);
                    } else {//上传失败
                        //记录上传失败的日志到properties文件
                        new PropertiesFileUtil(filesProp).updateFilesProperties(logFiles);
                    }
                } catch (Exception e) {
                    //有异常、记录上传失败的日志到properties文件
                    new PropertiesFileUtil(filesProp).updateFilesProperties(logFiles);
                    e.printStackTrace();
                }
            }
        }
    }
}
