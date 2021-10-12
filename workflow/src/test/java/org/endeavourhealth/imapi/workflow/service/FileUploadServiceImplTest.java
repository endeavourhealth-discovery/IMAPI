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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void testProcess() {
        FileUpload savedFile = fileUploadService.newUpload(fileUpload);
        assertEquals("UPLOADED", savedFile.getState().toString());
        StateMachine<States, Events> processSM = fileUploadService.process(savedFile.getId());
        assertEquals("PROCESSING", processSM.getState().getId().toString());
    }

    @Transactional
    @Test
    void testProcess_changeEvent() {
        FileUpload savedFile = fileUploadService.newUpload(fileUpload);
        assertEquals("UPLOADED", savedFile.getState().toString());
        StateMachine<States, Events> processSM = fileUploadService.changeEvent(savedFile.getId(),Events.PROCESS);
        assertEquals("PROCESSING", processSM.getState().getId().toString());
    }


}
