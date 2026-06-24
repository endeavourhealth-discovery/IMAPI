package org.endeavourhealth.imapi.utility;

import org.endeavourhealth.imapi.model.iml.EntityExtensionKt;
import org.endeavourhealth.interfacemanager.model.Entity;
import org.junit.jupiter.api.Test;

public class OpenApiGenerator {
  @Test
  public void Entity() {
    Entity entity = new Entity();
    EntityExtensionKt.test(entity, "hello", "world");
  }
}
