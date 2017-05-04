import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.KeywordTagDO;
import cn.yunyichina.log.common.entity.do_.KvTagDO;
import cn.yunyichina.log.common.entity.dto.SearchConditionDTO;
import cn.yunyichina.log.component.common.constant.IndexFormat;
import cn.yunyichina.log.component.common.constant.IndexType;
import cn.yunyichina.log.component.index.entity.ContextInfo;
import cn.yunyichina.log.component.index.entity.KeywordIndex;
import cn.yunyichina.log.component.index.entity.KvIndex;
import cn.yunyichina.log.component.scanner.imp.IndexCacheScanner;
import cn.yunyichina.log.service.collector.constants.CacheName;
import cn.yunyichina.log.service.collector.task.CleanCacheTask;
import cn.yunyichina.log.service.collector.task.LogTask;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/8 16:35
 * @Description:
 */
public class UnitTest {

    public static final File LOG_FILE = new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\13\\17\\35\\201704131735.log");

    public String getCacheDirBy(String logName, String typeName) {
        return typeName + File.separator + logName.substring(0, 4) + File.separator + logName.substring(4, 6)
                + File.separator + logName.substring(6, 8) + File.separator + logName.substring(8, 10)
                + File.separator + logName.substring(10, 12) + File.separator + logName + CacheName.CONTEXT_INDEX_SUFFIX;
    }

    @Test
    public void testScheduleBuildContext() throws Exception {
        Collection<File> logs = new HashSet<>();
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\01\\201704261401.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\02\\201704261402.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\03\\201704261403.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\04\\201704261404.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\05\\201704261405.log"));
        LogTask logTask = new LogTask();
        Map<String, ConcurrentHashMap<String, ContextInfo>> contextInfoMap = logTask.buildContextIndexCache(logs);
        System.out.println(JSON.toJSONString(contextInfoMap, true));
        logTask.writeContextIndexToDisk(contextInfoMap, 12);
    }

    @Test
    public void testScheduleBuildbuildKeywordIndex() throws Exception {
        Collection<File> logs = new HashSet<>();
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\01\\201704261401.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\02\\201704261402.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\03\\201704261403.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\04\\201704261404.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\05\\201704261405.log"));
        List<KeywordTagDO> keywordTagList = new ArrayList<>();
        keywordTagList.add(new KeywordTagDO().setKeyword("getPatient"));
        keywordTagList.add(new KeywordTagDO().setKeyword("pat_address"));
        CollectedItemDO collectedItem = new CollectedItemDO();
        collectedItem.setKeywordTagList(keywordTagList);
        LogTask logTask = new LogTask();
        Map<String, ConcurrentHashMap<String, Set<KeywordIndex>>> keywordIndexMap = logTask.buildKeywordIndexCache(logs, collectedItem);
        System.out.println(JSON.toJSONString(keywordIndexMap, true));
        logTask.writeKeywordIndexToDisk(keywordIndexMap, 10);
    }

    @Test
    public void testScheduleBuildbuildKvIndex() throws Exception {
        Collection<File> logs = new HashSet<>();
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\01\\201704261401.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\02\\201704261402.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\03\\201704261403.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\04\\201704261404.log"));
        logs.add(new File("E:\\yunyi\\log-sys\\log-resource\\test-resource\\log\\2017\\04\\26\\14\\05\\201704261405.log"));
        List<KvTagDO> kvTagList = new ArrayList<>();
        kvTagList.add(new KvTagDO().setKey("pat_address").setKeyTag("\"pat_id_no\":\"").setValueEndTag("\""));
        CollectedItemDO collectedItem = new CollectedItemDO();
        collectedItem.setKvTagList(kvTagList);
        LogTask logTask = new LogTask();
        Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, Set<KvIndex>>>> kvIndexMap = logTask.buildKvIndexCache(logs, collectedItem);
        System.out.println(JSON.toJSONString(kvIndexMap, true));
        logTask.writeKvIndexToDisk(kvIndexMap, 12);
    }

