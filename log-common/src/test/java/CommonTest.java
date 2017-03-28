import cn.yunyichina.log.common.entity.do_.KvTagDO;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

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

    }

    @Test
    public void test() {
        long begin = System.nanoTime();
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        System.out.println(BigDecimal.valueOf(System.nanoTime() - begin, 9));
    }
}
