package org.endeavourhealth.imapi.workflow.service;

import org.endeavourhealth.imapi.workflow.domain.FileUpload;
import org.endeavourhealth.imapi.workflow.repository.FileUploadRepository;
import org.endeavourhealth.imapi.workflow.domain.Events;
import org.endeavourhealth.imapi.workflow.domain.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@RequiredArgsConstructor
@ComponentScan("org.endeavourhealth.imapi.workflow")
//@Service
@Service
@Transactional
public class FileUploadServiceImpl{
    public static final String FILE_ID_HEADER = "file_id";

    @Autowired
    FileUploadRepository fileUploadRepository;

    @Autowired
    StateMachineFactory<States, Events> stateMachineFactory;

    @Autowired
    FileUploadStateChangeInterceptor fileUploadStateChangeInterceptor;


    public FileUpload newUpload(FileUpload fileUpload) {
        fileUpload.setState(States.UPLOADED);
        fileUpload.setId(1L);
        return fileUploadRepository.save(fileUpload);
    }

    @Transactional
    public StateMachine<States, Events> process(Long id) {
        StateMachine<States,Events> sm = build(id);
        sendEvent(id, sm, Events.PROCESS);
        return sm;
    }

    @Transactional
    public StateMachine<States, Events> complete(Long id) {
        StateMachine<States,Events> sm = build(id);
        sendEvent(id, sm, Events.COMPLETE);
        return sm;
    }

    @Transactional
    public StateMachine<States, Events> download(Long id) {
        StateMachine<States,Events> sm = build(id);
        sendEvent(id, sm, Events.DOWNLOAD);
        return sm;
    }

    @Transactional
    public StateMachine<States,Events> changeEvent(Long id, Events event){
        StateMachine<States,Events> sm = build(id);
        sendEvent(id, sm, event);
        return sm;
    }

    private void sendEvent(Long id, StateMachine<States, Events> sm, Events event){
        Message<Events> msg = MessageBuilder.withPayload(event)
                .setHeader(FILE_ID_HEADER, id)
                .build();
        sm.sendEvent(msg);
    }

    private StateMachine<States, Events> build(Long id){

        FileUpload fileUpload = fileUploadRepository.getOne(id);

        StateMachine<States, Events> sm = stateMachineFactory.getStateMachine(Long.toString(fileUpload.getId()));

        sm.stop();

        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(fileUploadStateChangeInterceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(fileUpload.getState(), null, null, null));
                });

        sm.start();


        return sm;
    }
}
