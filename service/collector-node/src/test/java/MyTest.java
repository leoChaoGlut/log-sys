import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.service.collectorNode.util.PropertiesUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


/**
 * Created by Jonven on 2016/12/5.
 */
public class MyTest {

    @Test
    public void testCache() throws IOException {
        Map<String, Object> cacheMap = new HashedMap();
        cacheMap.put("lastModifyTime", "2016-10-29 16:27");
        Set<File> fileSet = new HashSet<>();
        fileSet.add(new File("E:\\zTest\\testLog1\\2016\\11\\15\\14\\21\\201611151421.log"));
        fileSet.add(new File("E:\\zTest\\testLog1\\2016\\11\\15\\14\\23\\201611151423.log"));
        fileSet.add(new File("E:\\zTest\\testLog1\\2016\\11\\15\\14\\25\\201611151425.log"));
        cacheMap.put("uploadFailedFileList", fileSet);

        Set<String> keywordSet = new HashSet<>();
        keywordSet.add("<branchCode>");
        keywordSet.add("patName");
        keywordSet.add("pat_mobile");

        Set<KeyValueIndexBuilder.KvTag> kvTagSet = new HashSet<>();
        kvTagSet.add(new KeyValueIndexBuilder.KvTag("patCardNo", "<patCardNo>", "</patCardNo>"));
        kvTagSet.add(new KeyValueIndexBuilder.KvTag("patCardNo1", "<patCardNo>1", "</patCardNo>1"));

        cacheMap.put("kvTagSet", kvTagSet);
        cacheMap.put("keywordSet", keywordSet);
//        String json = JSON.toJSONString(cacheMap);
//        Files.write(json, new File("D:\\cache.cache"), Charsets.UTF_8);


        try {
//            Files.write(JSON.toJSONBytes(cacheMap),new File("E:\\zTest\\cache.cache"));
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("D:\\cache.cache")));
            System.out.println(JSON.toJSONString(cacheMap, true));
            oos.writeObject(cacheMap);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("D:\\cache.cache")));
        Map<String, Object> cacheMap = (Map<String, Object>) ois.readObject();
        Set<KeyValueIndexBuilder.KvTag> kvTagSet = (Set<KeyValueIndexBuilder.KvTag>) cacheMap.get("kvTagSet");
        for (KeyValueIndexBuilder.KvTag kvTag : kvTagSet) {
            System.out.println(JSON.toJSONString(kvTag, true));
        }
    }

    @Test
    public void test3() throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream("D:\\1.properties"));
        Set<Map.Entry<Object, Object>> entrySet = prop.entrySet();
        Map<Object, Object> storeMap = null;
        if (CollectionUtils.isEmpty(entrySet)) {
            storeMap = new HashedMap();
        } else {
            storeMap = new HashedMap(entrySet.size() << 1);
            for (Map.Entry<Object, Object> entry : entrySet) {
                storeMap.put(entry.getKey(), entry.getValue());
            }
        }
        storeMap.put("name", "leo");
        prop.putAll(storeMap);
        prop.store(new FileOutputStream("D:\\1.properties"), System.currentTimeMillis() + "");
    }

    @Test
    public void test4() throws Exception {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("D:\\1.properties");
        prop.load(fis);
        fis.close();
        System.out.println(prop.getProperty("name"));
        prop.setProperty("name", "22222");
        FileOutputStream fos = new FileOutputStream("D:\\1.properties");
        prop.store(fos, "");
        fos.close();
        System.out.println(prop.getProperty("name"));
        System.out.println(prop.getProperty("22"));
//        prop.setProperty("name", "leoleo");
//        prop.store(new FileOutputStream("D:\\1.properties"), System.currentTimeMillis() + "");
    }

    @Test
    public void test5() {
        PropertiesUtil propUtil = new PropertiesUtil();
        System.out.println(propUtil.get("name"));
    }

}
