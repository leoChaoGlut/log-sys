package cn.yunyichina.log.service.searcherNode.controller;

import cn.yunyichina.log.common.util.ZipUtil;
import com.google.common.io.Files;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Jonven on 2016/11/22.
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    private String uploadDir = "E:\\zTest\\uploads";

    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String zipFilePath = dir + File.separator + file.getName() + ".zip";
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(zipFilePath));
                stream.write(bytes);
                stream.flush();
//                Files.write();
                stream.close();
                handleZip(zipFilePath, uploadDir);
                return "upload success";
            } catch (Exception e) {
                e.printStackTrace();
                return "upload fail" + e.getMessage();
            }
        } else {
            return "file is empty";
        }
    }

    /**
     * 解压品并处理上传的文件
     */
    private void handleZip(String zipFilePath, String destDir) throws Exception {
        ZipUtil.unzip(zipFilePath, destDir);
        File file = new File(destDir);
        File[] files = file.listFiles();
        if (files != null) {
            for (File logFile : files) {
                if(logFile.isFile()) {//只处理文件、不处理文件夹
                    if(logFile.getName().contains(".log")){//只处理.log日志文件
                        String resultDir = destDir + File.separator + logFile.getName().substring(0, 4) +
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
                    }
                }
            }
        }
    }
}
