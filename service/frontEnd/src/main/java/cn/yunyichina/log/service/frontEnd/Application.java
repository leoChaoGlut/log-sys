package cn.yunyichina.log.service.frontEnd;

import cn.yunyichina.log.service.frontEnd.constants.CacheName;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/30 9:35
 * @Description:
 */
@EnableWebMvc
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.service.frontEnd")
@EnableEurekaClient
@EnableCaching
@MapperScan(basePackages = "cn.yunyichina.log.service.frontEnd.mapper")
public class Application {

    @Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
        return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
            @Override
            public void customize(ConcurrentMapCacheManager cacheManager) {
                List<String> cacheNameList = new ArrayList<>();
                cacheNameList.add(CacheName.OPTION);

                cacheManager.setCacheNames(cacheNameList);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
