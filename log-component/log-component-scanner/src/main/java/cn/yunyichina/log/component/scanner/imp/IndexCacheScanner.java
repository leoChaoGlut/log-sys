package cn.yunyichina.log.component.scanner.imp;

import cn.yunyichina.log.component.common.constant.IndexFormat;
import cn.yunyichina.log.component.scanner.Scanner;
import cn.yunyichina.log.component.scanner.constant.DirDepth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/24 17:14
 * @Description:
 */
public class IndexCacheScanner implements Scanner<List<File>> {
    private static final Logger logger = LoggerFactory.getLogger(IndexCacheScanner.class);

    private final String DOT = ".";
    private final int LENGTH_OF_yyyyMMddHHmm = "yyyyMMddHHmm".length();

    private String logDir;
    private String beginDatetime;
    private String endDatetime;
    private String indexFormat;
    /**
     * key: file name
     * value: log file
     */
    private List<File> indexFileList = new ArrayList<>();

    public static List<File> scan(String beginDatetimeStr, String endDatetimeStr, String indexDir, IndexFormat indexFormat) {
        long begin = System.nanoTime();
        try {
            logger.info("搜索索引缓存开始:" + beginDatetimeStr + " - " + endDatetimeStr);
            return new IndexCacheScanner(beginDatetimeStr, endDatetimeStr, indexDir, indexFormat.getVal())
                    .scan();
        } catch (Exception e) {
            logger.error("ContextInfoScanner扫描期间异常", e);
            return new ArrayList<>();
        } finally {
            logger.info("搜索索引缓存结束,耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9) + "秒");
        }
    }

    /**
     * 闭区间
     *
     * @param beginDatetimeStr yyyy-MM-dd HH:mm
     * @param endDatetimeStr   yyyy-MM-dd HH:mm
     * @param logDir           如: "/var/log"      注意文件分隔符,\\ 和 / 的差异,会导致找不到文件.切记要使用File.separator
     * @param indexFormat
     */
    protected IndexCacheScanner(String beginDatetimeStr, String endDatetimeStr, String logDir, String indexFormat) {
        this.beginDatetime = logDir + File.separator +
                beginDatetimeStr.replace("-", File.separator)
                        .replace(" ", File.separator)
                        .replace(":", File.separator);

        this.endDatetime = logDir + File.separator +
                endDatetimeStr.replace("-", File.separator)
                        .replace(" ", File.separator)
                        .replace(":", File.separator);

        this.logDir = logDir;
        this.indexFormat = indexFormat;
    }

    @Override
    public List<File> scan() {
        File logDir = new File(this.logDir);
        File[] logs = logDir.listFiles();
        if (logs != null && logs.length > 0) {
            for (File log : logs) {
                if (logNameValidated(log)) {
                    dfs(log, 0);
                }
            }
        }
        return indexFileList;
    }

    private boolean logNameValidated(File log) {
        if (log.getName().length() == LENGTH_OF_yyyyMMddHHmm + indexFormat.length() && log.getName().endsWith(indexFormat)) {
            return true;
        } else if (log.isDirectory()) {
            return true;
        } else {
            return false;
        }
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
                if (log.getName().endsWith(indexFormat)) {
                    indexFileList.add(log);
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
            if (this.beginDatetime.length() < logAbsPathLength || this.endDatetime.length() < logAbsPathLength) {
                //防止空指针
                return false;
            } else {
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

}
