package org.endeavourhealth.imapi.statemachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StateMachine<S, E, I> {

    public interface BeforeTransition<S, E>{
        boolean execute(S currentState, E event, S newState);
    }
    public interface AfterTransitions<S, E>{
        void execute(S newState, E event, S previousState);
    }

    protected S initialState;
    protected StateMachinePersister<I,S> persister;
    protected Map<S, Map<E, S>> config = new HashMap<>();

    private BeforeTransition<S, E> onBeforeTransition = (S currentState, E event, S newState) -> true;
    private AfterTransitions<S, E> onAfterTransition = (S newState, E event, S previousState) -> {};
    private S currentState;
    private I id;

    public S getCurrentState() {
        return currentState;
    }

    public I getId() {
        return id;
    }

    public void start(I id) {
        this.id = id;
        currentState = initialState;
        if(persister!=null){
            this.persister.save(id,initialState);
        }
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
        if (onBeforeTransition.execute(currentState, event, nextState)) {
            S previousState = currentState;
            currentState = nextState;
            onAfterTransition.execute(currentState, event, previousState);
            if(this.persister!=null){
                this.persister.save(id,currentState);
            }
        }
        return currentState;
    }

    public void load(I id){
        if(this.persister!=null){
            this.id = id;
            this.currentState = this.persister.load(id);
        }

    }

    public Set<E> possibleEvents() {
        Map<E, S> transitions = config.get(currentState);
        return transitions.keySet();
    }

    public StateMachine<S, E, I> beforeTransitions(BeforeTransition<S, E> onBeforeTransition) {
        this.onBeforeTransition = onBeforeTransition;
        return this;
    }

    public StateMachine<S, E, I> afterTransitions(AfterTransitions<S, E> onAfterTransitions) {
        this.onAfterTransition = onAfterTransitions;
        return this;
    }
}
