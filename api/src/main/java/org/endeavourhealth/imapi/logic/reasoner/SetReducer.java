package org.endeavourhealth.imapi.logic.reasoner;


import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import javax.naming.directory.InvalidAttributesException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SetReducer {



	/**
	 * Takes a set with a list of pre-expanded members and from those calculates an expression constraint
	 * This takes a while as each member needs to be examined for common parents
	 * @param set the set with a definition of a union of members
	 * <p>Note that the result may or may not be clinically valid as exclusions or intersections cannot be inferred
	 * from a flat member list. If the result list is not shortened then a "NOT CONVERTED TO EC" message is raised in </p>
	 * @return The entity redefined or as original
	 * @throws InvalidAttributesException A message of "NOT CONVERTED TO EC ...."
	 */
	public TTEntity reduce(TTEntity set) throws InvalidAttributesException {
		String sql=null;
		Integer originalSize;
		if (set.get(iri(IM.DEFINITION))!=null) {
				sql= getOrSql(set);
				if (sql==null)
					throw new InvalidAttributesException("Complex ecl. Cannot reduce");
				else
					originalSize= set.get(iri(IM.DEFINITION)).asNode().get(iri(SHACL.OR)).size();
		}
		else if (set.get(iri(IM.HAS_MEMBER))==null) {
			throw new InvalidAttributesException("No set members to reduce");
		}
		else {
				sql = getMemberSql();
				originalSize = set.get(iri(IM.HAS_MEMBER)).size();
		}
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			TupleQuery qry = conn.prepareTupleQuery(sql);
			qry.setBinding("set", Values.iri(set.getIri()));
			try (TupleQueryResult rs = qry.evaluate()) {
				if (!rs.hasNext())
					throw new InvalidAttributesException("Not converted to expression constraint. Does not have expanded members");

				TTNode ors= new TTNode();
				set.set(iri(IM.DEFINITION),ors);
				while (rs.hasNext()) {
					BindingSet bs = rs.next();
					ors.addObject(iri(SHACL.OR),TTIriRef.iri(bs.getValue("member").stringValue()));
				}
				set.getPredicateMap().remove(iri(IM.HAS_MEMBER));
				Integer newSize= set.get(iri(IM.DEFINITION)).asNode().get(iri(SHACL.OR)).size();
				System.out.println("for set " + set.getIri() +
					" original size = "+ originalSize+" new size "+ newSize+ " removed " + (originalSize- newSize) + " members");
			}
		}
		return set;
	}

	private String getMemberSql() {
		StringJoiner sql = new StringJoiner("\n");
		sql.add("PREFIX im: <http://endhealth.info/im#>")
			.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
			.add("Selection distinct ?member ?name")
			.add("where {")
			.add("    ?set im:hasMember ?member.")
			.add("    ?member rdfs:label ?name.")
			.add("   filter not exists {" )
			.add("    ?member im:isA ?super.")
			.add("    filter (?super!=?member)")
			.add("     ?set im:hasMember ?super")
			.add("    }}");
		return sql.toString();
	}

	private String getOrSql(TTEntity set) {

		TTArray definition= set.get(iri(IM.DEFINITION));
		if (definition.isIriRef()){
			return null;
		}
		if (!definition.isNode())
			return null;
		if (definition.asNode().get(iri(SHACL.OR))==null)
			return null;
		for (TTValue value:definition.asNode().get(iri(SHACL.OR)).getElements()){
			if (!value.isIriRef())
				return null;
		}
		StringJoiner sql = new StringJoiner("\n");
		sql.add("PREFIX im: <http://endhealth.info/im#>")
			.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
			.add("PREFIX sh: <http://www.w3.org/ns/shacl#>")
			.add("Select distinct ?member ?name")
			.add("where {")
			.add("    ?set im:definition ?or.")
			.add("    ?or sh:or ?member.")
			.add("    ?member rdfs:label ?name.")
			.add("   filter not exists {" )
			.add("    ?member im:isA ?super.")
			.add("    filter (?super!=?member)")
			.add("     ?or sh:or ?super")
			.add("    }}");
		return sql.toString();
	}


}
