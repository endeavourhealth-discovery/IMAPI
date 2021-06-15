package org.endeavourhealth.imapi.uprn;

import org.endeavourhealth.imapi.stateMachine.StateMachine;
import org.endeavourhealth.imapi.stateMachine.StateMachineFactory;
import org.endeavourhealth.imapi.stateMachine.TestEvents;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StateMachineTest {

    @Test
    void testFactory() {
        StateMachine<String, TestEvents> sm = StateMachineFactory.configure(
                Set.of("uploading", "processing", "complete", "downloaded", "failed"),
                Set.of(TestEvents.process, TestEvents.fail)
        )
                .setInitialState("uploading")
                .addTransition("uploading", TestEvents.process, "processing")
                .addTransition("uploading", TestEvents.fail, "failed")
                .addTransition("processing", TestEvents.complete, "complete")
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
        sm.start();
        assertEquals("uploading", sm.getCurrentState());
        sm.sendEvent(TestEvents.process);
        assertEquals("processing", sm.getCurrentState());
        try {
            sm.sendEvent(TestEvents.process);
            fail();
        }
        catch (Exception e) {}

    }
}