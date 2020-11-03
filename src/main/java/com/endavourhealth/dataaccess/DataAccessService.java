package com.endavourhealth.dataaccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptAxiom;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.entity.ConceptTct;
import com.endavourhealth.dataaccess.repository.ConceptAxiomRepository;
import com.endavourhealth.dataaccess.repository.ConceptPropertyDataRepository;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;
import com.endavourhealth.dataaccess.util.ConceptTctUtil;
import com.endavourhealth.services.axioms.models.Axiom;
import com.endavourhealth.services.members.models.Code;

public class DataAccessService {

	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptPropertyObjectRepository conceptPropertyObjectRepository;

	@Autowired
	ConceptPropertyDataRepository conceptPropertyDataRepository;

	@Autowired
	ConceptAxiomRepository conceptAxiomRepository;

	@Autowired
	ConceptTctRepository conceptTctRepository;

	@Autowired
	ConceptTctUtil conceptTctService;
	
	public Concept findByIri(String iri) {
		return conceptRepository.findByIri(iri);
	}
	
	public Concept findByDbid(Integer dbId) {
		return conceptRepository.findByDbid(dbId);
	}
	
	public List<Concept> search(String term, String root) {
		return conceptRepository.search(term, root);
	}

	public List<ConceptPropertyObject> getCoreProperties(Integer Dbid, ArrayList<String> relationships) {
		List<ConceptPropertyObject> properties = new ArrayList<>();
		// find concept property objects
		List<ConceptPropertyObject> conceptPropertyObjects = conceptPropertyObjectRepository.findByConceptDbid(Dbid);
		// get concepts for each of those objects
		conceptPropertyObjects.forEach(conceptPropertyObject -> {
			// lookup conceptPropertyObjects in the Transitive Closure table to determine if they are valid
			List<String> types = Arrays.asList("owl:topObjectProperty", "owl:topDataProperty");
			if (conceptTctService.checkIfPropertyIsValidType(conceptPropertyObject, types)) {
				properties.add(conceptPropertyObject);
			}
		});
		return properties;
	}

	public List<ConceptPropertyObject> getInheritedProperties(Integer Dbid, ArrayList<String> relationships) {
		List<ConceptPropertyObject> properties = new ArrayList<>();
		// find concept property objects

		List<ConceptPropertyObject> conceptPropertyObjects = new ArrayList<>();
		List<ConceptTct> conceptTcts = conceptTctRepository.findBySourceDbid(Dbid);
		conceptTcts.forEach(conceptTct -> {
			conceptPropertyObjects
					.addAll(conceptPropertyObjectRepository.findByConceptDbid(conceptTct.getTarget().getDbid()));
		});

		// get concepts for each of those objects
		conceptPropertyObjects.forEach(conceptPropertyObject -> {
			// lookup conceptPropertyObjects in the Transitive Closure table to determine if they are valid
			List<String> types = Arrays.asList("owl:topObjectProperty", "owl:topDataProperty");
			if (conceptTctService.checkIfPropertyIsValidType(conceptPropertyObject, types)
					&& conceptPropertyObject.getConcept().getDbid() != Dbid) {
				properties.add(conceptPropertyObject);
			}
		});
		return properties;
	}

	public List<Code> getMembers(String iri) {
		List<Code> codes = new ArrayList<Code>();

		List<Concept> members = conceptRepository.getMembers(":3521000252101", iri);

		members.forEach(member -> {
			codes.add(new Code(member.getIri(), member.getName(), member.getDescription(), member.getCode(), null));
		});

		return codes;

	}

	public List<Axiom> getAxioms(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		List<Axiom> axioms = new ArrayList<Axiom>();
		List<ConceptAxiom> conceptAxioms = conceptAxiomRepository.findByConceptDbid(concept.getDbid());
		conceptAxioms.forEach(conceptAxiom -> {
			axioms.add(new Axiom(conceptAxiom));
		});
		return axioms;
	}

	public List<ConceptTct> findByTargetDbidAndLevel(Integer conceptDbId, Integer directRelationLevel) {
		return conceptTctRepository.findByTargetDbidAndLevel(conceptDbId, directRelationLevel);
	}

}
