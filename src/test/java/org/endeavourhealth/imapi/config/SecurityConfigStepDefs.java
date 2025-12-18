package org.endeavourhealth.imapi.config;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SecurityConfigStepDefs {
  final SecurityConfig securityConfig = new SecurityConfig();
  final List<String> permittedEndpoints = new ArrayList<>();
  AutoCloseable mocks;
  @Mock
  AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry reqMatcher;
  @Mock
  AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorisedUrl;

  @Before
  public void initMocks() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @After
  public void closeMocks() throws Exception {
    mocks.close();
  }

  @When("the server starts up")
  public void theIsPublicAPIIsCalled() {

    when(reqMatcher.anyRequest()).thenReturn(authorisedUrl);
    when(reqMatcher.requestMatchers(any(HttpMethod.class), any(String.class)))
      .thenAnswer(invocation -> {
          permittedEndpoints.add(invocation.getArgument(1));
          return authorisedUrl;
        }
      );
    when(reqMatcher.requestMatchers(any(HttpMethod.class)))
      .thenAnswer(invocation -> authorisedUrl
      );
    when(authorisedUrl.permitAll()).thenReturn(reqMatcher);

    securityConfig.setRequestPermissions(reqMatcher);
  }

  @Then("the is isPublic endpoint will be available")
  public void theIsPublicEndpointWillBeAvailable() {
    assertTrue(permittedEndpoints.contains("/api/*/public/**"));
  }

  @Then("protected endpoints will not be available")
  public void protectedEndpointsWillNotBeAvailable() {
    assertFalse(permittedEndpoints.contains("/api/**/public/**"));
  }

  @Then("protected endpoints will be available")
  public void protectedEndpointsWillBeAvailable() {
    assertTrue(permittedEndpoints.contains("/api/*/private/**"));
  }
}
