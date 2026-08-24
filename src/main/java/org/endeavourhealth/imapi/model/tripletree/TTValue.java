package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;

import java.io.Serializable;

public interface TTValue extends Serializable {
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

  default TTLiteral asLiteral() {
    return null;
  }

  default TTIriRef asIriRef() {
    return null;
  }

  default TTNode asNode() {
    return null;
  }
}
