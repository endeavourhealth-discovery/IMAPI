package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

public class RequestObjectService {

    private final ObjectMapper mapper = new ObjectMapper();

    public String getRequestAgentIri(HttpServletRequest request) throws JsonProcessingException {
        String token = request.getHeader("Authorization");
        String agent = getPropertyValueFromJwt("cognito:username", token);
        return "http://agent.endhealth.org#" + agent;
    }

    private String getPropertyValueFromJwt(String propertyValue, String jwt) throws JsonProcessingException {
        String token = jwt.replace("Bearer ", "");
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        JsonNode payload = mapper.readTree(new String(decoder.decode(chunks[1])));
        return payload.get(propertyValue).asText();
    }

}
