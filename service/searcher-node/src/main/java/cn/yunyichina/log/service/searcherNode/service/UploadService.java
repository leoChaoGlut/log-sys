package cn.yunyichina.log.service.searcherNode.service;

import cn.yunyichina.log.service.searcherNode.util.HandleZip;
import com.google.common.io.Files;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by Jonven on 2016/11/28.
 */
@Service
public class UploadService {

    private String ZIP_SUFFIX = ".zip";
    private String UPLOAD_LOG_DIR = "E:\\zTest\\uploads\\log\\";
    private String UPLOAD_INDEX_DIR = "E:\\zTest\\uploads\\index\\";

    public String uploadFile(MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        File dir = new File(UPLOAD_LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String zipFilePath = dir + File.separator + file.getName() + ZIP_SUFFIX;
//        BufferedOutputStream stream = new BufferedOutputStream(
//                new FileOutputStream(zipFilePath));
//        stream.write(bytes);
//        stream.flush();
        Files.write(bytes, new File(zipFilePath));
        HandleZip.getFolder(zipFilePath, UPLOAD_LOG_DIR, UPLOAD_INDEX_DIR);
        return "success";
    }
}
