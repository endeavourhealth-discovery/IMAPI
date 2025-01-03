package org.endeavourhealth.imapi.utility;

import java.util.Optional;

public class EnvHelper {
  public static boolean isPublicMode() {
    return Optional.ofNullable(System.getenv("HOSTING_MODE")).orElse("").equals("public");
  }
}
