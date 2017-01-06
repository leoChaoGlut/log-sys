package cn.yunyichina.log.service.searcher.controller;

import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.component.entity.dto.Response;
import cn.yunyichina.log.service.searcher.service.UploadService;
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

    @Autowired
    UploadService uploadService;

    @PostMapping("/upload")
    public Response upload(MultipartFile zipFile) {
        try {
            logger.contextBegin("上传日志开始:" + zipFile.getName() + "," + zipFile.getSize());
            if (zipFile != null && !zipFile.isEmpty()) {
                uploadService.uploadFile(zipFile);
                logger.contextEnd("上传日志成功");
                return Response.success("上传日志成功");
            } else {
                logger.contextEnd("上传文件失败: zip 文件为空");
                return Response.failure("上传文件失败: zip 文件为空");
            }
        } catch (Exception e) {
            logger.contextEnd("上传文件出现异常:" + e.getLocalizedMessage());
            return Response.failure("上传文件出现异常:" + e.getLocalizedMessage());
        }
    }
}
