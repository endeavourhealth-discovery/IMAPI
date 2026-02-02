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

@SpringBootApplication()
@Configuration
public class ImApiSpringApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(ImApiSpringApplication.class, args);
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
