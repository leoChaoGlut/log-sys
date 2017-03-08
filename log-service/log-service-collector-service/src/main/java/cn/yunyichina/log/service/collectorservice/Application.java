package cn.yunyichina.log.service.collectorservice;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.google.common.base.Charsets;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 10:45
 * @Description:
 */
@EnableWebMvc
@EnableEurekaClient
@MapperScan(basePackages = "cn.yunyichina.log.service.collectorservice.mapper")
@SpringBootApplication(scanBasePackages = "cn.yunyichina.log.service.collectorservice")
public class Application extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter4 fastJsonConverter = new FastJsonHttpMessageConverter4();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charsets.UTF_8);
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm");
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);

        fastJsonConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON_UTF8
        ));

        converters.add(fastJsonConverter);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
