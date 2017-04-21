import org.junit.Test;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/14 10:57
 * @Description:
 */
public class RedisProxyTest {
    @Test
    public void test0() {
        String[] strs = {"a", "b", "c"};
        test1(strs);
    }

    private void test1(String... strs) {
        for (String str : strs) {
            System.out.println(str);
        }
    }
}
