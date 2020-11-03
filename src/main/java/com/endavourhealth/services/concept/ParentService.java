package com.endavourhealth.services.concept;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.endavourhealth.dataaccess.DataAccessService;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.services.concept.models.Concept;

@Service
public class ParentService {

	private static final Logger LOG = LoggerFactory.getLogger(ParentService.class);

	@Autowired
	DataAccessService dataAccessService;

	@Autowired
	public ConceptConverter conceptConverter;

	@Value("${concept.isa.iri:sn:116680003}")
	String isAConceptIri;

	public com.endavourhealth.dataaccess.entity.Concept isAConcept;

	@PostConstruct
	void init() {
		isAConcept = dataAccessService.findByIri(isAConceptIri);
	}

	/**
	 * Add the the full parent inheritance hierarchy to the given concept. <br>
	 * Note: it is assumed that the conceptDbId resolves to an actual concept
	 * entity. If not this method will not add any parents. To the caller it will
	 * appear as though the concept does not have any parents. This may not in fact
	 * be the case if the coneptDbId cannot be resolved. It is the caller's
	 * responsibility to ensure that the conceptDbId exists and corresponds to the
	 * given concept <br>
	 * Note 2: it is assumed that the conceptDbId resolves to the given concept
	 * param. This method makes not attempt to check this relationship. It is the
	 * caller's responsibility to ensure that the conceptDbId exists and corresponds
	 * to the given concept
	 * 
	 * @param concept     - the concept to add parents to (must not be null)
	 * @param conceptDbId - the database identifier of the concept (see note above)
	 */
	public void addParents(Concept concept, Integer conceptDbId) {

		if (conceptDbId != null) {

			if (concept != null) {
				List<ConceptPropertyObject> parents = getParents(conceptDbId);
				for (ConceptPropertyObject parent : parents) {
					Concept parentConcept = conceptConverter.convert(parent.getObject());

					if (parentConcept != null) {
						parentConcept.addChild(concept);

						// add parent's parents
						addParents(parentConcept, parent.getObject().getDbid());
					} else {
						// TODO - exception. There is something wrong with the DB as a CPO object points
						// to a non-existant concept
					}
				}
			} else {
				LOG.debug("Unable to add parents to concept as the given concept was null");
			}
		} else {
			LOG.debug(String.format("Unable to add parents to concept: %s as the given conceptDbId was null", concept));
		}
	}

	// filter the CPO entities only retaining those whose property is an instance of
	// the "Is A" concept
	private List<ConceptPropertyObject> getParents(Integer conceptDbId) {
		List<ConceptPropertyObject> parentProperties = new ArrayList<ConceptPropertyObject>();

		List<ConceptPropertyObject> allProperties = dataAccessService.getCoreProperties(conceptDbId, null);

		if (allProperties != null && allProperties.isEmpty() == false) {
			parentProperties = allProperties.stream().filter(cpo -> cpo.getProperty().equals(isAConcept))
					.collect(Collectors.toList());
		} else {
			LOG.debug(String.format(
					"No ConceptPropertyObject instances could be found with the concept field value: %d", conceptDbId));
		}

		return parentProperties;
	}

}
