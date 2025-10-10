package org.endeavourhealth.imapi.config;

import org.endeavourhealth.imapi.interceptor.CasdoorUserLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CasdoorLoginConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    InterceptorRegistration registration = registry.addInterceptor(new CasdoorUserLoginInterceptor());
    registration.addPathPatterns("/**");
    registration.excludePathPatterns("/toLogin", "/login", "callback");
  }
}
