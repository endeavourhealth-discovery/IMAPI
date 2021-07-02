package org.endeavourhealth.imapi.workflow.service;

import org.endeavourhealth.imapi.workflow.domain.Events;
import org.endeavourhealth.imapi.workflow.domain.FileUpload;
import org.endeavourhealth.imapi.workflow.domain.States;
import org.endeavourhealth.imapi.workflow.repository.FileUploadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

@EnableAutoConfiguration
@SpringBootTest(classes = {FileUploadServiceImpl.class})
class FileUploadServiceImplTest {

    @Autowired
    FileUploadServiceImpl fileUploadService;

    @Autowired
    FileUploadRepository fileUploadRepository;

    FileUpload fileUpload;

    @BeforeEach
    void setUp() {
        fileUpload = FileUpload.builder().build();
    }

    @Transactional
    @Test
    public void testProcess() {
        FileUpload savedFile = fileUploadService.newUpload(fileUpload);
        System.out.println(savedFile.getState());
        StateMachine<States, Events> processSM = fileUploadService.process(savedFile.getId());
        System.out.println(processSM.getState().getId());


    }
}
