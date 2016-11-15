package cn.yunyichina.log.index.util;

import cn.yunyichina.log.index.constant.DirDepth;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/1 15:08
 * @Description:
 */
public class LogFileScanner {

    private String baseDir;
    private String beginDatetime;
    private String endDatetime;

    private Map<String, File> fileMap = new TreeMap<>();

    /**
     * 闭区间
     *
     * @param beginDateTime yyyy-MM-dd hh:mm
     * @param endDateTime   yyyy-MM-dd hh:mm
     * @param baseDir       注意文件分隔符,\\ 和 / 的差异,会导致找不到文件.切记要使用File.separator
     */
    public LogFileScanner(String beginDateTime, String endDateTime, String baseDir) {
        this.beginDatetime = baseDir + File.separator + beginDateTime.replace("-", File.separator).replace(" ", File.separator).replace(":", File.separator);
        this.endDatetime = baseDir + File.separator + endDateTime.replace("-", File.separator).replace(" ", File.separator).replace(":", File.separator);
        this.baseDir = baseDir;
    }

    /**
     * @param beginLogFile
     * @param endLogFile
     * @param baseDir
     */
    public LogFileScanner(File beginLogFile, File endLogFile, String baseDir) {
        String beginFileName = beginLogFile.getName().substring(0, beginLogFile.getName().lastIndexOf("."));
        String endFileName = endLogFile.getName().substring(0, endLogFile.getName().lastIndexOf("."));
        this.beginDatetime = baseDir + File.separator + beginFileName.substring(0, 4) + File.separator + beginFileName.substring(4, 6) + File.separator + beginFileName.substring(6, 8) + File.separator + beginFileName.substring(8, 10) + File.separator + beginFileName.substring(10, 12);
        this.endDatetime = baseDir + File.separator + endFileName.substring(0, 4) + File.separator + endFileName.substring(4, 6) + File.separator + endFileName.substring(6, 8) + File.separator + endFileName.substring(8, 10) + File.separator + endFileName.substring(10, 12);
        this.baseDir = baseDir;
    }

    public Map<String, File> scan() {
        File baseDir = new File(this.baseDir);
        File[] files = baseDir.listFiles();
        if (files != null) {
            for (File file : files) {
                dfs(file, 0);
            }
        }
        return fileMap;
    }

    private void dfs(File file, int depth) {
        boolean needToPrune = prune(file, depth);
        if (!needToPrune) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        dfs(subFile, depth + 1);
                    }
                }
            } else {
                String fileName = file.getName();
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
                fileMap.put(fileName, file);
            }
        }
    }

    private boolean prune(File file, int depth) {
        if (depth > DirDepth.MINUTE) {
            return false;
        } else {
            String dirPath = file.getAbsolutePath();
            int dirPathLength = dirPath.length();
            String beginDateTimePath = this.beginDatetime.substring(0, dirPathLength);
            String endDateTimePath = this.endDatetime.substring(0, dirPathLength);
            if (beginDateTimePath.compareTo(dirPath) <= 0 && dirPath.compareTo(endDateTimePath) <= 0) {
                return false;
            } else {
                return true;
            }
        }
    }


}