package org.endeavourhealth.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.endeavourhealth.imapi.model.mapping.PRSBMapping;
import org.endeavourhealth.imapi.model.mapping.PRSBMapping.Dataset.DatasetConcept;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/mapping")
@CrossOrigin(origins = "*")
@Api(value = "MappingController", description = "Main Mapping endpoint")
public class MappingController {
	
//	json to pojo
//
//	pojo to ttconcept/concept
//
//	sql check
//
//	sql create/update

	ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping(value = "/prsb")
	public void importMappings() throws IOException {
		PRSBMapping prsb = objectMapper.readValue(new File("src/test/resources/RetrieveTransaction.json"),
				PRSBMapping.class);

		List<TTConcept> ttconceptList = new ArrayList<TTConcept>();

		prsb.getDataset().get(0).getConcept().forEach(concept -> {
			addConvertedConcept(concept, ttconceptList, null);
		});
		
		// perform create/update
		// if needed
		
		// TEST CODE
		writeJsonToFile(ttconceptList);
	}

	public void addConvertedConcept(DatasetConcept concept, List<TTConcept> conceptList, DatasetConcept parent) {
		conceptList.add(getTTConcept(concept, parent));

		if (concept.getConcept() != null) {
			concept.getConcept().forEach(child -> {
				addConvertedConcept(child, conceptList, concept);
			});
		}
	}

	public TTConcept getTTConcept(DatasetConcept concept, DatasetConcept parent) {
		TTConcept ttconcept = new TTConcept();
		ttconcept.setIri(IM.NAMESPACE + concept.getShortName());
		ttconcept.setName(concept.getName().get(0).getText());
		ttconcept.setDescription(concept.getDesc().get(0).getText());
		ttconcept.setScheme(null);
		ttconcept.setStatus(concept.getStatusCodeTTIriRef());
		ttconcept.setType(null);
		ttconcept.setCode(concept.getIddisplay());
		
//		TTArray property = new TTArray();
//		property.add(new TTLiteral(concept.getMaximumMultiplicity(), SHACL.MAXCOUNT));
		
//		ttconcept.set(SHACL.PROPERTY, property);
//		ttconcept.set(SHACL.PROPERTY, new TTLiteral(concept.getMinimumMultiplicity(), SHACL.MINCOUNT));
		
		if (parent != null) {
			TTArray isas = new TTArray();
			isas.add(new TTIriRef(IM.NAMESPACE + parent.getShortName(), parent.getName().get(0).getText()));
			ttconcept.set(IM.IS_A, isas);
		}
		
		
		
		
		
		return ttconcept;
	}
	
	private void writeJsonToFile(List<TTConcept> ttconceptList) throws IOException {
		Path path = Paths.get("src/test/resources/output.json");
		Files.write(path, ("[" + System.lineSeparator()).getBytes());

		ttconceptList.forEach(ttconcept -> {
			try {
				String json = objectMapper.writeValueAsString(ttconcept);
				Files.write(path, (json + "," + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		Files.write(path, ("]").getBytes(), StandardOpenOption.APPEND);
	}

}
