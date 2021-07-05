package org.endeavourhealth.imapi.statemachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateMachineFactory<S, E, I> {
    public static <S, E, I> StateMachineFactory<S, E, I> configure (Set<S> states, Set<E> events) {
        return new StateMachineFactory<>(states, events);
    }
    private final StateMachine<S, E, I> stateMachine = new StateMachine<>();
    private final Set<S> validStates = new HashSet<>();
    private final Set<E> validEvents = new HashSet<>();

    public StateMachineFactory<S, E, I> setPersister(StateMachinePersister<I, S> persister){
        stateMachine.persister = persister;
        return this;
    }

    public StateMachineFactory<S, E, I> setInitialState(S state) {
        if (!validStates.contains(state)) {
            throw new IllegalStateException("Invalid initial state");
        }
        stateMachine.initialState = state;
        return this;
    }

    public StateMachineFactory<S, E, I> addTransition(S sourceState, E event, S targetState) {
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

    public StateMachine<S, E, I> build() {
        return stateMachine;
    }

    private StateMachineFactory(Set<S> states, Set<E> events) {
        validStates.addAll(states);
        validEvents.addAll(events);
    }
}
