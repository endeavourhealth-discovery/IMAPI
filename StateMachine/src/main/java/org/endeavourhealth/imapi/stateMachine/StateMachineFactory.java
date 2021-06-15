package org.endeavourhealth.imapi.stateMachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateMachineFactory<S, E> {
    public static <S, E> StateMachineFactory<S, E> configure (Set<S> states, Set<E> events) {
        return new StateMachineFactory<>(states, events);
    };
    private StateMachine<S, E> stateMachine = new StateMachine<>();
    private Set<S> validStates = new HashSet<>();
    private Set<E> validEvents = new HashSet<>();

    public StateMachineFactory<S, E> setInitialState(S state) {
        if (!validStates.contains(state)) {
            throw new IllegalStateException("Invalid initial state");
        }
        stateMachine.initialState = state;
        return this;
    }

    public StateMachineFactory<S, E> addTransition(S sourceState, E event, S targetState) {
        if (!validStates.contains(sourceState)) {
            throw new IllegalStateException("Invalid source state");
        }
        if (!validEvents.contains(event)) {
            throw new IllegalStateException("Invalid event");
        }
        if (!validStates.contains(targetState)) {
            throw new IllegalStateException("Invalid target state");
        }
        Map<E, S> transitions = stateMachine.config.computeIfAbsent(sourceState, k -> new HashMap<>());
        if (transitions.containsKey(event)) {
            throw new IllegalStateException("Event already configured");
        }
        transitions.put(event, targetState);
        return this;
    }

    public StateMachine<S, E> build() {
        return stateMachine;
    }

    private StateMachineFactory(Set<S> states, Set<E> events) {
        validStates.addAll(states);
        validEvents.addAll(events);
    }
}
