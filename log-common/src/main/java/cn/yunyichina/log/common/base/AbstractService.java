package cn.yunyichina.log.common.base;

import java.util.Objects;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/8 17:26
 * @Description:
 */
public abstract class AbstractService {

    /**
     * 检查单条插入或更新操作是否成功
     *
     * @param count
     * @param exceptionMsg
     * @throws Exception
     */
    public void judgeAnOperation(int count, String exceptionMsg) {
        if (Objects.equals(count, 1)) {
//            修改操作成功
        } else {
            throw new RuntimeException(exceptionMsg);
        }
    }

    public void judgeOperations(int expectedCount, int count, String exceptionMsg) {
        if (Objects.equals(count, expectedCount)) {
//            修改操作成功
        } else {
            throw new RuntimeException(exceptionMsg);
        }
    }

}
