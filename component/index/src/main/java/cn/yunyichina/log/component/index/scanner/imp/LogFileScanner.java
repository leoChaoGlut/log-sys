package cn.yunyichina.log.component.index.scanner.imp;


import cn.yunyichina.log.component.index.constant.DirDepth;
import cn.yunyichina.log.component.index.scanner.Scanner;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/1 15:08
 * @Description:
 */
public class LogFileScanner implements Scanner<Map<String, File>> {

    private String rootDir;
    private String beginDatetime;
    private String endDatetime;

    private Map<String, File> fileMap = new TreeMap<>();

    /**
     * 闭区间
     *
     * @param beginDateTime yyyy-MM-dd HH:mm
     * @param endDateTime   yyyy-MM-dd HH:mm
     * @param rootDir       注意文件分隔符,\\ 和 / 的差异,会导致找不到文件.切记要使用File.separator
     */
    public LogFileScanner(String beginDateTime, String endDateTime, String rootDir) {
        this.beginDatetime = rootDir + File.separator + beginDateTime.replace("-", File.separator).replace(" ", File.separator).replace(":", File.separator);
        this.endDatetime = rootDir + File.separator + endDateTime.replace("-", File.separator).replace(" ", File.separator).replace(":", File.separator);
        this.rootDir = rootDir;
    }

    /**
     * @param beginLogFile yyyyMMddHHmm
     * @param endLogFile   yyyyMMddHHmm
     * @param rootDir
     */
    public LogFileScanner(File beginLogFile, File endLogFile, String rootDir) {
        String beginFileName = beginLogFile.getName().substring(0, beginLogFile.getName().lastIndexOf("."));
        String endFileName = endLogFile.getName().substring(0, endLogFile.getName().lastIndexOf("."));
        this.beginDatetime = rootDir + File.separator + beginFileName.substring(0, 4) + File.separator + beginFileName.substring(4, 6) + File.separator + beginFileName.substring(6, 8) + File.separator + beginFileName.substring(8, 10) + File.separator + beginFileName.substring(10, 12);
        this.endDatetime = rootDir + File.separator + endFileName.substring(0, 4) + File.separator + endFileName.substring(4, 6) + File.separator + endFileName.substring(6, 8) + File.separator + endFileName.substring(8, 10) + File.separator + endFileName.substring(10, 12);
        this.rootDir = rootDir;
    }

    @Override
    public Map<String, File> scan() {
        File rootDir = new File(this.rootDir);
        File[] files = rootDir.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.lastIndexOf(".log") != -1 || file.isDirectory()) {
                    dfs(file, 0);
                }
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
                if (fileName.endsWith(".log")) {
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));
                    fileMap.put(fileName, file);
                }
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