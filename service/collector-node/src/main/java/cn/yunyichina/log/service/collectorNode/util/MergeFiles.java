package cn.yunyichina.log.service.collectorNode.util;

import java.io.File;

/**
 * Created by Jonven on 2016/11/24.
 */
public class MergeFiles {

    public static File[] merge(File[] oldFiles,File[] newFiles){
        File[] files = null;
        int oldFilesLength = 0;
        int newFilesLength = 0;
        if (oldFiles!=null){
            oldFilesLength = oldFiles.length;
        }
        if(newFiles!=null){
            newFilesLength = newFiles.length;
        }
        files = new File[oldFilesLength+newFilesLength];
        if (oldFilesLength>0){
            for(int i=0;i<oldFilesLength;i++){
                files[i] = oldFiles[i];
            }
        }
        if (newFilesLength>0){
            int j = oldFilesLength;
            for(int i=0;i<newFilesLength;i++){
                files[j] = newFiles[i];
                j++;
            }
        }
        return files;
    }

}
