package org.endeavourhealth.imapi.stateMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StateMachine<S, E> {
    public interface BeforeTransition<S, E>{
        boolean execute(S currentState, E event, S newState);
    };
    public interface AfterTransitions<S, E>{
        void execute(S newState, E event, S previousState);
    };
    public BeforeTransition<S, E> beforeTransition = (S currentState, E event, S newState) -> {
        return true;
    };
    public AfterTransitions<S, E> afterTransitions = (S newState, E event, S previousState) -> {};
    protected S initialState;
    private S currentState;
    protected Map<S, Map<E, S>> config = new HashMap<>();

    public S getCurrentState() {
        return currentState;
    }

    public void start() {
        currentState = initialState;
    }

    public S sendEvent(E event) {
        if (currentState == null) {
            throw new IllegalStateException("Current state invalid");
        }

        Map<E, S> transitions = config.get(currentState);

        if (transitions == null) {
            throw  new IllegalStateException("No transitions configured for current state");
        }

        S nextState = transitions.get(event);

        if (nextState == null) {
            throw new IllegalStateException("No valid next state for event");
        }
        if (beforeTransition.execute(currentState, event, nextState)) {
            S previousState = currentState;
            currentState = nextState;
            afterTransitions.execute(currentState, event, previousState);
        }

        return currentState;
    }

    public Set<E> possibleEvents() {
        Map<E, S> transitions = config.get(currentState);
        return transitions.keySet();
    }
}
