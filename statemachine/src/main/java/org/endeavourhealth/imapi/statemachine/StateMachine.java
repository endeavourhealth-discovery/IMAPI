package org.endeavourhealth.imapi.statemachine;

import java.util.Map;
import java.util.Set;

public class StateMachine {

    public interface BeforeTransition{
        boolean execute(String currentState, String event, String newState);
    }
    public interface AfterTransitions{
        void execute(String newState, String event, String previousState);
    }

    private BeforeTransition onBeforeTransition = (String currentState, String event, String newState) -> true;
    private AfterTransitions onAfterTransition = (String newState, String event, String previousState) -> {};
    private String currentState;
    private String id;
    private StateMachineConfig config = new StateMachineConfig();
    private StateMachineConfigDAL configDAL;
    private StateMachineTaskDAL taskDAL;

    public StateMachine() {
        configDAL = new StateMachineConfigDALJDBC();
        taskDAL = new StateMachineTaskDALJDBC();
    }

    protected StateMachine(StateMachineConfigDAL configDAL, StateMachineTaskDAL taskDAL) {
        this.configDAL = configDAL;
        this.taskDAL = taskDAL;
    }

    public StateMachine loadConfig(String name) throws Exception {
        this.config = configDAL.loadConfig(name);
        return this;
    }

    public StateMachine config(String name, Set<String> states, Set<String> events, String initialState, Set<StateMachineTransition> transitions) throws Exception {
        config.setType(name);
        config.getValidStates().addAll(states);
        config.getValidEvents().addAll(events);
        config.setInitialState(initialState);
        transitions.forEach(t -> config.addTransition(t.getSource(), t.getEvent(), t.getTarget()));
        if (configDAL != null)
            configDAL.saveConfig(name, config);
        return this;
    }

    public String getCurrentState() {
        return currentState;
    }

    public String getId() {
        return id;
    }

    public void newTask(String id) throws Exception {
        if (!this.config.getValidStates().contains(config.getInitialState()))
            throw new IllegalStateException("Invalid initial state");

        this.id = id;
        currentState = config.getInitialState();
        if(taskDAL!=null){
            this.taskDAL.save(config.getType(), id, config.getInitialState());
        }
    }

    public String sendEvent(String event) throws Exception {
        if (currentState == null) {
            throw new IllegalStateException("Current state invalid");
        }

        Map<String, String> transitions = config.getEventTransitions(currentState);

        if (transitions == null) {
            throw  new IllegalStateException("No transitions configured for current state");
        }

        String nextState = transitions.get(event);

        if (nextState == null) {
            throw new IllegalStateException("No valid next state for event");
        }
        if (onBeforeTransition.execute(currentState, event, nextState)) {
            String previousState = currentState;
            currentState = nextState;
            onAfterTransition.execute(currentState, event, previousState);
            if(this.taskDAL!=null){
                this.taskDAL.save(config.getType(), id, currentState);
            }
        }
        return currentState;
    }

    public void loadTask(String id) throws Exception {
        if(this.taskDAL!=null){
            this.id = id;
            this.currentState = this.taskDAL.load(config.getType(), id);
        }
    }

    public Set<String> possibleEvents() {
        Map<String, String> transitions = config.getEventTransitions(currentState);
        return transitions.keySet();
    }

    public StateMachine beforeTransition(BeforeTransition onBeforeTransition) {
        this.onBeforeTransition = onBeforeTransition;
        return this;
    }

    public StateMachine afterTransition(AfterTransitions onAfterTransitions) {
        this.onAfterTransition = onAfterTransitions;
        return this;
    }
}
