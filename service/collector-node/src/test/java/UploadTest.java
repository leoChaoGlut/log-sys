import cn.yunyichina.log.service.collectorNode.util.PropertiesFileUtil;
import cn.yunyichina.log.service.collectorNode.util.FileZipUpload;
import cn.yunyichina.log.service.collectorNode.util.MergeFiles;
import org.junit.Test;

import java.io.File;

/**
 * Created by Jonven on 2016/11/25.
 */
public class UploadTest {

    @Test
    public void upload(){

        String propertiesPath = "E:\\uploads\\files.properties";

        File[] files = new File[3];
        files[0] = new File("E:\\testLog1\\2016\\11\\15\\14\\21\\201611151421.log");
        files[1] = new File("E:\\testLog1\\2016\\11\\15\\14\\23\\201611151423.log");
        files[2] = new File("E:\\testLog1\\2016\\11\\15\\14\\25\\201611151425.log");

        String basePath = "E:\\testLog.zip";

        File[] failFiles =  new PropertiesFileUtil(propertiesPath).getFilesProperties();
        if (failFiles!=null){
            files = MergeFiles.merge(files,failFiles);
        }
        try {
            FileZipUpload.uploadFile(files,basePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
