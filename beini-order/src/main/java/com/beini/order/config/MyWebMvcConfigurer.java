package com.beini.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/**
 * 配置信息
 * @author lb_chen
 *
 */
@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter  {
    /**
     * 设置静态资源的访问路径信息
     */
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}
