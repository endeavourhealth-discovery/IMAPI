package org.endeavourhealth.imapi.security;

import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.function.Supplier;

/**
 * Validates Casdoor access tokens (signature via Casdoor's JWKS, issuer, expiry, audience). Built on first use so the application can
 * start, and serve public endpoints, without Casdoor being configured.
 */
public class CasdoorJwtDecoder implements JwtDecoder {
  private final Supplier<CasdoorSettings> settings;
  private volatile NimbusJwtDecoder delegate;

  public CasdoorJwtDecoder() {
    this(CasdoorSettings::fromEnvironment);
  }

  public CasdoorJwtDecoder(Supplier<CasdoorSettings> settings) {
    this.settings = settings;
  }

  @Override
  public Jwt decode(String token) throws JwtException {
    return delegate().decode(token);
  }

  private NimbusJwtDecoder delegate() {
    if (delegate == null) {
      synchronized (this) {
        if (delegate == null) delegate = build(settings.get().requireConfigured());
      }
    }
    return delegate;
  }

  static NimbusJwtDecoder build(CasdoorSettings settings) {
    NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(settings.jwkSetUri())
      .jwsAlgorithm(SignatureAlgorithm.from(settings.jwsAlgorithm()))
      .build();
    decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
      JwtValidators.createDefaultWithIssuer(settings.issuer()),
      new CasdoorTokenValidator(settings.allowedClientIds())
    ));
    return decoder;
  }
}
