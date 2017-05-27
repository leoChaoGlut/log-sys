package cn.yunyichina.log.service.tracer.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/21 11:16
 * @Description:
 */
@Configuration
@MapperScan(basePackages = "cn.yunyichina.log.service.tracer.mapper")
public class SpringConfig {
}
