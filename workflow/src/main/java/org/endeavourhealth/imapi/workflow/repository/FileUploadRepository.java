package org.endeavourhealth.imapi.workflow.repository;

import org.endeavourhealth.imapi.workflow.domain.FileUpload;
import org.springframework.stereotype.Component;

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
