package org.endeavourhealth.imapi.statemachine;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.endeavourhealth.imapi.statemachine.StateMachineTransition.transition;
import static org.junit.jupiter.api.Assertions.*;

class ConceptStateMachineTest {

    @Test
    void manualConfig() throws Exception {
        StateMachine sm = new StateMachine(new StateMachineConfigDALTest(), new StateMachineTaskDALTest())
            .config(
            "Concept",
            Set.of("Editing", "Draft", "Released"),
            Set.of("EDIT", "COMPLETE", "APPROVE"),
            "Editing",
            Set.of(
                transition("Editing", "EDIT", "Editing"),
                transition("Editing", "COMPLETE", "Draft"),
                transition("Draft", "EDIT", "Editing"),
                transition("Draft", "APPROVE", "Released"),
                transition("Released", "EDIT", "Editing")
            )
        );

        testStateMachine(sm);
    }


    void testStateMachine(StateMachine sm) throws Exception {
        assertNotNull(sm);
        assertNull(sm.getCurrentState());

        sm.beforeTransition((String source, String event, String target) -> {
            System.out.printf("BEFORE: [%s] --> (%s) --> [%s]\n", source, event, target);
            return true;
        });
        sm.afterTransition((String newState, String event, String previousState) -> {
            System.out.printf("AFTER : [%s] --> (%s) --> [%s]\n", previousState, event, newState);
        });

        String conceptA = "http://endhealth.net/im#ConceptA";
        sm.newTask(conceptA);
        assertEquals("Editing", sm.getCurrentState());
        sm.sendEvent("EDIT");
        assertEquals("Editing", sm.getCurrentState());
        sm.sendEvent("COMPLETE");
        assertEquals("Draft", sm.getCurrentState());

        String conceptB = "http://endhealth.net/im#ConceptB";
        sm.newTask(conceptB);
        assertEquals("Editing", sm.getCurrentState());
        assertEquals(conceptB, sm.getId());

        sm.loadTask(conceptA);
        assertEquals(conceptA, sm.getId());
        assertEquals("Draft", sm.getCurrentState());
        sm.sendEvent("APPROVE");
        assertEquals("Released", sm.getCurrentState());
        try {
            sm.sendEvent("COMPLETE");
            fail();
        } catch (Exception e) {
        }

        sm.loadTask(conceptB);
        Set<String> nextEvents = sm.possibleEvents();
        assertEquals(2, nextEvents.size());
        assertTrue(nextEvents.contains("COMPLETE"));
        assertTrue(nextEvents.contains("EDIT"));
    }
}
