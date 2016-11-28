package cn.yunyichina.log.service.searcherNode.service;

import cn.yunyichina.log.common.util.ZipUtil;
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by Jonven on 2016/11/28.
 */
@Service
public class UploadService {

    @Value("${constants.upload.logSuffix}")
    private String LOG_SUFFIX;
    @Value("${constants.upload.indexSuffix}")
    private String INDEX_SUFFIX;
    @Value("${constants.upload.zipIndex}")
    private String ZIP_SUFFIX;
    @Value("${constants.upload.logRootDir}")
    private String LOG_ROOT_DIR;
    @Value("${constants.upload.indexRootDir}")
    private String INDEX_ROOT_DIR;

    public void uploadFile(MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        File dir = new File(LOG_ROOT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String zipFilePath = dir + File.separator + file.getName() + ZIP_SUFFIX;
        Files.write(bytes, new File(zipFilePath));
        rebuildLogDirStructure(zipFilePath);
    }

    private void rebuildLogDirStructure(String zipFilePath) throws Exception {
        ZipUtil.unzip(zipFilePath, LOG_ROOT_DIR);
        File[] logFiles = new File(LOG_ROOT_DIR).listFiles();
        if (logFiles != null) {
            for (File file : logFiles) {
                if (file.isFile()) {
                    if (file.getName().endsWith(LOG_SUFFIX)) {
                        String copiedLogFilePath = LOG_ROOT_DIR + File.separator + file.getName().substring(0, 4) +
                                File.separator + file.getName().substring(4, 6) +
                                File.separator + file.getName().substring(6, 8) +
                                File.separator + file.getName().substring(8, 10) +
                                File.separator + file.getName().substring(10, 12) +
                                File.separator + file.getName();

                        File copiedLogFile = new File(copiedLogFilePath);
                        if (copiedLogFile.exists()) {
                            file.delete();
                        } else {
                            Files.createParentDirs(copiedLogFile);
                            Files.move(file, copiedLogFile);
                        }
                    } else if (file.getName().endsWith(INDEX_SUFFIX)) {
                        File copiedIndexFile = new File(INDEX_ROOT_DIR + file.getName());
//                        TODO 硬盘索引备份,硬盘索引增量(先备份,后替换,且必须保留上一个文件的备份),内存索引增量
//                        if (copiedIndexFile.exists()) {
//
//                        } else {
//                            Files.createParentDirs(copiedIndexFile);
//                            Files.move(file, copiedIndexFile);
//                        }
                    }
                } else {

                }
            }
        }
    }

}
