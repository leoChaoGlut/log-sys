package cn.yunyichina.log.service.searcher.controller;

import cn.yunyichina.log.common.entity.entity.dto.ResponseDTO;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.service.searcher.service.UploadServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Jonven on 2016/11/22.
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    final LoggerWrapper logger = LoggerWrapper.getLogger(FileUploadController.class);

//    @Autowired
//    UploadServiceV1 uploadServiceV1;

    @Autowired
    UploadServiceV2 uploadServiceV2;
//
//    @PostMapping("/upload")
//    public ResponseDTO upload(MultipartFile zipFile) {
//        try {
//            logger.contextBegin("上传日志开始:" + zipFile.getName() + "," + zipFile.getSize());
//            if (zipFile != null && !zipFile.isEmpty()) {
//                uploadServiceV1.uploadFile(zipFile);
//                logger.contextEnd("上传日志成功");
//                return ResponseDTO.success("上传日志成功");
//            } else {
//                logger.contextEnd("上传文件失败: zip 文件为空");
//                return ResponseDTO.failure("上传文件失败: zip 文件为空");
//            }
//        } catch (Exception e) {
//            logger.contextEnd("上传文件出现异常:" + e.getLocalizedMessage());
//            return ResponseDTO.failure("上传文件出现异常:" + e.getLocalizedMessage());
//        }
//    }

    @PostMapping("/upload/v2")
    public ResponseDTO uploadV2(MultipartFile zipFile, String applicationName) {
        try {
            logger.contextBegin("上传日志开始:" + zipFile.getName() + "," + zipFile.getSize());
            if (zipFile != null && !zipFile.isEmpty()) {
                uploadServiceV2.uploadFile(zipFile, applicationName);
                logger.contextEnd("上传日志成功");
                return ResponseDTO.success("上传日志成功");
            } else {
                logger.contextEnd("上传文件失败: zip 文件为空");
                return ResponseDTO.failure("上传文件失败: zip 文件为空");
            }
        } catch (Exception e) {
            logger.contextEnd("上传文件出现异常:" + e.getLocalizedMessage());
            return ResponseDTO.failure("上传文件出现异常:" + e.getLocalizedMessage());
        }
    }
}
