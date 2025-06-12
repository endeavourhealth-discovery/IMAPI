package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Base64;

public class RequestObjectService {


  public String getRequestAgentName(HttpServletRequest request) throws JsonProcessingException {
    String token = request.getHeader("Authorization");
    return getPropertyValueFromJwt("cognito:username", token);
  }

  public String getRequestAgentId(HttpServletRequest request) throws JsonProcessingException {
    String token = request.getHeader("Authorization");
    return getPropertyValueFromJwt("sub", token);
  }

  private String getPropertyValueFromJwt(String propertyValue, String jwt) throws JsonProcessingException {
    String token = jwt.replace("Bearer ", "");
    String[] chunks = token.split("\\.");
    Base64.Decoder decoder = Base64.getUrlDecoder();
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      JsonNode payload = om.readTree(new String(decoder.decode(chunks[1])));
      return payload.get(propertyValue).asText();
    }
  }

  public String getUserGraph(HttpServletRequest request) throws JsonProcessingException {
    String userId = getRequestAgentId(request);
    return IM.DOMAIN + "draft/" + userId + "#";
  }
}
