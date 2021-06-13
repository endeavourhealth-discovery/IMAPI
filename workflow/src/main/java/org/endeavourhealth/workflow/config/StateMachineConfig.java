package org.endeavourhealth.workflow.config;

import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.workflow.domain.Events;
import org.endeavourhealth.workflow.domain.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<States,Events>{


    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates()
                .initial(States.UPLOADED)
                .states(EnumSet.allOf(States.class))
                .end(States.DOWNLOADED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal().source(States.UPLOADED).target(States.PROCESSING).event(Events.PROCESS).action(processAction())
                .and()
                .withExternal().source(States.PROCESSING).target(States.COMPLETED).event(Events.COMPLETE)
                .and()
                .withExternal().source(States.COMPLETED).target(States.DOWNLOADED).event(Events.DOWNLOAD);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        StateMachineListenerAdapter<States,Events> adapter = new StateMachineListenerAdapter<States, Events>(){
            @Override
            public void stateChanged(State from, State to) {
                log.info(String.format("stateChanged(from: %s, to: %s)", from, to));
            }
        };

        config.withConfiguration().listener(adapter);
    }

    public Action<States,Events> processAction(){
        return context -> {
            System.out.println("Process was called ");

        };
    }







}
