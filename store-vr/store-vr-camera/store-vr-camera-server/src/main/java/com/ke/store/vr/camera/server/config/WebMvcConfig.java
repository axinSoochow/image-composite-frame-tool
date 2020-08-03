package com.ke.store.vr.camera.server.config;

import com.ke.store.vr.camera.server.web.filter.DemoFilter;
import com.ke.store.vr.camera.server.web.interceptor.DemoInterceptor;
import com.ke.store.vr.common.config.enumm.EnumConvertHandler;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.concurrent.TimeUnit;

/**
 * WebMVC配置，你可以集中在这里配置拦截器、过滤器、静态资源缓存等
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 将所有/static/** 访问都映射到classpath:/static/ 目录下
    CacheControl cc = CacheControl.maxAge(30, TimeUnit.HOURS).mustRevalidate();
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/")
        .setCacheControl(cc);
  }


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 拦截器的配置
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
      registry.addConverterFactory(new EnumConvertHandler());
  }

  @Bean
  public DemoInterceptor demoInterceptor() {
    return new DemoInterceptor();
  }

  @Bean
  public FilterRegistrationBean<Filter> yourFilterBean() {
    FilterRegistrationBean<Filter> filterBean = new FilterRegistrationBean<>();
    Filter demoFilter = new DemoFilter();
    filterBean.setFilter(demoFilter);
    // 适用那些路径
    filterBean.addUrlPatterns("/*");
    return filterBean;
  }
}
