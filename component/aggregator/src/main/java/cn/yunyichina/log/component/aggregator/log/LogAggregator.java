package cn.yunyichina.log.component.aggregator.log;

import cn.yunyichina.log.common.constant.Constant;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.scanner.imp.LogFileScanner;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/17 10:56
 * @Description:
 */
public class LogAggregator {

    private int beginIndex;
    private int endIndex;
    private List<File> logList;
    private ContextIndexBuilder.ContextInfo contextInfo;

    public static String aggregate(ContextIndexBuilder.ContextInfo contextInfo) {
        LogAggregator aggregator = new LogAggregator(contextInfo);
        try {
            return aggregator.aggregate();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    protected LogAggregator(ContextIndexBuilder.ContextInfo contextInfo) {
        this.contextInfo = contextInfo;

        ContextIndexBuilder.IndexInfo begin = contextInfo.getBegin();
        ContextIndexBuilder.IndexInfo end = contextInfo.getEnd();

        this.beginIndex = begin.getIndexOfLogFile();
        this.endIndex = end.getIndexOfLogFile();

        LogFileScanner scanner = new LogFileScanner(begin.getLogFile(), end.getLogFile(), Constant.BASE_DIR);
        Map<String, File> fileMap = scanner.scan();
        logList = new ArrayList<>(fileMap.values());
    }

    protected String aggregate() throws Exception {
        if (logList.size() == 1) {
            File log = logList.get(0);
            String logContent = Files.asCharSource(log, Charsets.UTF_8).read();
            return logContent.substring(beginIndex, endIndex);
        } else if (logList.size() > 1) {
            StringBuilder logBuilder = new StringBuilder();
            int logListSize = logList.size();

            String firstLogConetnt = Files.asCharSource(logList.get(0), Charsets.UTF_8).read();
            logBuilder.append(firstLogConetnt.substring(beginIndex));

            for (int i = 1; i < logListSize - 2; i++) {
                logBuilder.append(Files.asCharSource(logList.get(i), Charsets.UTF_8).read());
            }

            String lastLogConetnt = Files.asCharSource(logList.get(logListSize - 1), Charsets.UTF_8).read();
            logBuilder.append(lastLogConetnt.substring(0, endIndex));

            return logBuilder.toString();
        } else {
            return "";
        }
    }

}
