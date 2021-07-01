package org.endeavourhealth.imapi.uprn;

import org.endeavourhealth.imapi.stateMachine.StateMachine;
import org.endeavourhealth.imapi.stateMachine.StateMachineFactory;
import org.endeavourhealth.imapi.stateMachine.TestEvents;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StateMachineTest {

    @Test
    void testFactory() {
        StateMachine<String, TestEvents, UUID> sm = StateMachineFactory.<String,TestEvents,UUID>configure(
                Set.of("uploading", "processing", "complete", "downloaded", "failed"),
                Set.of(TestEvents.process, TestEvents.fail, TestEvents.complete)
        )
                .setInitialState("uploading")
                .addTransition("uploading", TestEvents.process, "processing")
                .addTransition("uploading", TestEvents.fail, "failed")
                .addTransition("processing", TestEvents.complete, "complete")
                .setPersister(new TestPersister())
                .build();
        assertNotNull(sm);
        sm.beforeTransition = (String source, TestEvents event, String target) -> {
            System.out.printf("Transitioning from %s to %s \n", source, target);
            return true;
        };
        sm.afterTransitions = (String newState, TestEvents event, String previousState) -> {
            System.out.printf("Transitioned from %s to %s \n", previousState, newState);
        };
        assertNull(sm.getCurrentState());
        UUID task1 = UUID.randomUUID();
        sm.start(task1);
        assertEquals("uploading", sm.getCurrentState());
        sm.sendEvent(TestEvents.process);
        assertEquals("processing", sm.getCurrentState());
        UUID task2 = UUID.randomUUID();
        sm.start(task2);
        assertEquals("uploading",sm.getCurrentState());
        assertEquals(task2, sm.getId());
        sm.load(task1);
        assertEquals(task1, sm.getId());
        assertEquals("processing",sm.getCurrentState());
        sm.sendEvent(TestEvents.complete);
        assertEquals("complete",sm.getCurrentState());
        try {
            sm.sendEvent(TestEvents.process);
            fail();
        }
        catch (Exception e) {}

    }
}