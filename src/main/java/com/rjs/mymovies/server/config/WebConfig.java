package com.rjs.mymovies.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    private List<MediaType> supportedMediaFormats = new ArrayList<>();

    public WebConfig() {
        supportedMediaFormats.add(MediaType.IMAGE_JPEG);
        supportedMediaFormats.add(MediaType.IMAGE_PNG);
//	    supportedMediaFormats.add(MediaType.APPLICATION_OCTET_STREAM);
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        arrayHttpMessageConverter.setSupportedMediaTypes(supportedMediaFormats);

        return arrayHttpMessageConverter;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/user/login").setViewName("user/login");
        registry.addViewController("/user/signup").setViewName("user/signup");
        registry.addViewController("/shows/search").setViewName("shows/search");
        registry.addViewController("/admin/edit-show").setViewName("admin/edit-show");
        registry.addViewController("/admin/adminTemplate").setViewName("admin/adminTemplate");
        registry.addViewController("/admin/adminUsers").setViewName("admin/adminUsers");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**", "/css/**", "/img/**", "/js/**").addResourceLocations("classpath:/META-INF/resources/webjars/", "classpath:/static/css/", "classpath:/static/img/", "classpath:/static/js/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(byteArrayHttpMessageConverter());
    }
}
