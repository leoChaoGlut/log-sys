package cn.yunyichina.log.service.collectorNode.task;

import cn.yunyichina.log.common.util.ZipUtil;
import cn.yunyichina.log.component.aggregator.index.imp.ContextIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeyValueIndexAggregator;
import cn.yunyichina.log.component.aggregator.index.imp.KeywordIndexAggregator;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.component.index.scanner.imp.LogFileScanner;
import cn.yunyichina.log.service.collectorNode.constants.Config;
import cn.yunyichina.log.service.collectorNode.util.CacheManager;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import static cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder.KvTag;
import static cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder.IndexInfo;
/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/5 14:18
 * @Description:
 */
@Service
public class ScheduleTask {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final String LAST_MODIFY_TIME = "lastModifyTime";
    private final String UPLOAD_FAILED_FILE_LIST = "uploadFailedFileList";
    private final String CONTEXT_INFO_INDEX_FILE_NAME = "contextInfo.index";
    private final String KEYWORD_INDEX_FILE_NAME = "keyword.index";
    private final String KEY_VALUE_INDEX_FILE_NAME = "keyValue.index";
    private final String ZIP_FILE_NAME = "zip.zip";

    @Autowired
    private Config config;

    @Autowired
    private CacheManager cacheManager;

    @Scheduled(fixedRate = 3000)
    public void execute() {
        System.err.println("=========start");
        Set<File> fileSet = null;

        try {
            String beginDatetime = (String) cacheManager.getCacheMap().get(LAST_MODIFY_TIME);
            Date now = new Date();
            String endDatetime = sdf.format(now);
            if (beginDatetime == null) {
                long fixedRateAgo = now.getTime() - config.getFixedRate();
                beginDatetime = sdf.format(new Date(fixedRateAgo));
            }
            LogFileScanner scanner = new LogFileScanner(beginDatetime, endDatetime, config.getLogRootDir());
            Map<String, File> fileMap = scanner.scan();
            if (CollectionUtils.isEmpty(fileMap)) {
//            没有需要上传的日志文件
            } else {
                Set<File> uploadFailedFileSet = (Set<File>) cacheManager.getCacheMap().get(UPLOAD_FAILED_FILE_LIST);//获取上传失败的文件
                int uploadFailedFileSize = uploadFailedFileSet == null ? 0 : uploadFailedFileSet.size();
                Collection<File> fileCollection = fileMap.values();
                fileSet = new HashSet<>(fileCollection.size() + uploadFailedFileSize);
                fileSet.addAll(fileCollection);

                if (uploadFailedFileSet != null) {
                    fileSet.addAll(uploadFailedFileSet);
                }

                buildIndexAndFlushToDisk(fileSet);
                boolean uploadSucceed = uploadFiles(fileSet);
                if (uploadSucceed) {
                    cacheManager.getCacheMap().remove(UPLOAD_FAILED_FILE_LIST);
                    cacheManager.getCacheMap().put(LAST_MODIFY_TIME, endDatetime);
                } else {
                    cacheManager.getCacheMap().put(UPLOAD_FAILED_FILE_LIST, fileSet);
                }
            }
        } catch (Exception e) {
            if (fileSet != null) {
                cacheManager.getCacheMap().put(UPLOAD_FAILED_FILE_LIST, fileSet);
            }
            e.printStackTrace();
        }
    }

    private void buildIndexAndFlushToDisk(Set<File> fileSet) throws IOException {
        Map<Long, ContextIndexBuilder.ContextInfo> contextInfoMap = buildContextIndexByFiles(fileSet);
        Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap = buildKeywordIndexByFiles(fileSet);
        Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap = buildKeyValueIndexByFiles(fileSet);

        File contextIndexFile = new File(config.getTmpZipDir() + File.separator + CONTEXT_INFO_INDEX_FILE_NAME);
        indexPersistence(contextIndexFile, contextInfoMap);
        fileSet.add(contextIndexFile);

        File keywordIndexFile = new File(config.getTmpZipDir() + File.separator + KEYWORD_INDEX_FILE_NAME);
        indexPersistence(keywordIndexFile, keywordIndexMap);
        fileSet.add(keywordIndexFile);

        File keyValueIndexFile = new File(config.getTmpZipDir() + File.separator + KEY_VALUE_INDEX_FILE_NAME);
        indexPersistence(keyValueIndexFile, keyValueIndexMap);
        fileSet.add(keyValueIndexFile);
    }

    private boolean uploadFiles(Set<File> fileSet) throws Exception {
        String zipFilePath = config.getTmpZipDir() + ZIP_FILE_NAME;
        ZipUtil.zip(zipFilePath, fileSet.toArray(new File[0]));
        File zipFile = new File(zipFilePath);
        if (zipFile.exists()) {
            CloseableHttpClient httpClient = HttpClients.createDefault();
//            TODO 上传的目标url应该是获取到的,而不是在配置中,因为会变化
//            TODO 上传的目标url应该是获取到的,而不是在配置中,因为会变化
//            TODO 上传的目标url应该是获取到的,而不是在配置中,因为会变化
            HttpPost post = new HttpPost(config.getUploadServerUrl());
            HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("zipFile", zipFile).build();
            post.setEntity(entity);
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    zipFile.delete();
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
        Set<KvTag> kvTagSet = (Set<KvTag>) cacheManager.getCacheMap().get("kvTagSet");
        for (File file : fileSet) {
            KeyValueIndexBuilder builder = new KeyValueIndexBuilder(kvTagSet, file);
            Map<String, Map<String, Set<IndexInfo>>> keyValueIndexMap = builder.build();
            aggregator.aggregate(keyValueIndexMap);
        }
        return aggregator.getAggregatedCollection();
    }

    private Map<String, Set<KeywordIndexBuilder.IndexInfo>> buildKeywordIndexByFiles(Set<File> fileSet) {
        KeywordIndexAggregator aggregator = new KeywordIndexAggregator();

        Set<String> keywordSet = (Set<String>) cacheManager.getCacheMap().get("keywordSet");
        for (File file : fileSet) {
            KeywordIndexBuilder builder = new KeywordIndexBuilder(file, keywordSet);
            Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordeIndexMap = builder.build();
            aggregator.aggregate(keywordeIndexMap);
        }
        return aggregator.getAggregatedCollection();
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


    private void indexPersistence(File indexFile, Object indexObj) throws IOException {
        ObjectOutputStream oos = null;
        try {
            if (indexFile.exists()) {

            } else {
                Files.createParentDirs(indexFile);
                indexFile.createNewFile();
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
