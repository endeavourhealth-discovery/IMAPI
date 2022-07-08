package org.endeavourhealth.imapi.logic.service;

import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
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

	public TTIriRef createConcept(String namespace) throws Exception {
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			String sql="select ?increment where {<"+ IM.NAMESPACE+"SnomedConceptGenerator>"+" <"+
				IM.NAMESPACE+"hasIncrementalFrom> ?increment}";
			TupleQuery qry = conn.prepareTupleQuery(sql);
			TupleQueryResult rs= qry.evaluate();
			if (rs.hasNext()){
				Integer from= Integer.parseInt(rs.next().getValue("increment").stringValue());
				updateIncrement(from);
				String concept= SnomedConcept.createConcept(from,false);
				return TTIriRef.iri(namespace+concept);
			}
		}
	return null;
	}

	private void updateIncrement(Integer from) throws Exception {
		TTDocument document = new TTDocument()
			.setCrud(IM.UPDATE_PREDICATES)
			.setGraph(IM.CODE_SCHEME_DISCOVERY)
			.addEntity(new TTEntity()
				.setCrud(IM.UPDATE_PREDICATES)
				.setIri(IM.NAMESPACE+"SnomedConceptGenerator")
				.set(TTIriRef.iri(IM.NAMESPACE+"hasIncrementalFrom"), TTLiteral.literal(from+1)));
		TTTransactionFiler filer= new TTTransactionFiler(null);
		filer.fileTransaction(document);
	}
}
