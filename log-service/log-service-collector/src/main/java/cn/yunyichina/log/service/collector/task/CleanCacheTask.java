package cn.yunyichina.log.service.collector.task;

import cn.yunyichina.log.component.common.constant.IndexFormat;
import cn.yunyichina.log.component.common.constant.IndexType;
import cn.yunyichina.log.component.scanner.imp.IndexCacheScanner;
import cn.yunyichina.log.service.collector.util.CacheUtil;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhoufeng on 2017/4/27 0027.
 */
@Service
public class CleanCacheTask {

    private final FastDateFormat cacheDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");

    private final String DOT = ".";

    private final int A_WEEK = 7;//cache 寿命:7 天

    /**
     * 定时任务执行间隔:2小时1次
     */
    @Scheduled(fixedRateString = "${cleanCacheIntervalInMillis:7200000}")
    public void execute() {
        File[] collectedItemCacheFiles = new File(CacheUtil.CACHE_DIR).listFiles();
        if (collectedItemCacheFiles != null) {
            for (File collectedItemCacheFile : collectedItemCacheFiles) {
                try {
                    deleteIndexCache(collectedItemCacheFile, IndexType.CONTEXT, IndexFormat.CONTEXT);
                    deleteIndexCache(collectedItemCacheFile, IndexType.KEYWORD, IndexFormat.KEYWORD);
                    deleteIndexCache(collectedItemCacheFile, IndexType.KEY_VALUE, IndexFormat.KEY_VALUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteIndexCache(File collectedItemCacheFile, IndexType indexType, IndexFormat indexFormat) throws Exception {
        String indexDir = CacheUtil.CACHE_DIR + File.separator + collectedItemCacheFile.getName() + File.separator + indexType.getVal();
        String beginDatetime = findOldestCache(indexDir);
        if (beginDatetime != null) {
            Calendar c = Calendar.getInstance();
            c.clear();
            c.setTime(cacheDateFormat.parse(beginDatetime));
            c.add(Calendar.DATE, A_WEEK);
            String endDatetime = cacheDateFormat.format(c.getTime());
            deleteIndexCacheBy(indexDir, beginDatetime, endDatetime, indexType, indexFormat);
        }
    }

    public void deleteIndexCacheBy(String indexDir, String beginDatetime, String endDatetime, IndexType indexType, IndexFormat indexFormat) {
        List<File> fileList = IndexCacheScanner.scan(beginDatetime, endDatetime, indexDir, indexFormat);
        for (File file : fileList) {
            CacheUtil.deleteIndexCache(file, indexType);
        }
    }

    public String findOldestCache(String parentTree) {
        File parentTreeFile = new File(parentTree);
        if (parentTreeFile.exists()) {
            File[] files = parentTreeFile.listFiles();
            if (files.length > 0) {
                if (files[0].isDirectory()) {
                    return findOldestCache(parentTreeFile.getPath() + File.separator + files[0].getName());
                } else {
                    return getBeginDatetimeBy(files[0].getName());
                }
            }
        }
        return null;
    }

    public String getBeginDatetimeBy(String indexFileName) {
        indexFileName = indexFileName.substring(0, indexFileName.indexOf(DOT));
        return indexFileName.substring(0, 4) + "-" + indexFileName.substring(4, 6) + "-" + indexFileName.substring(6, 8)
                + " " + indexFileName.substring(8, 10) + ":" + indexFileName.substring(10, 12);
    }

}
