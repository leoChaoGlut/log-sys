package cn.yunyichina.log.common.util;

import com.google.common.io.Files;

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
            //如果没有文件、则创建新的文件
            File file = new File(profilepath);
            if (!file.exists()){
                Files.createParentDirs(file);
                file.createNewFile();
            }
            props = new Properties();
            is = new FileInputStream(profilepath);
            props.load(is);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 根据key值获取value值
     * @param key
     * @return
     */
    public String getValue(String key){
        return (String)props.get(key);
    }

    /**
     * 根据key值设置value值，没有对应的key值则插入一对<key，value>
     * @param key
     * @param value
     * @return
     */
    public Boolean setValue(String key,String value){

        OutputStream fos = null;
        try {
            fos = new FileOutputStream(profilepath);
            props.setProperty(key,value);
            props.store(fos, "update");
            fos.flush();
            fos.close();
            is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    /**
     * 更新记录失败文件的集合，没有对应的key值则插入对应的文件路径value值
     * @param files
     * @return
     */
    public Boolean updateFilesProperties(File[] files){

        OutputStream fos = null;
        try {
            fos = new FileOutputStream(profilepath);
            for (File file : files) {
                if(file.getName().contains(".log")) {//仅仅记录.log后缀的日志，不记录其他
                    props.setProperty(file.getName(), file.getPath());
                }
            }
            props.store(fos, "update");
            fos.flush();
            fos.close();
            is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 清除记录失败文件的集合
     * @return
     */
    public Boolean clearFilesProperties(){
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(profilepath);
            props.clear();
            props.store(fos,"clear");
            fos.flush();
            fos.close();
            is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
