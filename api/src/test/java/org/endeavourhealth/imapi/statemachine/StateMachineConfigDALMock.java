package org.endeavourhealth.imapi.statemachine;

import java.util.HashMap;
import java.util.Map;

public class StateMachineConfigDALMock implements StateMachineConfigDAL {
  private static Map<String, StateMachineConfig> db = new HashMap<>();

  public void saveConfig(String name, StateMachineConfig config) {
    db.put(name, config);
  }

  public StateMachineConfig loadConfig(String name) {
    return db.get(name);
  }
}
