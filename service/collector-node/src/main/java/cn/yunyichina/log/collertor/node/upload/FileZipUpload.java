package cn.yunyichina.log.collertor.node.upload;


import cn.yunyichina.log.collertor.node.constant.UrlConstant;
import cn.yunyichina.log.common.util.Zip;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jonven on 2016/11/21.
 */
public class FileZipUpload {

    public static void uploadFile(File[] files, String basePath) throws Exception {

        Zip.zip(files, basePath);
        File file = new File(basePath);

        if (!file.exists()) {
            throw new Exception("文件不存在！");
        }

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(UrlConstant.UPLAOD_URL);

        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("file", file).build();

        httppost.setEntity(httpEntity);

        System.out.println("executing request " + httppost.getRequestLine());

        try {
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();

                System.out.println(EntityUtils.toString(resEntity));
                System.out.println(resEntity.getContent());

                EntityUtils.consume(resEntity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
