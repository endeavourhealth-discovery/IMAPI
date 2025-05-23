package org.endeavourhealth.imapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class SpringDocConfig {

  @Bean
  public OpenAPI defineOpenApi() {
    Server serverDev = new Server();
    serverDev.setUrl("https://dev.endhealth.co.uk/IMAPI");
    serverDev.setDescription("Development");

    Server serverLive = new Server();
    serverLive.setUrl("https://dev.endhealth.co.uk/IMAPI");
    serverLive.setDescription("Development");

    Contact contact = new Contact();
    contact.setName("Support");
    contact.setEmail("info@voror.co.uk");

    Info information = new Info()
      .title("Information Model Manager API")
      .version("1.0")
      .description("This API exposes endpoints to Information Model Manager API.")
      .contact(contact);
    return new OpenAPI().info(information).servers(List.of(serverDev, serverLive));
  }

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
