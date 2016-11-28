package cn.yunyichina.log.service.searcherNode.service;

import cn.yunyichina.log.common.util.ZipUtil;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.service.searcherNode.constant.IndexType;
import cn.yunyichina.log.service.searcherNode.util.IndexManager;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jonven on 2016/11/28.
 */
@Service
public class UploadService {

    @Autowired
    IndexManager indexManager;

    @Value("${constants.upload.logSuffix}")
    private String LOG_SUFFIX;

    @Value("${constants.upload.indexSuffix}")
    private String INDEX_SUFFIX;

    @Value("${constants.upload.bakSuffix}")
    private String BAK_SUFFIX;

    @Value("${constants.upload.zipIndex}")
    private String ZIP_SUFFIX;

    @Value("${constants.upload.logRootDir}")
    private String LOG_ROOT_DIR;

    @Value("${constants.upload.indexRootDir}")
    private String INDEX_ROOT_DIR;

    public void uploadFile(MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        File dir = new File(LOG_ROOT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String zipFilePath = dir + File.separator + file.getName() + ZIP_SUFFIX;
        Files.write(bytes, new File(zipFilePath));
        ZipUtil.unzip(zipFilePath, LOG_ROOT_DIR);
        rebuildLogDirStructureAndUpdateIndex();
    }

    private void rebuildLogDirStructureAndUpdateIndex() throws Exception {
        File[] logFiles = new File(LOG_ROOT_DIR).listFiles();
        if (logFiles != null) {
            for (File file : logFiles) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));
                    if (Objects.equal(fileSuffix, LOG_SUFFIX)) {
                        String oldLogFilePath = buildOldLogFilePath(fileName, fileSuffix);
                        File oldLogFile = new File(oldLogFilePath);
                        if (oldLogFile.exists()) {
                            file.delete();
                        } else {
                            Files.createParentDirs(oldLogFile);
                            Files.move(file, oldLogFile);
                        }
                    } else if (Objects.equal(fileSuffix, INDEX_SUFFIX)) {
                        ObjectInputStream ois = null;
                        try {
                            ois = new ObjectInputStream(new FileInputStream(file));
                            switch (fileName) {
                                case IndexType.CONTEXT:
                                    Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap = (Map<Long, ContextIndexBuilder.ContextInfo>) ois.readObject();
                                    indexManager.appendContextIndex(contextIndexMap);
                                    backupAndReplace(fileName, indexManager.getContextIndexMap());
                                    break;
                                case IndexType.KEYWORD:
                                    Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap = (Map<String, Set<KeywordIndexBuilder.IndexInfo>>) ois.readObject();
                                    indexManager.appendKeywordIndex(keywordIndexMap);
                                    backupAndReplace(fileName, indexManager.getKeywordIndexMap());
                                    break;
                                case IndexType.KEY_VALUE:
                                    Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap = (Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>>) ois.readObject();
                                    indexManager.appendKeyValueIndex(keyValueIndexMap);
                                    backupAndReplace(fileName, indexManager.getKeyValueIndexMap());
                                    break;
                                default:
                                    throw new Exception("不支持的索引类型");
                            }
                        } finally {
                            file.delete();
                            if (ois != null) {
                                ois.close();
                            }
                        }
                    }
                } else {

                }
            }
        }
    }

    private String buildOldLogFilePath(String fileName, String fileSuffix) {
        return LOG_ROOT_DIR + File.separator + fileName.substring(0, 4) +
                File.separator + fileName.substring(4, 6) +
                File.separator + fileName.substring(6, 8) +
                File.separator + fileName.substring(8, 10) +
                File.separator + fileName.substring(10, 12) +
                File.separator + fileName + fileSuffix;
    }

    private void backupAndReplace(String fileName, Object obj) throws IOException {
        File oldIndexFile = new File(INDEX_ROOT_DIR + fileName + INDEX_SUFFIX);
        if (oldIndexFile.exists()) {
            File bakIndexFile = new File(INDEX_ROOT_DIR + fileName + BAK_SUFFIX);
            if (bakIndexFile.exists()) {

            } else {
                Files.createParentDirs(bakIndexFile);
            }
            Files.write(Files.asByteSource(oldIndexFile).read(), bakIndexFile);
        } else {
            Files.createParentDirs(oldIndexFile);
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(oldIndexFile));
            oos.writeObject(obj);
            oos.flush();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }

    }

}
