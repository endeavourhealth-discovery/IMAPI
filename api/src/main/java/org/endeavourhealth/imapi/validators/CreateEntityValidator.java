package org.endeavourhealth.imapi.validators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.ArrayList;

public class CreateEntityValidator {
    public void isValid(TTEntity entity, EntityService service) throws TTFilerException, JsonProcessingException {
        ArrayList<String> errorMessages = new ArrayList();
        ObjectMapper mapper = new ObjectMapper();
        if (!isValidIri(entity)) errorMessages.add("Missing iri.");
        if (service.iriExists(entity.getIri())) errorMessages.add("Iri already exists.");
        if (!isValidName(entity)) errorMessages.add("Name is invalid.");
        if (!isValidType(entity)) errorMessages.add("Types are invalid.");
        if (!isValidStatus(entity)) errorMessages.add("Status is invalid");
        if (errorMessages.size() > 0) {
            String errorsAsString = String.join(",", errorMessages);
            throw new TTFilerException("Create entity errors: [" + errorsAsString + "] for entity " + mapper.writeValueAsString(entity));
        }
    }

    private Boolean isValidIri(TTEntity entity) {
        if (null == entity.getIri()) return false;
        return true;
    }

    private Boolean isValidName(TTEntity entity) {
        if(null == entity.getName() || 0 == entity.getName().length()) return false;
        return true;
    }

    private Boolean isValidType(TTEntity entity) {
        if (null == entity.getType() || entity.getType().isEmpty() || !entity.getType().getElements().stream().allMatch(type -> type.isIriRef())) return false;
        return true;
    }

    private Boolean isValidStatus(TTEntity entity) {
        if (null == entity.getStatus() || !entity.getStatus().isIriRef()) return false;
        return true;
    }

    private Boolean hasParents(TTEntity entity) {
        if (null == entity.get(IM.IS_A) && null == entity.get(IM.IS_CONTAINED_IN) && null == entity.get(RDFS.SUBCLASSOF)) return false;
        return true;
    }
}
