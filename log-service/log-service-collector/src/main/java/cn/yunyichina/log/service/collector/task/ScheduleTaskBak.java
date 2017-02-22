package cn.yunyichina.log.service.collector.task;

import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.common.util.ZipUtil;
import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.index.scanner.imp.LogFileScanner;
import cn.yunyichina.log.service.collector.constants.Config;
import cn.yunyichina.log.service.collector.constants.Key;
import cn.yunyichina.log.service.collector.service.ScheduleService;
import cn.yunyichina.log.service.collector.util.PropertiesUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder.IndexInfo;
import static cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder.KvTag;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/5 14:18
 * @Description:
 */
//@Service
public class ScheduleTaskBak {

    final LoggerWrapper logger = LoggerWrapper.getLogger(ScheduleTaskBak.class);

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private Config config;

    @Autowired
    private PropertiesUtil propUtil;

    @Autowired
    private ScheduleService scheduleService;

    private final File[] FILE = new File[0];

    @Scheduled(fixedRateString = "${fixedRate}")
    public void execute() {
        logger.contextBegin("定时任务开始,间隔: " + config.getFixedRate() + " ms");
        Set<File> fileSet = null;
        try {
            Date now = new Date();
            Map<String, File> logFileMap = scanLastestLogFiles(now.getTime());
            logger.info("扫描到的日志文件:" + JSON.toJSONString(logFileMap));
            if (CollectionUtils.isEmpty(logFileMap)) {
                logger.contextEnd("没有需要上传的日志文件");
            } else {
                String json = propUtil.get(Key.UPLOAD_FAILED_FILE_LIST);
                logger.info("上传失败文件列表:" + json);
                List<File> uploadFailedFileList = JSON.parseArray(json, File.class);//获取上传失败的文件
                int uploadFailedFileSize = uploadFailedFileList == null ? 0 : uploadFailedFileList.size();
                Collection<File> fileCollection = logFileMap.values();
                fileSet = new HashSet<>(fileCollection.size() + uploadFailedFileSize);
                fileSet.addAll(fileCollection);

                if (CollectionUtils.isEmpty(uploadFailedFileList)) {

                } else {
                    fileSet.addAll(uploadFailedFileList);
                }

                buildIndexAndFlushToDisk(fileSet);
                boolean uploadSucceed = uploadFiles(fileSet);
                if (uploadSucceed) {
                    propUtil.remove(Key.UPLOAD_FAILED_FILE_LIST);
                    propUtil.put(Key.LAST_MODIFY_TIME, sdf.format(now));
                } else {
                    propUtil.put(Key.UPLOAD_FAILED_FILE_LIST, JSON.toJSONString(fileSet));
                }
                logger.contextEnd("上传" + (uploadSucceed ? "成功" : "失败"));
            }
        } catch (Exception e) {
            if (fileSet != null) {
                propUtil.put(Key.UPLOAD_FAILED_FILE_LIST, JSON.toJSONString(fileSet));
            }
            logger.error("定时任务抛出异常:" + e.getLocalizedMessage(), e);
            logger.contextEnd("定时任务抛出异常:" + e.getLocalizedMessage());
        }
    }

    /**
     * Date传递是的引用,不是传值,所以要传timestamp
     *
     * @param currentTimestamp
     * @return
     */
    private Map<String, File> scanLastestLogFiles(long currentTimestamp) {
        String beginDatetime = propUtil.get(Key.LAST_MODIFY_TIME);
        logger.info("从cache.properties中获取上一次上传日志的时间:" + beginDatetime);
        String endDatetime = sdf.format(new Date(currentTimestamp));
        if (beginDatetime == null || "".equals(beginDatetime.trim())) {
            long fixedRateAgo = currentTimestamp - config.getFixedRate();
            beginDatetime = sdf.format(new Date(fixedRateAgo));
            logger.info("开始时间为空,以现在时间往后推: " + config.getFixedRate() + " ms");
        }
        logger.info("时间区间:" + beginDatetime + " - " + endDatetime);
        LogFileScanner scanner = new LogFileScanner(beginDatetime, endDatetime, config.getLogRootDir());
        Map<String, File> logFileMap = scanner.scan();
        return logFileMap;
    }

