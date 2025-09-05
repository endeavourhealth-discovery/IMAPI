package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.dataaccess.UserRepository;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class RequestObjectService {
  private final UserRepository userRepository = new UserRepository();

  public String getRequestAgentName(HttpServletRequest request) throws JsonProcessingException {
    String token = request.getHeader("Authorization");
    return getPropertyValueFromJwt("cognito:username", token);
  }

  public String getRequestAgentId(HttpServletRequest request) throws JsonProcessingException {
    String token = request.getHeader("Authorization");
    return getPropertyValueFromJwt("sub", token);
  }

  public UUID getRequestAgentIdAsUUID(HttpServletRequest request) throws JsonProcessingException {
    return UUID.fromString(getRequestAgentId(request));
  }

  private String getPropertyValueFromJwt(String propertyValue, String jwt) throws JsonProcessingException {
    if (null == jwt || jwt.isEmpty()) {
      throw new IllegalArgumentException("jwt is null or empty");
    }
    String token = jwt.replace("Bearer ", "");
    String[] chunks = token.split("\\.");
    Base64.Decoder decoder = Base64.getUrlDecoder();
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      JsonNode payload = om.readTree(new String(decoder.decode(chunks[1])));
      return payload.get(propertyValue).asText();
    }
  }

  public String getUserDraftGraph(HttpServletRequest request) throws JsonProcessingException {
    String userId = getRequestAgentId(request);
    return IM.DOMAIN + "draft/" + userId + "#";
  }

  public List<Graph> getUserGraphs(HttpServletRequest request) throws JsonProcessingException {
    try {
      String userId = getRequestAgentId(request);
      return userRepository.getUserGraphs(userId);
    } catch (IllegalArgumentException e) {
      return List.of(Graph.IM);
    }
  }

  public List<Graph> getUserGraphs(String userId) throws JsonProcessingException {
    try {
      return userRepository.getUserGraphs(userId);
    } catch (IllegalArgumentException e) {
      return List.of(Graph.IM);
    }
  }

}
