package com.rjs.mymovies.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    public WebConfig() {
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/shows/search").setViewName("shows/search");
        registry.addViewController("/admin/adminTemplate").setViewName("admin/adminTemplate");
//        registry.addViewController("/adminTemplate").setViewName("adminTemplate");
        registry.addViewController("/admin/adminUsers").setViewName("admin/adminUsers");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**", "/css/**", "/js/**").addResourceLocations("/webjars/", "classpath:/static/css/", "classpath:/static/js/");
    }
}
