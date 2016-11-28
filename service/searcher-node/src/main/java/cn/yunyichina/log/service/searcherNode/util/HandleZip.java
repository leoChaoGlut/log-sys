package cn.yunyichina.log.service.searcherNode.util;

import cn.yunyichina.log.common.util.ZipUtil;
import com.google.common.io.Files;

import java.io.File;

/**
 * Created by Jonven on 2016/11/28.
 */
public class HandleZip {

    private static String LOG_SUFFIX = ".log";

    /**
     * 解压品并处理上传的文件
     */
    public static void getFolder(String zipFilePath, String logDir,String indexDir) throws Exception {
        ZipUtil.unzip(zipFilePath, logDir);
        File file = new File(logDir);
        File[] files = file.listFiles();
        if (files != null) {
            for (File logFile : files) {
                if (logFile.isFile()) {//只处理文件、不处理文件夹
                    if (logFile.getName().contains(LOG_SUFFIX)) {//只处理.log日志文件

                        String resultDir = logDir + File.separator + logFile.getName().substring(0, 4) +
                                File.separator + logFile.getName().substring(4, 6) +
                                File.separator + logFile.getName().substring(6, 8) +
                                File.separator + logFile.getName().substring(8, 10) +
                                File.separator + logFile.getName().substring(10, 12) +
                                File.separator + logFile.getName();

                        File resultFile = new File(resultDir);
                        if (!resultFile.exists()) {
                            Files.createParentDirs(resultFile);
                            Files.move(logFile, resultFile);
                        } else {
                            logFile.delete();
                        }
                    }else{
                        File indeFile = new File(indexDir + logFile.getName());
                        if (!indeFile.exists()) {
                            Files.createParentDirs(indeFile);
                        }
                        Files.move(logFile, indeFile);
                    }
                }
            }
        }
    }
}
