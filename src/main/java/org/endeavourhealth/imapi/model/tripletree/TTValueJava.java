package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.io.Serializable;

public interface TTValueJava extends Serializable {
  @JsonIgnore
  default boolean isLiteral() {
    return false;
  }

  @JsonIgnore
  default boolean isIriRef() {
    return false;
  }

  @JsonIgnore
  default boolean isNode() {
    return false;
  }

  default TTLiteralJava asLiteral() {
    return null;
  }

  default TTIriRef asIriRef() {
    return null;
  }

  default TTNodeJava asNode() {
    return null;
  }
}
