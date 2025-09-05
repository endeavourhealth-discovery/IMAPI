package org.endeavourhealth.imapi.model.postRequestPrimatives;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UUIDBody {
  private UUID value;

  public UUIDBody(UUID value) {
    this.value = value;
  }

  public UUIDBody() {
  }
}
