package com.rjs.mymovies.server.config;

import com.github.dandelion.core.web.DandelionFilter;
import com.github.dandelion.core.web.DandelionServlet;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.thymeleaf.dialect.DandelionDialect;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
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

    // Dandelion config
    @Bean
    public DandelionDialect dandelionDialect() {
        return new DandelionDialect();
    }

    @Bean
    public DataTablesDialect dataTablesDialect() {
        return new DataTablesDialect();
    }

    @Bean
    public Filter dandelionFilter() {
        return new DandelionFilter();
    }

    @Bean
    public ServletRegistrationBean dandelionServletRegistrationBean() {
        return new ServletRegistrationBean(new DandelionServlet(), "/dandelion-assets/*");
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
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/shows/search").setViewName("shows/search");
        registry.addViewController("/admin/adminTemplate").setViewName("admin/adminTemplate");
        registry.addViewController("/admin/adminUsers").setViewName("admin/adminUsers");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**", "/css/**", "/js/**").addResourceLocations("/webjars/", "classpath:/static/css/", "classpath:/static/js/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(byteArrayHttpMessageConverter());
    }
}
