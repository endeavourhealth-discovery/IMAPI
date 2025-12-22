package org.endeavourhealth.imapi.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.casbin.casdoor.config.CasdoorConfiguration;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CasdoorConfig {
  private String certificate;
  @Getter
  private CasdoorConfiguration casdoorConfiguration = new CasdoorConfiguration();

  public CasdoorConfig() {
    casdoorConfiguration = new CasdoorConfiguration();
    String applicationName = System.getenv("CASDOOR_APPLICATION_NAME");
    casdoorConfiguration.setApplicationName(applicationName);
    String clientId = System.getenv("CASDOOR_CLIENT_ID");
    casdoorConfiguration.setClientId(clientId);
    String endpoint = System.getenv("CASDOOR_ENDPOINT");
    casdoorConfiguration.setEndpoint(endpoint);
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    try (InputStream is = classloader.getResourceAsStream("casdoor-cert.txt")) {
      assert is != null;
      this.certificate = new String(is.readAllBytes());
    } catch (IOException e) {
      log.error("Failed to read casdoor certificate");
    }
    casdoorConfiguration.setCertificate(certificate);
    String clientSecret = System.getenv("CASDOOR_CLIENT_SECRET");
    casdoorConfiguration.setClientSecret(clientSecret);
    String organisationName = System.getenv("CASDOOR_ORGANISATION_NAME");
    casdoorConfiguration.setOrganizationName(organisationName);
  }
}
