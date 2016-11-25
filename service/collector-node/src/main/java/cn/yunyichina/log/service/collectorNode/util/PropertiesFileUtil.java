package cn.yunyichina.log.service.collectorNode.util;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Jonven on 2016/11/24.
 */
public class PropertiesFileUtil {

    //属性文件的路径
    private String profilepath;
    private Properties props;
    private InputStream is;

    public PropertiesFileUtil(String profilepath){
        this.profilepath = profilepath;
        try {
            props = new Properties();
            is = new FileInputStream(profilepath);
            props.load(is);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getValue(String key){
        return (String)props.get(key);
    }

    /**
     * 获取记录失败文件的集合
     *
     * @return
     */
    public File[] getFilesProperties() {
        File[] failFiles = null;

        try {
            Set<Map.Entry<Object, Object>> entrySet = props.entrySet();
            if (entrySet != null) {
                failFiles = new File[entrySet.size()];
                int count = 0;
                for (Map.Entry<Object, Object> entry : entrySet) {
                    String filePath = (String) entry.getValue();
                    failFiles[count] = new File(filePath);
                    count++;
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return failFiles;
    }

    public Boolean updateFilesProperties(File[] files){

        OutputStream fos = null;
        try {
            fos = new FileOutputStream(profilepath);
            for (File file : files) {
                props.setProperty(file.getName(),file.getPath());
            }
            props.store(fos, "update");
            fos.close();
            is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean clearFilesProperties(){
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(profilepath);
            props.clear();
            props.store(fos,"clear");
            fos.close();
            is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
