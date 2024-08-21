package org.endeavourhealth.imapi.statemachine;

public class StateMachineTransition {
  public static StateMachineTransition transition(String source, String event, String target) {
    return new StateMachineTransition(source, event, target);
  }

  private String source;
  private String event;
  private String target;

  public StateMachineTransition(String source, String event, String target) {
    this.source = source;
    this.event = event;
    this.target = target;
  }

  public String getSource() {
    return source;
  }

  public StateMachineTransition setSource(String source) {
    this.source = source;
    return this;
  }

  public String getEvent() {
    return event;
  }

  public StateMachineTransition setEvent(String event) {
    this.event = event;
    return this;
  }

  public String getTarget() {
    return target;
  }

  public StateMachineTransition setTarget(String target) {
    this.target = target;
    return this;
  }
}
