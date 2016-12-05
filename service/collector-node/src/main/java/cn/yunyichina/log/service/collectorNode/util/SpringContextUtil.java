package cn.yunyichina.log.service.collectorNode.util;

import org.springframework.context.ApplicationContext;

/**
 * Created by Jonven on 2016/11/29.
 */
public class SpringContextUtil {

    private static ApplicationContext applicationContext;
    private static boolean hasInit = false;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (!hasInit) {
            SpringContextUtil.applicationContext = applicationContext;
            hasInit = true;
        }
    }

    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }
}
