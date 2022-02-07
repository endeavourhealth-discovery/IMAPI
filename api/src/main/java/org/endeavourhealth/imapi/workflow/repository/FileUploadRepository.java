package org.endeavourhealth.imapi.workflow.repository;

import org.endeavourhealth.imapi.workflow.domain.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileUploadRepository {
    private static final Logger LOG = LoggerFactory.getLogger(FileUploadRepository.class);

    public FileUpload getOne(Long id) {
        FileUpload result = new FileUpload();
        result.setId(id);
        return result;
    }
    public FileUpload save(FileUpload fileUpload) {
        LOG.debug("***** SAVE *****");
        return fileUpload;
    }

}
