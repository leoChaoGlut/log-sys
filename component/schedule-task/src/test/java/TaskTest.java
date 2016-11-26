import cn.yunyichina.log.component.scheduleTask.task.LogScheduleTask;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jonven on 2016/11/25.
 */
public class TaskTest {

    @Test
    public void test(){
        LogScheduleTask task = new LogScheduleTask();
        task.getLog();
    }

    @Test
    public void testDirs(){
//        String last_end_time = new PropertiesFileUtil("E:\\zTest\\cursor.properties").getValue("last_end_time");
//        System.err.println(last_end_time);
        File file = new File("E:\\zTest\\aa\\bb\\cc\\cursor.properties");
        if (!file.exists()){
            try {
                Files.createParentDirs(file);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
