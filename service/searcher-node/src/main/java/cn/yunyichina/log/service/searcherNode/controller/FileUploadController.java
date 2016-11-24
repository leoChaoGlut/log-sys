package cn.yunyichina.log.service.searcherNode.controller;

import cn.yunyichina.log.common.util.UnZip;
import com.google.common.io.Files;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jonven on 2016/11/22.
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                File dir = new File("E:\\uploads");
                if (!dir.exists()) {
                    return "文件不存在";
                }
                String zipFilePath = dir + File.separator + file.getName() + ".zip";
                String destDir = "E:\\深三";
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(zipFilePath));
                stream.write(bytes);
                stream.flush();
//                Files.write();
                stream.close();
                handleZip(zipFilePath, destDir);
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "fail" + e.getMessage();
            }
        } else {
            return "file is empty";
        }
    }

    /**
     * 解压品并处理上传的文件
     */
    private void handleZip(String zipFilePath, String destDir) throws IOException {
        UnZip.unzip(zipFilePath, destDir);
        File file = new File(destDir);
        if (!file.exists()) {
            return;
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File logFile : files) {
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
                }
            }
        }
    }
}
