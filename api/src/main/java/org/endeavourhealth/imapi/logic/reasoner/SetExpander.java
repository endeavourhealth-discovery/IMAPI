package org.endeavourhealth.imapi.logic.reasoner;

import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;


public class SetExpander {
	private static final Logger LOG = LoggerFactory.getLogger(SetExpander.class);
	private final EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
	private final EntityRepository2 repo2= new EntityRepository2();

	public void expandAllSets(){
		LOG.info("Getting value sets....");
		//First get the list of sets that dont have members already expanded
		Set<String> sets= getSets();
		//for each set get their definition
		for (String iri:sets){
			LOG.info("Updating members of "+ iri);
			//get the definition
			TTBundle setDefinition= entityTripleRepository.getEntityPredicates(iri,Set.of(IM.DEFINITION.getIri()),0);
			//get the expansion.
			List<CoreLegacyCode> members= repo2.getSetExpansion(setDefinition.getEntity().get(IM.DEFINITION),false);
			updateMembers(iri,members);

		}

	}

	private void updateMembers(String iri,List<CoreLegacyCode> members) {
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			String spq = "DELETE { <" + iri + "> <" + IM.HAS_MEMBER.getIri() + "> ?x.}"+
				"\nWHERE { <" + iri + "> <" + IM.HAS_MEMBER.getIri() + "> ?x.}";
			Update upd = conn.prepareUpdate(spq);
			upd.execute();
			StringJoiner sj = new StringJoiner("\n");
			sj.add("INSERT DATA {");
			int batch = 0;
			for (CoreLegacyCode member : members) {
				batch++;
				if (batch < 1000) {
					sj.add("<" + iri + "> <" + IM.HAS_MEMBER.getIri() + "> <" + member.getIri() + ">.");
				} else {
					sendUp(sj, conn);
					sj = new StringJoiner("\n");
					sj.add("INSERT DATA {");
					batch=0;
				}
			}
			sendUp(sj,conn);
		}
	}

	private void sendUp(StringJoiner sj, RepositoryConnection conn) {
		sj.add("}");
		Update upd= conn.prepareUpdate(sj.toString());
		conn.begin();
		upd.execute();
		conn.commit();
	}

	private Set<String> getSets() {
		Set<String> setIris= new HashSet<>();
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			StringJoiner spq = new StringJoiner("\n");
			spq.add("SELECT distinct ?iri ")
				.add("WHERE { ?iri <" + RDF.TYPE.getIri() + "> <"+IM.VALUESET.getIri()+">.")
				.add("?iri <"+ IM.DEFINITION.getIri()+"> ?d.")
				.add("FILTER not exists {?iri <" + IM.HAS_MEMBER.getIri() + "> ?x}}");
			TupleQuery qry = conn.prepareTupleQuery(spq.toString());
			try (TupleQueryResult rs = qry.evaluate()) {
				while (rs.hasNext()) {
					setIris.add(rs.next().getValue("iri").stringValue());
				}
			}
		}
		return setIris;
	}
}
