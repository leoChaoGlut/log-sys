import cn.yunyichina.log.common.util.ThreadPool;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.service.collector.constants.Key;
import cn.yunyichina.log.service.collector.util.PropertiesUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * Created by Jonven on 2016/12/5.
 */
@ComponentScan("cn.yunyichina.log.service.collector.util")
public class MyTest {

    @Autowired
    PropertiesUtil propUtil;

    @Autowired
    ThreadPool threadPool;


    @Before
    public void before() throws ExecutionException, InterruptedException {


//
//        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyTest.class);
//        System.out.println("be2222222fore");
    }


    public class TestClass implements Callable<Object> {

        int i;

        public TestClass(int i) {
            this.i = i;
        }

        @Override
        public Object call() throws Exception {
            return i;
        }
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        List<Future> futureList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Future<Object> future = threadPool.getThreadPool().submit(new TestClass(i));
            futureList.add(future);
        }

        threadPool.getThreadPool().shutdown();
        ;
        while (!threadPool.getThreadPool().isTerminated()) {
        }

        for (int i = 0; i < futureList.size(); i++) {
            Object returValue = futureList.get(i).get();
        }
    }

    @Test
    public void testCache() throws IOException {
        System.out.println("======1111111==========");
        Map<String, Object> cacheMap = new HashMap();
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


    }


    @Test
    public void test3() throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream("D:\\1.properties"));
        Set<Map.Entry<Object, Object>> entrySet = prop.entrySet();
        Map<Object, Object> storeMap = null;
        if (CollectionUtils.isEmpty(entrySet)) {
            storeMap = new HashMap();
        } else {
            storeMap = new HashMap(entrySet.size() << 1);
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
    public void test5() throws Exception {
        Set<File> fileSet = new HashSet<>();
        fileSet.add(new File("D:\\tmp\\2016\\11\\15\\14\\21\\201611151421.log"));
        fileSet.add(new File("D:\\tmp\\2016\\11\\15\\14\\23\\201611151423.log"));
        fileSet.add(new File("D:\\tmp\\2016\\11\\15\\14\\25\\201611151425.log"));

        Set<String> keywordSet = new HashSet<>();
        keywordSet.add("<branchCode>");
        keywordSet.add("patName");
        keywordSet.add("pat_mobile");

        Set<KeyValueIndexBuilder.KvTag> kvTagSet = new HashSet<>();
        kvTagSet.add(new KeyValueIndexBuilder.KvTag("patCardNo", "<patCardNo>", "</patCardNo>"));

        Properties prop = new Properties();
        prop.load(new FileInputStream("D:\\1.properties"));

        prop.setProperty(Key.LAST_MODIFY_TIME, "2016-10-29 16:27");
        prop.setProperty(Key.UPLOAD_FAILED_FILE_LIST, JSON.toJSONString(fileSet));
        prop.setProperty(Key.KEYWORD_SET, JSON.toJSONString(keywordSet));
        prop.setProperty(Key.KV_TAG_SET, JSON.toJSONString(kvTagSet));

        prop.store(new FileOutputStream("D:\\1.properties"), "");

    }

}
