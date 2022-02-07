package org.endeavourhealth.imapi.workflow.service;

import lombok.RequiredArgsConstructor;
import org.endeavourhealth.imapi.workflow.domain.Events;
import org.endeavourhealth.imapi.workflow.domain.FileUpload;
import org.endeavourhealth.imapi.workflow.domain.States;
import org.endeavourhealth.imapi.workflow.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class FileUploadStateChangeInterceptor extends StateMachineInterceptorAdapter<States, Events> {

    @Autowired
    FileUploadRepository fileUploadRepository;

    @Override
    public void preStateChange(State<States, Events> state, Message<Events> message,
                               Transition<States, Events> transition, StateMachine<States, Events> stateMachine,
                               StateMachine<States, Events> rootStateMachine) {

        Optional.ofNullable(message).flatMap(msg -> Optional.ofNullable((Long) msg.getHeaders()
                .getOrDefault(FileUploadServiceImpl.FILE_ID_HEADER, -1L))).ifPresent(id -> {
            FileUpload fileUpload = fileUploadRepository.getOne(id);
            fileUpload.setState(state.getId());
            fileUploadRepository.save(fileUpload);
        });
    }
}
