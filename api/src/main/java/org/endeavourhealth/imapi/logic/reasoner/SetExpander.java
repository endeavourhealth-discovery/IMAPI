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
import java.util.Set;
import java.util.StringJoiner;
import java.util.zip.DataFormatException;


public class SetExpander {
	private static final Logger LOG = LoggerFactory.getLogger(SetExpander.class);
	private final EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
	private final EntityRepository2 repo2= new EntityRepository2();

	public void expandAllSets() {
		LOG.info("Getting value sets....");
		//First get the list of sets that dont have members already expanded
		Set<String> sets= getSets();
		//for each set get their definition
		for (String iri:sets){
			LOG.info("Updating members of "+ iri);
			//get the definition
			TTBundle setDefinition= entityTripleRepository.getEntityPredicates(iri,Set.of(IM.DEFINITION.getIri()));
			//get the expansion.
			Set<CoreLegacyCode> members= repo2.getSetExpansion(setDefinition.getEntity().get(IM.DEFINITION),false);
			updateMembers(iri,members);

		}

	}

	public void expandSet(String iri) throws DataFormatException {
		LOG.info("Updating members of "+ iri);
		TTBundle setDefinition= entityTripleRepository.getEntityPredicates(iri,Set.of(IM.DEFINITION.getIri()));
		if (setDefinition.getEntity().get(IM.DEFINITION)==null)
			throw new DataFormatException(iri+ " : Unknown iri or this set has no definition");
		//get the expansion.
		Set<CoreLegacyCode> members= repo2.getSetExpansion(setDefinition.getEntity().get(IM.DEFINITION),false);
		updateMembers(iri,members);

	}

	private void updateMembers(String iri,Set<CoreLegacyCode> members) {
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			String spq = "DELETE { <" + iri + "> <" + IM.HAS_MEMBER.getIri() + "> ?x.}"+
				"\nWHERE { <" + iri + "> <" + IM.HAS_MEMBER.getIri() + "> ?x.}";
			Update upd = conn.prepareUpdate(spq);
			upd.execute();
			String graph= "<"+iri.substring(0,iri.lastIndexOf("#")+1)+">";
			StringJoiner sj = new StringJoiner("\n");
			sj.add("INSERT DATA { graph "+ graph+"{");
			int batch = 0;
			for (CoreLegacyCode member : members) {
				batch++;
				if (batch == 1000) {
					sendUp(sj, conn);
					sj = new StringJoiner("\n");
					sj.add("INSERT DATA { graph " + graph + "{");
					batch = 0;
				}
				sj.add("<" + iri + "> <" + IM.HAS_MEMBER.getIri() + "> <" + member.getIri() + ">.");
				}
			sendUp(sj,conn);
		}
	}

	private void sendUp(StringJoiner sj, RepositoryConnection conn) {
		sj.add("}}");
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
