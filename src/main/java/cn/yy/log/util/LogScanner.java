package cn.yy.log.util;

import cn.yy.log.entity.vo.DirDepth;
import cn.yy.log.entity.vo.LogPair;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/1 15:08
 * @Description:
 */
public class LogScanner {

    private final String FORMAT_LOG = ".log";
    private final String FORMAT_INDEX = ".index";

    private String basePath;
    private String beginDateTimePath;
    private String endDateTimePath;
    private Map<String, LogPair> logPairMap = new TreeMap<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public static Map<String, LogPair> scan(String beginDateTime, String endDateTime, String basePath) {
        return new LogScanner(beginDateTime, endDateTime, basePath).scan();
    }

    private LogScanner(String beginDateTime, String endDateTime, String basePath) {
        this.beginDateTimePath = basePath + "\\" + beginDateTime.replaceAll("-", "\\\\").replaceAll(" ", "\\\\").replaceAll(":", "\\\\");
        this.endDateTimePath = basePath + "\\" + endDateTime.replaceAll("-", "\\\\").replaceAll(" ", "\\\\").replaceAll(":", "\\\\");
        this.basePath = basePath;
//      int capacity = (int) (((sdf.parse(endDateTime).getTime() - sdf.parse(beginDateTime).getTime()) / (1000 * 60)) * 2);//1000 -> 毫秒, 60 -> 秒, 2 -> count(.index + .log)
    }

    private Map<String, LogPair> scan() {
        File baseDir = new File(basePath);
        File[] files = baseDir.listFiles();
        if (files != null) {
            for (File file : files) {
                backTracking(file, 0);
            }
        }
        return logPairMap;
    }

    private void backTracking(File file, int depth) {
        boolean needToPrune = prune(file, depth);
        if (!needToPrune) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        backTracking(subFile, depth + 1);
                    }
                }
            } else {
                String fileName = file.getName();
                int dotIndex = fileName.indexOf(".");
                String fileFormat = fileName.substring(dotIndex);
                fileName = fileName.substring(0, dotIndex);
                LogPair logPair = logPairMap.get(fileName);
                if (logPair == null) {
                    logPair = new LogPair();
                }
                switch (fileFormat) {
                    case FORMAT_LOG:
                        logPair.setLogFile(file);
                        break;
                    case FORMAT_INDEX:
                        logPair.setIndexFile(file);
                        break;
                    default:
                        break;
                }
                logPairMap.put(fileName, logPair);
            }
        }
    }

    private boolean prune(File file, int depth) {
        if (depth > DirDepth.MINUTE) {
            return false;
        } else {
            String dirPath = file.getAbsolutePath();
            int dirPathLength = dirPath.length();
            String beginDateTimePath = this.beginDateTimePath.substring(0, dirPathLength);
            String endDateTimePath = this.endDateTimePath.substring(0, dirPathLength);
            if (beginDateTimePath.compareTo(dirPath) <= 0 && dirPath.compareTo(endDateTimePath) <= 0) {
                return false;
            } else {
                return true;
            }
        }
    }

}
