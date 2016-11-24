package cn.yunyichina.log.collertor.node.test;


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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jonven on 2016/11/21.
 */
public class FileUploadClient {

    private static String uploadRrl = "http://localhost:10500/file/upload";

    public static void main(String args[]) {
        try {
            File[] files = new File[3];
            files[0] = new File("E:\\testLog1\\2016\\11\\15\\14\\21\\201611151421.log");
            files[1] = new File("E:\\testLog1\\2016\\11\\15\\14\\23\\201611151423.log");
            files[2] = new File("E:\\testLog1\\2016\\11\\15\\14\\25\\201611151425.log");
            uploadFile(files,"E:\\testLog.zip");
        } catch (Exception ex) {
            Logger.getLogger(FileUploadClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void uploadFile(File[] files,String basePath) throws Exception {

        Zip.zip(files, basePath);
        File file = new File(basePath);

        if (!file.exists()) {
            throw new Exception("文件不存在！");
        }

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(uploadRrl);

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
