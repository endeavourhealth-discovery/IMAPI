package org.endeavourhealth.imapi.security;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Casdoor connection settings, read from the environment.
 *
 * @param url              Base URL of Casdoor (https), e.g. https://auth.example.org
 * @param issuer           Expected `iss` of access tokens; defaults to {@code url}
 * @param organisation     Casdoor organisation that owns the users
 * @param clientId         Client id of the Casdoor application IMAPI uses for management API calls
 * @param clientSecret     Client secret of that application
 * @param enforcerId       Casdoor enforcer (owner/name) consulted for authorisation
 * @param allowedClientIds Client ids (token audiences) of the applications whose tokens IMAPI accepts
 * @param jwsAlgorithm     Algorithm access tokens are signed with
 */
public record CasdoorSettings(
  String url,
  String issuer,
  String organisation,
  String clientId,
  String clientSecret,
  String enforcerId,
  List<String> allowedClientIds,
  String jwsAlgorithm
) {
  public static CasdoorSettings fromEnvironment() {
    return from(System.getenv());
  }

  static CasdoorSettings from(Map<String, String> env) {
    String url = stripTrailingSlash(env.get("CASDOOR_URL"));
    return new CasdoorSettings(
      url,
      Optional.ofNullable(stripTrailingSlash(env.get("CASDOOR_ISSUER"))).orElse(url),
      env.get("CASDOOR_ORGANISATION_NAME"),
      env.get("CASDOOR_CLIENT_ID"),
      env.get("CASDOOR_CLIENT_SECRET"),
      env.get("CASDOOR_ENFORCER_ID"),
      split(env.get("CASDOOR_ALLOWED_CLIENT_IDS")),
      Optional.ofNullable(env.get("CASDOOR_JWS_ALGORITHM")).filter(s -> !s.isBlank()).orElse("RS256")
    );
  }

  /** @throws IllegalStateException naming the first missing setting */
  public CasdoorSettings requireConfigured() {
    require("CASDOOR_URL", url);
    require("CASDOOR_ORGANISATION_NAME", organisation);
    require("CASDOOR_CLIENT_ID", clientId);
    require("CASDOOR_CLIENT_SECRET", clientSecret);
    require("CASDOOR_ENFORCER_ID", enforcerId);
    if (allowedClientIds.isEmpty()) throw new IllegalStateException("CASDOOR_ALLOWED_CLIENT_IDS is not set");
    return this;
  }

  public String jwkSetUri() {
    return url + "/.well-known/jwks";
  }

  public String profileUrl() {
    return url + "/account";
  }

  private static void require(String name, String value) {
    if (value == null || value.isBlank()) throw new IllegalStateException(name + " is not set");
  }

  private static String stripTrailingSlash(String value) {
    if (value == null || value.isBlank()) return null;
    return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
  }

  private static List<String> split(String value) {
    if (value == null || value.isBlank()) return List.of();
    return Arrays.stream(value.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
  }
}
