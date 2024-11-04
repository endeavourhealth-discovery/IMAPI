package org.endeavourhealth.imapi.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
  @Bean
  public GroupedOpenApi openApi() {
    return GroupedOpenApi.builder()
      .group("All")
      .pathsToMatch("/api/**")
      .build();
  }

  @Bean
  public GroupedOpenApi fhirApi() {
    return GroupedOpenApi.builder()
      .group("FHIR")
      .pathsToMatch("/api/fhir/**")
      .build();
  }
}
