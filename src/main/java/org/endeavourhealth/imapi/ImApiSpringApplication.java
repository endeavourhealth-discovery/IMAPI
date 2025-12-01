package org.endeavourhealth.imapi;

import org.endeavourhealth.imapi.ai.IMAIService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication()
@Configuration
public class ImApiSpringApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(ImApiSpringApplication.class, args);
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOriginPattern("*");
    config.setAllowedMethods(Arrays.asList("POST", "OPTIONS", "GET", "DELETE", "PUT"));
    config.setAllowedHeaders(Arrays.asList("X-Requested-From", "Origin", "Content-Type", "Accept", "Authorization"));
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Bean
  public ToolCallbackProvider weatherTools(IMAIService IMAIService) {
    return MethodToolCallbackProvider.builder().toolObjects(IMAIService).build();
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(ImApiSpringApplication.class);
  }
}
