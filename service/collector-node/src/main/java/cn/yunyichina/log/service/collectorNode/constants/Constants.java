package cn.yunyichina.log.service.collectorNode.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Jonven on 2016/11/29.
 */
@Configuration
//@PropertySource("classpath:config.properties")
public class Constants {

//    public static String cursorKey = "last_end_time";
//    public static String cursorPropPath = "E:\\\\zTest\\\\cursor.properties";
//    public static String logPath = "E:\\\\zTest\\\\testLog1";

    @Value("${constants.cursorKey}")
    public String cursorKey;
    @Value("${constants.filePath.cursorPropPath}")
    public String cursorPropPath;
    @Value("${constants.filePath.logPath}")
    public String logPath;
}
