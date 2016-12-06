package cn.yunyichina.log.service.searcherNode.controller;

import cn.yunyichina.log.common.entity.dto.Response;
import cn.yunyichina.log.service.searcherNode.service.UploadService;
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

    @Autowired
    UploadService uploadService;

    @PostMapping("/upload")
    public Response upload(MultipartFile zipFile) {
        System.err.println("==========upload============");
        try {
            if (zipFile != null && !zipFile.isEmpty()) {
                System.out.println(zipFile.getSize());
                uploadService.uploadFile(zipFile);
                return Response.success();
            } else {
                return Response.failure("The zipFile which to be uploaded is null or empty");
            }
        } catch (Exception e) {
            return Response.failure(e.getLocalizedMessage());
        }
    }
}
