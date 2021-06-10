package org.endeavourhealth.workflow.service;

import org.endeavourhealth.workflow.domain.Events;
import org.endeavourhealth.workflow.domain.FileUpload;
import org.endeavourhealth.workflow.domain.States;
import org.endeavourhealth.workflow.repository.FileUploadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@EnableAutoConfiguration
@SpringBootTest(classes = {org.endeavourhealth.workflow.service.FileUploadServiceImpl.class})
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