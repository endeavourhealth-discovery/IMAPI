package org.endeavourhealth.imapi.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class TokenAuthenticated extends AbstractAuthenticationToken {
  public TokenAuthenticated() {
    super(Collections.emptyList());
    setAuthenticated(true); // mark as authenticated
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null; // or token, or any object you want
  }
}
