package cn.yunyichina.log.service.collectorNode.util;

import org.springframework.context.ApplicationContext;

/**
 * Created by Jonven on 2016/11/29.
 */
public class SpringContextUtil {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }
}
