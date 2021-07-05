package org.endeavourhealth.imapi.statemachine;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StateMachineTest {

    @Test
    void testFactory() {
        StateMachine<String, TestEvents, UUID> sm = StateMachineFactory.<String,TestEvents,UUID>configure(
                Set.of("uploading", "processing", "complete", "downloaded", "failed"),
                Set.of(TestEvents.PROCESS, TestEvents.FAIL, TestEvents.COMPLETE, TestEvents.DOWNLOAD)
        )
                .setInitialState("uploading")
                .addTransition("uploading", TestEvents.PROCESS, "processing")
                .addTransition("uploading", TestEvents.FAIL, "failed")
                .addTransition("processing", TestEvents.COMPLETE, "complete")
                .addTransition("complete",TestEvents.DOWNLOAD, "downloaded")
                .setPersister(new TestPersister())
                .build();
        assertNotNull(sm);
        sm.beforeTransitions((String source, TestEvents event, String target) -> {
            System.out.printf("Transitioning from %s to %s \n", source, target);
            return true;
        });
        sm.afterTransitions((String newState, TestEvents event, String previousState) -> {
            System.out.printf("Transitioned from %s to %s \n", previousState, newState);
        });
        assertNull(sm.getCurrentState());
        UUID task1 = UUID.randomUUID();
        sm.start(task1);
        assertEquals("uploading", sm.getCurrentState());
        sm.sendEvent(TestEvents.PROCESS);
        assertEquals("processing", sm.getCurrentState());
        UUID task2 = UUID.randomUUID();
        sm.start(task2);
        assertEquals("uploading",sm.getCurrentState());
        assertEquals(task2, sm.getId());
        sm.load(task1);
        assertEquals(task1, sm.getId());
        assertEquals("processing",sm.getCurrentState());
        sm.sendEvent(TestEvents.COMPLETE);
        assertEquals("complete",sm.getCurrentState());
        try {
            sm.sendEvent(TestEvents.PROCESS);
            fail();
        }
        catch (Exception e) {}
        sm.load(task2);
        Set<TestEvents> nextEvents = sm.possibleEvents();
        assertEquals(2,nextEvents.size());
        assertTrue(nextEvents.contains(TestEvents.PROCESS));
        assertTrue(nextEvents.contains(TestEvents.FAIL));

    }
}
