package org.endeavourhealth.workflow.repository;

import org.endeavourhealth.workflow.domain.Events;
import org.endeavourhealth.workflow.domain.FileUpload;
import org.endeavourhealth.workflow.domain.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class FileUploadRepository {

    public FileUpload getOne(Long id) {
        FileUpload result = new FileUpload();
        result.setId(id);
        return result;
    }
    public FileUpload save(FileUpload fileUpload) {
        System.out.println("***** SAVE *****");
        return fileUpload;
    }

}
