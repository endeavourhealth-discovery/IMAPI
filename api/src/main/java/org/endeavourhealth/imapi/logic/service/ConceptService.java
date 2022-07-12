package org.endeavourhealth.imapi.logic.service;

import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.ConceptRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TTTransactionFiler;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.transforms.SnomedConcept;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.concurrent.ExecutionException;

public class ConceptService {
	private ConceptRepository conceptRepository = new ConceptRepository();

	public TTIriRef createConcept(String namespace) throws Exception {
		return conceptRepository.createConcept(namespace);
	}

}
