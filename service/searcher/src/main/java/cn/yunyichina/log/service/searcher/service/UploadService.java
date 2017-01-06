package cn.yunyichina.log.service.searcher.service;

import cn.yunyichina.log.common.constant.IndexType;
import cn.yunyichina.log.common.log.LoggerWrapper;
import cn.yunyichina.log.common.util.ZipUtil;
import cn.yunyichina.log.component.index.builder.imp.ContextIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeyValueIndexBuilder;
import cn.yunyichina.log.component.index.builder.imp.KeywordIndexBuilder;
import cn.yunyichina.log.service.searcher.util.IndexManager;
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

    final LoggerWrapper logger = LoggerWrapper.getLogger(UploadService.class);

    @Autowired
    IndexManager indexManager;

    private final String LOG_SUFFIX = ".log";
    private final String INDEX_SUFFIX = ".index";
    private final String BAK_SUFFIX = ".bak";
    private final String ZIP_SUFFIX = ".zip";

    @Value("${constants.upload.logRootDir}")
    private String UPLOADED_LOG_ROOT_DIR;

    @Value("${constants.upload.indexRootDir}")
    private String INDEX_ROOT_DIR;

    public void uploadFile(MultipartFile file) throws Exception {
        logger.info("日志文件大小:" + file.getSize());
        byte[] bytes = file.getBytes();
        File uploadedLogRootDir = new File(UPLOADED_LOG_ROOT_DIR);
        if (uploadedLogRootDir.exists()) {
            logger.info("日志根目录已存在:" + UPLOADED_LOG_ROOT_DIR);
        } else {
            boolean succeed = uploadedLogRootDir.mkdirs();
            if (succeed) {
                logger.info("日志根目录创建成功:" + UPLOADED_LOG_ROOT_DIR);
            } else {
                logger.error("上传文件时,创建目录失败." + UPLOADED_LOG_ROOT_DIR);
                throw new Exception("上传文件时,创建目录失败." + UPLOADED_LOG_ROOT_DIR);
            }
        }
        String zipFilePath = uploadedLogRootDir + File.separator + file.getName() + ZIP_SUFFIX;
        logger.info("Zip文件路径:" + zipFilePath);
        Files.write(bytes, new File(zipFilePath));
        ZipUtil.unzip(zipFilePath, UPLOADED_LOG_ROOT_DIR);
        rebuildLogDirStructureAndUpdateIndex();
    }

    private void rebuildLogDirStructureAndUpdateIndex() throws Exception {
        File[] uploadedLogFiles = new File(UPLOADED_LOG_ROOT_DIR).listFiles();
        if (uploadedLogFiles == null || uploadedLogFiles.length <= 0) {
            logger.info("zip文件没有内容");
        } else {
            for (File uploadedLogFile : uploadedLogFiles) {
                if (uploadedLogFile.isFile()) {
                    String fileName = uploadedLogFile.getName();
                    int dotIndex = fileName.lastIndexOf(".");
                    String fileSuffix = fileName.substring(dotIndex);
                    fileName = fileName.substring(0, dotIndex);
                    if (Objects.equal(fileSuffix, LOG_SUFFIX)) {
                        restructureLogFile(uploadedLogFile, fileName, fileSuffix);
                    } else if (Objects.equal(fileSuffix, INDEX_SUFFIX)) {
                        updateIndexManager(uploadedLogFile, fileName);
                    } else {
                        logger.info("仅处理 '" + LOG_SUFFIX + "' & '" + INDEX_SUFFIX + "' 格式的文件");
                    }
                } else {
//                          不处理目录
                }
            }
        }
    }

    private void updateIndexManager(File uploadedLogFile, String indexFileName) throws Exception {
        ObjectInputStream ois = null;
        try {
            logger.info("开始更新indexManager");
            ois = new ObjectInputStream(new FileInputStream(uploadedLogFile));
            logger.info("索引名:" + indexFileName + ", 索引文件:" + uploadedLogFile.getAbsolutePath());
            switch (indexFileName) {
                case IndexType.CONTEXT:
                    Map<Long, ContextIndexBuilder.ContextInfo> contextIndexMap = (Map<Long, ContextIndexBuilder.ContextInfo>) ois.readObject();
                    indexManager.appendContextIndex(contextIndexMap);
                    backupAndReplace(indexFileName, indexManager.getContextIndexMap());
                    break;
                case IndexType.KEYWORD:
                    Map<String, Set<KeywordIndexBuilder.IndexInfo>> keywordIndexMap = (Map<String, Set<KeywordIndexBuilder.IndexInfo>>) ois.readObject();
                    indexManager.appendKeywordIndex(keywordIndexMap);
                    backupAndReplace(indexFileName, indexManager.getKeywordIndexMap());
                    break;
                case IndexType.KEY_VALUE:
                    Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>> keyValueIndexMap = (Map<String, Map<String, Set<KeyValueIndexBuilder.IndexInfo>>>) ois.readObject();
                    indexManager.appendKeyValueIndex(keyValueIndexMap);
                    backupAndReplace(indexFileName, indexManager.getKeyValueIndexMap());
                    break;
                default:
                    logger.info("不支持的索引类型" + indexFileName);
                    break;
            }
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } finally {
                    boolean succeed = uploadedLogFile.delete();
                    if (succeed) {
                        logger.info("成功删除临时文件:" + uploadedLogFile.getAbsolutePath());
                    } else {
                        logger.error("删除临时文件失败:" + uploadedLogFile.getAbsolutePath());
                        throw new Exception("删除上传的日志文件失败");
                    }
                }
            }
        }
    }

    private void restructureLogFile(File uploadedLogFile, String fileName, String fileSuffix) throws Exception {
        String restructureLogFilePath = restructureLogFilePath(fileName, fileSuffix);
        logger.info("根据日志名恢复日志路径:" + restructureLogFilePath);
        File restructureLogFile = new File(restructureLogFilePath);
        if (restructureLogFile.exists()) {
            logger.info("日志已存在" + restructureLogFilePath);
            boolean succeed = uploadedLogFile.delete();
            if (succeed) {
                logger.info("成功删除上传的日志文件:" + restructureLogFilePath);
            } else {
                logger.error("恢复日志目录时,替换旧日志文件失败" + restructureLogFilePath);
                throw new Exception("恢复日志目录时,替换旧日志文件失败" + restructureLogFilePath);
            }
        } else {
            logger.info("日志不存在,即将构建目录和移动它");
            Files.createParentDirs(restructureLogFile);
            Files.move(uploadedLogFile, restructureLogFile);
            logger.info("移动成功");
        }
    }

    private String restructureLogFilePath(String fileName, String fileSuffix) {
        return UPLOADED_LOG_ROOT_DIR + File.separator + fileName.substring(0, 4) +
                File.separator + fileName.substring(4, 6) +
                File.separator + fileName.substring(6, 8) +
                File.separator + fileName.substring(8, 10) +
                File.separator + fileName.substring(10, 12) +
                File.separator + fileName + fileSuffix;
    }

    /**
     * 将索引文件备份并替换
     *
     * @param indexFileName
     * @param indexObj
     * @throws IOException
     */
    private void backupAndReplace(String indexFileName, Object indexObj) throws IOException {
        String oldIndexFilePath = INDEX_ROOT_DIR + File.separator + indexFileName + INDEX_SUFFIX;
        File oldIndexFile = new File(oldIndexFilePath);
        logger.info("开始备份索引文件:" + oldIndexFilePath);
        if (oldIndexFile.exists()) {
            logger.info("索引文件已存在:" + oldIndexFilePath);
            String bakIndexFilePath = INDEX_ROOT_DIR + File.separator + indexFileName + BAK_SUFFIX;
            File bakIndexFile = new File(bakIndexFilePath);
            if (bakIndexFile.exists()) {
                logger.info("备份索引文件已存在:" + bakIndexFilePath);
            } else {
                Files.createParentDirs(bakIndexFile);
                logger.info("成功创建索引备份文件:" + bakIndexFilePath);
            }
            Files.write(Files.asByteSource(oldIndexFile).read(), bakIndexFile);
            logger.info("成功备份索引:" + indexFileName);
        } else {
            Files.createParentDirs(oldIndexFile);
            logger.info("索引文件不存在,但成功创建:" + oldIndexFilePath);
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(oldIndexFile));
            oos.writeObject(indexObj);
            oos.flush();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }

    }

}
