import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder.KvTag;

/**
 * Created by Jonven on 2016/12/5.
 */
public class MyTest {

    @Test
    public void testCache(){
        Map<String,Object> cacheMap = new HashedMap();
        cacheMap.put("lastModifyTime","2016-10-29 16:27");
        Set<File> fileSet = new HashSet<>();
        fileSet.add(new File("E:\\zTest\\testLog1\\2016\\11\\15\\14\\21\\201611151421.log"));
        fileSet.add(new File("E:\\zTest\\testLog1\\2016\\11\\15\\14\\23\\201611151423.log"));
        fileSet.add(new File("E:\\zTest\\testLog1\\2016\\11\\15\\14\\25\\201611151425.log"));
        cacheMap.put("uploadFailedFileList",fileSet);

        Set<String> keywordSet = new HashSet<>();
        keywordSet.add("<branchCode>");
        keywordSet.add("patName");
        keywordSet.add("pat_mobile");

        Set<KvTag> kvTagSet = new HashSet<>();
        kvTagSet.add(new KvTag("patCardNo", "<patCardNo>", "</patCardNo>"));
        kvTagSet.add(new KvTag("patCardNo", "<patCardNo>", "</patCardNo>"));

        cacheMap.put("kvTagSet",kvTagSet);
        cacheMap.put("keywordSet",keywordSet);
        try {
//            Files.write(JSON.toJSONBytes(cacheMap),new File("E:\\zTest\\cache.cache"));
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("E:\\zTest\\cache.cache")));
            oos.writeObject(cacheMap);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
