package org.endeavourhealth.imapi.utility;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Shared default-configured ObjectMapper. ObjectMapper is thread-safe once configured, and reusing one
 * keeps its serializer/deserializer caches warm. Do not reconfigure this instance; create your own
 * mapper if you need non-default settings.
 */
public final class SharedObjectMapper {
  public static final ObjectMapper INSTANCE = new ObjectMapper();

  private SharedObjectMapper() {
  }
}
