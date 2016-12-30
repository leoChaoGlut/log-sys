package cn.yunyichina.log.service.collector.constants;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/6 15:11
 * @Description:
 */
public interface Key {

    //最近一次上传日志成功的时间
    String LAST_MODIFY_TIME = "lastModifyTime";
    String UPLOAD_FAILED_FILE_LIST = "uploadFailedFileList";
    String CONTEXT_INFO_INDEX_FILE_NAME = "context.index";
    String KEYWORD_INDEX_FILE_NAME = "keyword.index";
    String KEY_VALUE_INDEX_FILE_NAME = "keyValue.index";
    String ZIP_FILE_NAME = "zip.zip";
    String KV_TAG_SET = "kvTagSet";
    String KEYWORD_SET = "keywordSet";
    String CONTEXT_COUNT = "contextCount";

}
