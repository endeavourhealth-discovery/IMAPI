package org.endeavourhealth.imapi.validators;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.ArrayList;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EntityValidator {
    public void isValid(TTEntity entity, EntityService service, String mode) throws TTFilerException, JsonProcessingException {
        ArrayList<String> errorMessages = new ArrayList();
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            if (Boolean.TRUE.equals(!isValidIri(entity))) errorMessages.add("Missing iri.");
            if ("Create".equals(mode) && service.iriExists(entity.getIri())) errorMessages.add("Iri already exists.");
            if ("Update".equals(mode) && !service.iriExists(entity.getIri())) errorMessages.add("Iri doesn't exists.");
            if (Boolean.TRUE.equals(!isValidName(entity))) errorMessages.add("Name is invalid.");
            if (Boolean.TRUE.equals(!isValidType(entity))) errorMessages.add("Types are invalid.");
            if (Boolean.TRUE.equals(!isValidStatus(entity))) errorMessages.add("Status is invalid");
            if (Boolean.TRUE.equals(!hasParents(entity))) errorMessages.add("Parents are invalid");
            if (!errorMessages.isEmpty()) {
                String errorsAsString = String.join(",", errorMessages);
                throw new TTFilerException(mode + " entity errors: [" + errorsAsString + "] for entity " + om.writeValueAsString(entity));
            }
        }
    }

    private Boolean isValidIri(TTEntity entity) {
        if (null == entity.getIri()) return false;
        return !"".equals(entity.getIri());
    }

    private Boolean isValidName(TTEntity entity) {
        if(null == entity.getName()) return false;
        return !"".equals(entity.getName());
    }

    private Boolean isValidType(TTEntity entity) {
        if (null == entity.getType()) return false;
        if (entity.getType().isEmpty()) return false;
        return entity.getType().getElements().stream().allMatch(TTValue::isIriRef);
    }

    private Boolean isValidStatus(TTEntity entity) {
        if (null == entity.getStatus()) return false;
        return entity.getStatus().isIriRef();
    }

    private Boolean hasParents(TTEntity entity) {
        if (null == entity.get(iri(IM.IS_A)) && null == entity.get(iri(IM.IS_CONTAINED_IN)) && null == entity.get(iri(RDFS.SUBCLASS_OF)) && null == entity.get(iri(IM.IS_SUBSET_OF))) return false;
        if (null != entity.get(iri(IM.IS_A)) && !entity.get(iri(IM.IS_A)).isEmpty()) {
            if (!entity.get(iri(IM.IS_A)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
        }
        if (null!= entity.get(iri(IM.IS_CONTAINED_IN)) && !entity.get(iri(IM.IS_CONTAINED_IN)).isEmpty()) {
            if (!entity.get(iri(IM.IS_CONTAINED_IN)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
        }
        if (null != entity.get(iri(RDFS.SUBCLASS_OF)) && !entity.get(iri(RDFS.SUBCLASS_OF)).isEmpty()) {
            if (!entity.get(iri(RDFS.SUBCLASS_OF)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
        }
        if (null != entity.get(iri(IM.IS_SUBSET_OF)) && !entity.get(iri(IM.IS_SUBSET_OF)).isEmpty()) {
            if (!entity.get(iri(IM.IS_SUBSET_OF)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
        }
        return true;
    }
}
