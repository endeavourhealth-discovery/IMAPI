package org.endeavourhealth.imapi.statemachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateMachineConfig {
  private String type;
  private String initialState;
  private final Set<String> validStates = new HashSet<>();
  private final Set<String> validEvents = new HashSet<>();
  private Map<String, HashMap<String, String>> transitions = new HashMap<>();

  public String getType() {
    return type;
  }

  public StateMachineConfig setType(String type) {
    this.type = type;
    return this;
  }

  public String getInitialState() {
    return initialState;
  }

  public StateMachineConfig setInitialState(String initialState) {
    this.initialState = initialState;
    return this;
  }

  public Set<String> getValidStates() {
    return validStates;
  }

  public Set<String> getValidEvents() {
    return validEvents;
  }

  public StateMachineConfig addTransition(String sourceState, String event, String targetState) {
    if (!validStates.contains(sourceState)) {
      throw new IllegalStateException("Invalid source state");
    }
    if (!validEvents.contains(event)) {
      throw new IllegalStateException("Invalid event");
    }
    if (!validStates.contains(targetState)) {
      throw new IllegalStateException("Invalid target state");
    }
    Map<String, String> eventTargets = this.transitions.computeIfAbsent(sourceState, k -> new HashMap<>());
    if (eventTargets.containsKey(event)) {
      throw new IllegalStateException("Event already configured");
    }
    eventTargets.put(event, targetState);
    return this;
  }

  public Map<String, HashMap<String, String>> getTransitions() {
    return transitions;
  }

  public StateMachineConfig setTransitions(Map<String, HashMap<String, String>> transitions) {
    this.transitions = transitions;
    return this;
  }

  public Map<String, String> getEventTransitions(String sourceState) {
    return this.transitions.get(sourceState);
  }
}
