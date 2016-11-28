package cn.yunyichina.log.common.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;

/**
 * Created by Jonven on 2016/11/21.
 */
public class UploadUtil {

    /**
     * 上传文件(单文件)
     * @param files 文件数组
     * @param basePath 上传的文件路径
     * @return
     * @throws Exception
     */
    public static Boolean uploadFile(File[] files, String basePath) throws Exception {

        ZipUtil.zip(basePath,files);
        File file = new File(basePath);

        if (!file.exists()) {
            throw new Exception("压缩文件集合失败！");
        }

        CloseableHttpClient httpsClient = NetworkUtil.createHttpsClient();
//      TODO URL改为Https
        HttpPost post = new HttpPost("http://localhost:10401/file/upload");
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("file", file).build();
        post.setEntity(httpEntity);
        CloseableHttpResponse response = null;
        try {
            response = httpsClient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//              TODO 文件传输成功
                new File(basePath).delete();
                System.err.println("文件传输成功");
                return true;
            } else {
//              TODO 文件传输失败
                System.err.println("文件传输失败");
                return false;
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }
}
