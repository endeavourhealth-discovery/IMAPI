package org.endeavourhealth.imapi.statemachine;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.endeavourhealth.imapi.statemachine.StateMachineTransition.transition;
import static org.junit.jupiter.api.Assertions.*;

class UPRNStateMachineTest {

  @Test
  void manualConfig() throws Exception {
    StateMachine sm = new StateMachine(new StateMachineConfigDALMock(), new StateMachineTaskDALMock())
      .config(
        "UPRN",
        Set.of("uploading", "processing", "complete", "downloaded", "failed"),
        Set.of("PROCESS", "FAIL", "COMPLETE", "DOWNLOAD"),
        "uploading",
        Set.of(
          transition("uploading", "PROCESS", "processing"),
          transition("uploading", "FAIL", "failed"),
          transition("processing", "COMPLETE", "complete"),
          transition("complete", "DOWNLOAD", "downloaded")
        )
      );

    testStateMachine(sm);
  }

  @Test
  void loadConfig() throws Exception {
    new StateMachine(new StateMachineConfigDALMock(), new StateMachineTaskDALMock())
      .config(
        "UPRN",
        Set.of("uploading", "processing", "complete", "downloaded", "failed"),
        Set.of("PROCESS", "FAIL", "COMPLETE", "DOWNLOAD"),
        "uploading",
        Set.of(
          transition("uploading", "PROCESS", "processing"),
          transition("uploading", "FAIL", "failed"),
          transition("processing", "COMPLETE", "complete"),
          transition("complete", "DOWNLOAD", "downloaded")
        )
      );

    StateMachine sm = new StateMachine(new StateMachineConfigDALMock(), new StateMachineTaskDALMock())
      .loadConfig("UPRN");

    testStateMachine(sm);
  }

  void testStateMachine(StateMachine sm) throws Exception {
    assertNotNull(sm);
    assertNull(sm.getCurrentState());

    sm.beforeTransition((String source, String event, String target) -> {
      System.out.printf("Transitioning from %s to %s \n", source, target);
      return true;
    });
    sm.afterTransition((String newState, String event, String previousState) -> {
      System.out.printf("Transitioned from %s to %s \n", previousState, newState);
    });

    String task1 = UUID.randomUUID().toString();
    sm.newTask(task1);
    assertEquals("uploading", sm.getCurrentState());
    sm.sendEvent("PROCESS");
    assertEquals("processing", sm.getCurrentState());
    String task2 = UUID.randomUUID().toString();
    sm.newTask(task2);
    assertEquals("uploading", sm.getCurrentState());
    assertEquals(task2, sm.getId());
    sm.loadTask(task1);
    assertEquals(task1, sm.getId());
    assertEquals("processing", sm.getCurrentState());
    sm.sendEvent("COMPLETE");
    assertEquals("complete", sm.getCurrentState());
    try {
      sm.sendEvent("PROCESS");
      fail();
    } catch (Exception e) {
    }
    sm.loadTask(task2);
    Set<String> nextEvents = sm.possibleEvents();
    assertEquals(2, nextEvents.size());
    assertTrue(nextEvents.contains("PROCESS"));
    assertTrue(nextEvents.contains("FAIL"));
  }
}
