package cn.yunyichina.log.service.searcher.service;

import cn.yunyichina.log.common.constant.IndexType;
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
        byte[] bytes = file.getBytes();
        File uploadedLogRootDir = new File(UPLOADED_LOG_ROOT_DIR);
        if (uploadedLogRootDir.exists()) {

        } else {
            boolean succeed = uploadedLogRootDir.mkdirs();
            if (succeed) {

            } else {
                throw new Exception("上传文件时,创建目录失败.");
            }
        }
        String zipFilePath = uploadedLogRootDir + File.separator + file.getName() + ZIP_SUFFIX;
        Files.write(bytes, new File(zipFilePath));
        ZipUtil.unzip(zipFilePath, UPLOADED_LOG_ROOT_DIR);
        rebuildLogDirStructureAndUpdateIndex();
    }

    private void rebuildLogDirStructureAndUpdateIndex() throws Exception {
        File[] uploadedLogFiles = new File(UPLOADED_LOG_ROOT_DIR).listFiles();
        if (uploadedLogFiles == null || uploadedLogFiles.length <= 0) {
//            TODO zip文件没有内容
//            TODO zip文件没有内容
//            TODO zip文件没有内容
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
//                          不处理其它格式的文件
                    }
                } else {
//                          不处理目录
                }
            }
        }
    }

    private void updateIndexManager(File uploadedLogFile, String fileName) throws Exception {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(uploadedLogFile));
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
//                    不支持的索引类型
                    break;
            }
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } finally {
                    boolean succeed = uploadedLogFile.delete();
                    if (succeed) {

                    } else {
                        throw new Exception("删除上传的日志文件失败");
                    }
                }
            }
        }
    }

    private void restructureLogFile(File uploadedLogFile, String fileName, String fileSuffix) throws Exception {
        String restructureLogFilePath = restructureLogFilePath(fileName, fileSuffix);
        File restructureLogFile = new File(restructureLogFilePath);
        if (restructureLogFile.exists()) {
            boolean succeed = uploadedLogFile.delete();
            if (succeed) {

            } else {
                throw new Exception("恢复日志目录时,替换旧日志文件失败");
            }
        } else {
            Files.createParentDirs(restructureLogFile);
            Files.move(uploadedLogFile, restructureLogFile);
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
     * @param fileName
     * @param obj
     * @throws IOException
     */
    private void backupAndReplace(String fileName, Object obj) throws IOException {
        File oldIndexFile = new File(INDEX_ROOT_DIR + File.separator + fileName + INDEX_SUFFIX);
        if (oldIndexFile.exists()) {
            File bakIndexFile = new File(INDEX_ROOT_DIR + File.separator + fileName + BAK_SUFFIX);
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
