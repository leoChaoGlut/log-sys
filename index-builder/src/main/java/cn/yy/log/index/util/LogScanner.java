package cn.yy.log.index.util;

import cn.yy.log.index.entity.vo.DirDepth;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/1 15:08
 * @Description:
 */
public class LogScanner {

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
    public LogScanner(String beginDateTime, String endDateTime, String baseDir) {
        this.beginDatetime = baseDir + File.separator + beginDateTime.replace("-", File.separator).replace(" ", File.separator).replace(":", File.separator);
        this.endDatetime = baseDir + File.separator + endDateTime.replace("-", File.separator).replace(" ", File.separator).replace(":", File.separator);
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
                int dotIndex = fileName.indexOf(".");
                fileName = fileName.substring(0, dotIndex);
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