    private void buildIndexAndFlushToDisk(Set<File> fileSet) throws Exception {
        Map<Long, ContextIndexBuilder.ContextInfo> contextInfoMap = buildContextIndexByFiles(fileSet);

        scheduleService.recordLastestContextCount(null, null);

        Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap = buildKeywordIndexByFiles(fileSet);
        Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap = buildKeyValueIndexByFiles(fileSet);

        File contextIndexFile = new File(config.getTmpZipDir() + File.separator + Key.CONTEXT_INFO_INDEX_FILE_NAME);
        indexPersistence(contextIndexFile, contextInfoMap);
        fileSet.add(contextIndexFile);

        File keywordIndexFile = new File(config.getTmpZipDir() + File.separator + Key.KEYWORD_INDEX_FILE_NAME);
        indexPersistence(keywordIndexFile, keywordIndexMap);
        fileSet.add(keywordIndexFile);

        File keyValueIndexFile = new File(config.getTmpZipDir() + File.separator + Key.KEY_VALUE_INDEX_FILE_NAME);
        indexPersistence(keyValueIndexFile, keyValueIndexMap);
        fileSet.add(keyValueIndexFile);
    }


    private boolean uploadFiles(Set<File> fileSet) throws Exception {
        String zipFilePath = config.getTmpZipDir() + File.separator + Key.ZIP_FILE_NAME;
        logger.info("上传文件总数:" + fileSet.size());
        ZipUtil.zip(zipFilePath, fileSet.toArray(FILE));
        File zipFile = new File(zipFilePath);
        if (zipFile.exists()) {
            logger.info("zip大小:" + zipFile.getTotalSpace());
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(config.getUploadServerUrl());
            HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("zipFile", zipFile).build();
            post.setEntity(entity);
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    boolean succeed = zipFile.delete();
                    if (succeed) {

                    } else {
                        throw new Exception("上传日志文件成功,但删除zip文件失败.");
                    }
                    return true;
                } else {
                    return false;
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        } else {
            return false;
        }
    }

    private Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> buildKeyValueIndexByFiles(Set<File> fileSet) {
        KeyValueIndexAggregator aggregator = new KeyValueIndexAggregator();
        List<KvTag> kvTagList = JSON.parseArray(propUtil.get(Key.KV_TAG_SET), KvTag.class);
        if (CollectionUtils.isEmpty(kvTagList)) {
            return null;
        } else {
            Set<KvTag> kvTagSet = new HashSet<>(kvTagList);
            for (File file : fileSet) {
                KeyValueIndexBuilder builder = new KeyValueIndexBuilder(kvTagSet, file);
                Map<String, Map<String, Set<IndexInfo>>> keyValueIndexMap = builder.build();
                aggregator.aggregate(keyValueIndexMap);
            }
            return aggregator.getAggregatedCollection();
        }
    }

    private Map<String, Set<KeywordIndexBuilder.IndexInfo>> buildKeywordIndexByFiles(Set<File> fileSet) {
        KeywordIndexAggregator aggregator = new KeywordIndexAggregator();
        List<String> keywordList = JSON.parseArray(propUtil.get(Key.KEYWORD_SET), String.class);
        if (CollectionUtils.isEmpty(keywordList)) {
            return null;
        } else {
            Set<String> keywordSet = new HashSet<>(keywordList);
            for (File file : fileSet) {
                KeywordIndexBuilder builder = new KeywordIndexBuilder(file, keywordSet);
                Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordeIndexMap = builder.build();
                aggregator.aggregate(keywordeIndexMap);
            }
            return aggregator.getAggregatedCollection();
        }
    }

    private Map<Long, ContextIndexBuilder.ContextInfo> buildContextIndexByFiles(Set<File> fileSet) {
        ContextIndexAggregator aggregator = new ContextIndexAggregator();
        for (File file : fileSet) {
            ContextIndexBuilder builder = new ContextIndexBuilder(file);
            Map<Long, ContextIndexBuilder.ContextInfo> contextInfoMap = builder.build();
            aggregator.aggregate(contextInfoMap);
        }
        return aggregator.getAggregatedCollection();
    }


    private void indexPersistence(File indexFile, Object indexObj) throws Exception {
        ObjectOutputStream oos = null;
        try {
            if (indexFile.exists()) {

            } else {
                Files.createParentDirs(indexFile);
                boolean succeed = indexFile.createNewFile();
                if (succeed) {

                } else {
                    throw new Exception("持久化索引文件失败");
                }
            }
            oos = new ObjectOutputStream(new FileOutputStream(indexFile));
            oos.writeObject(indexObj);
            oos.flush();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }


}