    @Test
    public void testFile() throws Exception {
//        File file = new File("E:\\log\\2016\\11\\15\\14\\21\\201611151421.log");
//        String logName = file.getName().substring(0,file.getName().indexOf("."));
//
//        System.out.println(getCacheDirBy(logName,"context"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String begin = "201704131758";
        String end = "201704131802";
        Date beginDate = sdf.parse(begin);
        Date endDate = sdf.parse(end);
        long diff = (endDate.getTime() - beginDate.getTime()) / 1000 / 60;
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < diff; i++) {
            cal.setTime(beginDate);
            cal.add(Calendar.MINUTE, i);
            System.out.println(sdf.format(cal.getTime()));
        }


    }

    @Test
    public void test() throws IOException {
        File file = new File("D:\\gitRepo\\yunyi\\src\\log\\log-sys\\log-resource\\test-resource\\log\\2017\\02\\22\\18\\11\\201702221811.log");
        String s = Files.toString(file, Charsets.UTF_8);
        int length = s.length();
        System.out.println(length);
        int segCount = length % 1000;
        System.out.println(segCount);
    }

    @Test
    public void read() throws IOException, ClassNotFoundException {
        String cachePath = "E:\\cache\\7\\context.cache";
        File cacheFile = new File(cachePath);
        if (cacheFile.exists()) {

        } else {
            Files.createParentDirs(cacheFile);
            cacheFile.createNewFile();
            try (
                    FileOutputStream fos = new FileOutputStream(cachePath);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
            ) {
                oos.writeObject(null);
                oos.flush();
            }
        }
        try (
                FileInputStream fis = new FileInputStream(cachePath);
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            System.out.println(JSON.toJSONString(ois.readObject(), true));
            ;
        }
    }

    @Test
    public void test0() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        c.set(Calendar.MINUTE, 10);
        System.out.println(c.get(Calendar.MINUTE));
        System.out.println(dateFormat.format(c.getTime()));
    }

    @Test
    public void test1() throws IOException {
        long begin;
        SearchConditionDTO dto = new SearchConditionDTO()
                .setBeginDateTime(new Date())
                .setCollectedItemId(123123)
                .setEndDateTime(new Date())
                .setFuzzy(false)
                .setKey("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                .setKeyword("sdfsadfdsafasldkfakjsdfajkdsfhkasdfhkasdhkfa")
                .setNoIndexKeyword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                .setSearchEngineType(123)
                .setValue("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        begin = System.nanoTime();
        byte[] bytes = JSON.toJSONBytes(dto);
        System.out.println(BigDecimal.valueOf(System.nanoTime() - begin, 9));
        begin = System.nanoTime();
        SearchConditionDTO o = JSON.parseObject(bytes, SearchConditionDTO.class);
        System.out.println(BigDecimal.valueOf(System.nanoTime() - begin, 9));
        System.out.println("=========================");
        begin = System.nanoTime();
        String json = JSON.toJSONString(dto);
        System.out.println(BigDecimal.valueOf(System.nanoTime() - begin, 9));
        begin = System.nanoTime();
        SearchConditionDTO dto1 = JSON.parseObject(json, SearchConditionDTO.class);
        System.out.println(BigDecimal.valueOf(System.nanoTime() - begin, 9));
        System.out.println("=========================");
        begin = System.nanoTime();
        Files.write(bytes, new File("D://3.json"));
        System.out.println(BigDecimal.valueOf(System.nanoTime() - begin, 9));
        System.out.println("=========================");
        begin = System.nanoTime();
        Files.write(json, new File("D://4.json"), StandardCharsets.UTF_8);
        System.out.println(BigDecimal.valueOf(System.nanoTime() - begin, 9));
        System.out.println("=========================");

    }

    @Test
    public void test2() throws IOException {
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 10_000_000);
        System.out.println(bloomFilter.mightContain("leo"));
        bloomFilter.put("leo");
        System.out.println(bloomFilter.mightContain("leo"));
//        bloomFilter.writeTo(new FileOutputStream("D://bloom"));
        System.out.println();
    }

    @Test
    public void test3() throws IOException {
        File file = new File("D://1//1.txt");
        Files.createParentDirs(file);
        Files.write("asd".getBytes(StandardCharsets.UTF_8), file);
    }

    @Test
    public void test4() throws IOException {
//        File file = new File("E:\\cache");
//        File[] files = file.listFiles();
//        System.out.println(files[4].getName());
        String indexDir = "E:\\cache\\test\\ctx";
        String beginDatetime = "2017-04-26 14:01";
        String endDatetime = "2017-04-26 14:03";
        List<File> fileList = IndexCacheScanner.scan(beginDatetime, endDatetime, indexDir, IndexFormat.CONTEXT);
        for (File file : fileList) {
            deleteDir(file, IndexType.CONTEXT);
        }

    }

    public void deleteDir(File file, IndexType indexType) {
        if (file.isFile()) {
            file.delete();
        }
        File parentFile = file.getParentFile();
        if (parentFile.listFiles().length > 1) {

        } else {
            if (parentFile.getName().equals(indexType.getVal())) {

            } else {
                parentFile.delete();
                deleteDir(parentFile, indexType);
            }
        }
    }

    @Test
    public void test5() {
        System.out.println(findSmallestTreeFile(new File("E:\\cache\\test\\ctx")));
    }

    public String findSmallestTreeFile(File parentTreeFile) {
        System.out.println(parentTreeFile.getPath());
        File[] files = parentTreeFile.listFiles();
        if (files.length > 0) {
            if (files[0].isDirectory()) {
                return findSmallestTreeFile(new File(parentTreeFile.getPath() + File.separator + files[0].getName()));
            } else {
                return getBeginDateTimeBy(files[0].getName());
            }
        }
        return null;
    }

    public String getBeginDateTimeBy(String indexFileName) {
        indexFileName = indexFileName.substring(0, indexFileName.indexOf("."));
        return indexFileName.substring(0, 4) + "-" + indexFileName.substring(4, 6) + "-" + indexFileName.substring(6, 8)
                + " " + indexFileName.substring(8, 10) + ":" + indexFileName.substring(10, 12);
    }

    @Test
    public void test6() {
        CleanCacheTask cleanCacheTask = new CleanCacheTask();
        cleanCacheTask.execute();
    }

}
