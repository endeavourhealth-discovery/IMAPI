package org.endeavourhealth.imapi.validators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.ArrayList;

public class EntityValidator {
    public void isValid(TTEntity entity, EntityService service, String mode) throws TTFilerException, JsonProcessingException {
        ArrayList<String> errorMessages = new ArrayList();
        ObjectMapper mapper = new ObjectMapper();
        if (!isValidIri(entity)) errorMessages.add("Missing iri.");
        if ("Create".equals(mode) && service.iriExists(entity.getIri())) errorMessages.add("Iri already exists.");
        if ("Update".equals(mode) && !service.iriExists(entity.getIri())) errorMessages.add("Iri doesn't exists.");
        if (!isValidName(entity)) errorMessages.add("Name is invalid.");
        if (!isValidType(entity)) errorMessages.add("Types are invalid.");
        if (!isValidStatus(entity)) errorMessages.add("Status is invalid");
        if (!hasParents(entity)) errorMessages.add("Parents are invalid");
        if (errorMessages.size() > 0) {
            String errorsAsString = String.join(",", errorMessages);
            throw new TTFilerException(mode + " entity errors: [" + errorsAsString + "] for entity " + mapper.writeValueAsString(entity));
        }
    }

    private Boolean isValidIri(TTEntity entity) {
        if (null == entity.getIri()) return false;
        if ("".equals(entity.getIri())) return false;
        return true;
    }

    private Boolean isValidName(TTEntity entity) {
        if(null == entity.getName()) return false;
        if ("".equals(entity.getName())) return false;
        return true;
    }

    private Boolean isValidType(TTEntity entity) {
        if (null == entity.getType()) return false;
        if (entity.getType().isEmpty()) return false;
        if (!entity.getType().getElements().stream().allMatch(TTValue::isIriRef)) return false;
        return true;
    }

    private Boolean isValidStatus(TTEntity entity) {
        if (null == entity.getStatus()) return false;
        if (!entity.getStatus().isIriRef()) return false;
        return true;
    }

    private Boolean hasParents(TTEntity entity) {
        if (null == entity.get(IM.IS_A) && null == entity.get(IM.IS_CONTAINED_IN) && null == entity.get(RDFS.SUBCLASSOF)) return false;
        if (!entity.get(IM.IS_A).isEmpty()) {
            if (!entity.get(IM.IS_A).getElements().stream().allMatch(TTValue::isIriRef)) return false;
        }
        if (!entity.get(IM.IS_CONTAINED_IN).isEmpty()) {
            if (!entity.get(IM.IS_CONTAINED_IN).getElements().stream().allMatch(TTValue::isIriRef)) return false;
        }
        if (!entity.get(RDFS.SUBCLASSOF).isEmpty()) {
            if (!entity.get(RDFS.SUBCLASSOF).getElements().stream().allMatch(TTValue::isIriRef)) return false;
        }
        return true;
    }
}
