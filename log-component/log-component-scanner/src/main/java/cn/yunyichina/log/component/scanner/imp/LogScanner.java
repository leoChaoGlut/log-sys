package cn.yunyichina.log.component.scanner.imp;


import cn.yunyichina.log.component.scanner.Scanner;
import cn.yunyichina.log.component.scanner.constant.DirDepth;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/1 15:08
 * @Description:
 */
public class LogScanner implements Scanner<Map<String, File>> {

    private final String LOG_SUFFIX = ".log";
    private final String DOT = ".";

    private String logDir;
    private String beginDatetime;
    private String endDatetime;

    /**
     * key: file name
     * value: log file
     */
    private Map<String, File> logMap = new TreeMap<>();

    public static Map<String, File> scan(String beginDatetime, String endDatetime, String logDir) {
        return new LogScanner(beginDatetime, endDatetime, logDir)
                .scan();
    }

    public static Map<String, File> scan(File beginLogFile, File endLogFile, String logDir) {
        return new LogScanner(beginLogFile, endLogFile, logDir)
                .scan();
    }


    /**
     * 闭区间
     *
     * @param beginDatetime yyyy-MM-dd HH:mm
     * @param endDatetime   yyyy-MM-dd HH:mm
     * @param logDir        如: "/var/log"      注意文件分隔符,\\ 和 / 的差异,会导致找不到文件.切记要使用File.separator
     */
    public LogScanner(String beginDatetime, String endDatetime, String logDir) {
        this.beginDatetime = logDir + File.separator +
                beginDatetime.replace("-", File.separator)
                        .replace(" ", File.separator)
                        .replace(":", File.separator);

        this.endDatetime = logDir + File.separator +
                endDatetime.replace("-", File.separator)
                        .replace(" ", File.separator)
                        .replace(":", File.separator);

        this.logDir = logDir;
    }

    /**
     * 闭区间
     *
     * @param beginLogFile
     * @param endLogFile
     * @param logDir
     */
    public LogScanner(File beginLogFile, File endLogFile, String logDir) {
        String beginFileName = beginLogFile.getName().substring(0, beginLogFile.getName().lastIndexOf(DOT));
        String endFileName = endLogFile.getName().substring(0, endLogFile.getName().lastIndexOf(DOT));

        this.beginDatetime = logDir + File.separator +
                beginFileName.substring(0, 4) + File.separator +
                beginFileName.substring(4, 6) + File.separator +
                beginFileName.substring(6, 8) + File.separator +
                beginFileName.substring(8, 10) + File.separator +
                beginFileName.substring(10, 12);

        this.endDatetime = logDir + File.separator +
                endFileName.substring(0, 4) + File.separator +
                endFileName.substring(4, 6) + File.separator +
                endFileName.substring(6, 8) + File.separator +
                endFileName.substring(8, 10) + File.separator +
                endFileName.substring(10, 12);

        this.logDir = logDir;
    }

    @Override
    public Map<String, File> scan() {
        File logDir = new File(this.logDir);
        File[] logs = logDir.listFiles();
        if (logs != null && logs.length > 0) {
            for (File log : logs) {
                if (log.getName().endsWith(LOG_SUFFIX) || log.isDirectory()) {
                    dfs(log, 0);
                }
            }
        }
        return logMap;
    }

    /**
     * dfs 搜索指定范围内的所有log
     *
     * @param log
     * @param depth
     */
    private void dfs(File log, int depth) {
        boolean needToPrune = prune(log, depth);
        if (!needToPrune) {
            if (log.isDirectory()) {
                File[] subLogs = log.listFiles();
                if (subLogs != null) {
                    for (File subLog : subLogs) {
                        dfs(subLog, depth + 1);
                    }
                }
            } else {
                String logName = log.getName();
                if (logName.endsWith(LOG_SUFFIX)) {
                    logName = logName.substring(0, logName.lastIndexOf(DOT));
                    logMap.put(logName, log);
                }
            }
        }
    }

    /**
     * 剪枝，只遍历指定时间范围内的目录
     *
     * @param log
     * @param depth
     * @return
     */
    private boolean prune(File log, int depth) {
        if (depth > DirDepth.MINUTE) {
            return false;
        } else {
            String logAbsPath = log.getAbsolutePath();
            int logAbsPathLength = logAbsPath.length();
            String beginDateTimePath = this.beginDatetime.substring(0, logAbsPathLength);
            String endDateTimePath = this.endDatetime.substring(0, logAbsPathLength);
            if (beginDateTimePath.compareTo(logAbsPath) <= 0 && logAbsPath.compareTo(endDateTimePath) <= 0) {
                return false;
            } else {
                return true;
            }
        }
    }


}