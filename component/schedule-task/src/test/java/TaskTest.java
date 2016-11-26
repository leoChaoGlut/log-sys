import cn.yunyichina.log.component.scheduleTask.task.LogScheduleTask;
import org.junit.Test;

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
    public void testProperties(){
//        String last_end_time = new PropertiesFileUtil("E:\\zTest\\cursor.properties").getValue("last_end_time");
//        System.err.println(last_end_time);
    }

}
