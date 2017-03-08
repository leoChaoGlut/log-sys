import cn.yunyichina.log.common.entity.do_.KvTagDO;
import org.junit.Test;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/1 11:06
 * @Description:
 */
public class CommonTest {

    @Test
    public void commonTest() {
        KvTagDO kvTagDO = new KvTagDO()
                .setKey("")
                .setId(1)
                .setKeyOffset(1);
        System.out.println(kvTagDO.toString());

    }
}
