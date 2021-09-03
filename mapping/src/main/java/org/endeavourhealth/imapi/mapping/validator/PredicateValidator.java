package org.endeavourhealth.imapi.mapping.validator;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
		Set<String> existingPredicates = entityService.getPredicateIris();
		Set<String> newPredicates = new HashSet<String>();
		for (Map.Entry<String, List<MappingInstruction>> entry : map.getInstructions().entrySet()) {
			entry.getValue().forEach(instruction -> {
				if (!existingPredicates.contains(instruction.getProperty())) {
					newPredicates.add(instruction.getProperty());
				}
			});
		}
		newPredicates.remove("@id");

		return newPredicates;
	}

}
