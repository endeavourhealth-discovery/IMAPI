package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.endeavourhealth.imapi.json.IMDMDeserializer;
import org.endeavourhealth.imapi.json.IMDMSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@JsonSerialize(using = IMDMSerializer.class)
@JsonDeserialize(using = IMDMDeserializer.class)
public class IMDMBase<B> {
  private Map<String, Object> properties = new HashMap<>();
  private String type;
  private UUID id;

  public IMDMBase(String type, UUID id) {
    this.type = type;
    this.id = id;
  }

  public <T> T getProperty(String name) {
    return (T) properties.get(name);
  }

  public B setProperty(String propertyName, Object value) {
    this.properties.put(propertyName, value);
    return (B) this;
  }
}
