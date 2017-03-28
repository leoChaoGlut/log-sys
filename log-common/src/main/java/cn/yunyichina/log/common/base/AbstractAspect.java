package cn.yunyichina.log.common.base;

import org.aspectj.lang.JoinPoint;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 9/21/16 10:39 AM
 * @Description:
 */
public abstract class AbstractAspect {

    protected String tips(String msg) {
        return "  ================================  " + msg + "  ===  ";
    }

    protected String getTargetClassName(JoinPoint jp) {
        return jp.getTarget().getClass().getName();//如果AOP用的是CGlib,用getClass获取到cglib动态生成的内部类，需要再加一个superClass才能获取到本身的类
    }

    protected String getTargetMethodName(JoinPoint jp) {
        return jp.getSignature().getName();
    }

    protected String getTargetInfo(JoinPoint jp) {
        return getTargetClassName(jp) + "." + getTargetMethodName(jp);
    }
}
