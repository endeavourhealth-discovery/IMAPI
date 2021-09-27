package org.endeavourhealth.imapi.mapping.validator;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.mapping.model.MappingInstructionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PredicateValidator {

    @Autowired
    EntityService entityService;

    public Set<String> getNewPredicates(MappingInstructionWrapper map) throws SQLException {
        Set<String> mapPredicates = new HashSet<String>();
        for (List<MappingInstruction> mappingInstructions : map.getInstructions().values()) {
            for (MappingInstruction instruction : mappingInstructions) {
                mapPredicates.add(instruction.getProperty());
            }
        }
        Set<String> existingPredicates = entityService.getPredicateIris(mapPredicates);
        existingPredicates.add("@id");
        existingPredicates.add("http://www.w3.org/ns/r2rml#graph");
        mapPredicates.removeAll(existingPredicates);
        return mapPredicates;
    }

}
