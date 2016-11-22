package cn.yunyichina.log.service.collectionNode.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/14 18:25
 * @Description:
 */
@Configuration
@EnableWebMvc
@SpringBootApplication(scanBasePackages = {"cn.yunyichina.log.collectionNode"})
public class SpringConfig {

}
