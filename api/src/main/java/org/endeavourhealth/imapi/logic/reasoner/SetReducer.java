package org.endeavourhealth.imapi.logic.reasoner;


import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import javax.naming.directory.InvalidAttributesException;
import java.util.*;

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
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			List<String> members = new ArrayList<>();
			StringJoiner sql = new StringJoiner("\n");
			sql.add("Select ?core where {?concept <" + IM.HAS_MEMBER.getIri() + "> ?member." +
				"?member <"+ IM.MATCHED_TO.getIri()+"> ?core}");
			TupleQuery qry = conn.prepareTupleQuery(sql.toString());
			qry.setBinding("concept", Values.iri(set.getIri()));
			try (TupleQueryResult rs = qry.evaluate()) {
				if (!rs.hasNext())
					throw new InvalidAttributesException("Not converted to expression constraint. Does not have expanded members");
				while (rs.hasNext()) {
					BindingSet bs = rs.next();
					members.add(bs.getValue("core").stringValue());
				}
				Set<String> toRemove = new HashSet<>();
				for (String member : members) {
					qry = conn.prepareTupleQuery("Select ?parent\n " +
						"where {<" + member + "> <" + IM.IS_A.getIri() + "> ?parent}");
					TupleQueryResult rs2 = qry.evaluate();
					while (rs2.hasNext()) {
						BindingSet bs = rs2.next();
						String parent = bs.getValue("parent").stringValue();
						if (!parent.equals(member)) {
							if (members.contains(parent)) {
								toRemove.add(member);
							}
						}
					}
				}
				int currentCount = members.size();
				toRemove.forEach(m -> members.remove(m));
				int newCount = members.size();
				if (currentCount - newCount < 10)
					throw new InvalidAttributesException("Not converted to expression constraint. No significant change");
				set.getPredicateMap().remove(IM.HAS_MEMBER);
				TTNode or = new TTNode();
				set.set(IM.DEFINITION, or);
				for (String member : members)
					or.addObject(SHACL.OR, TTIriRef.iri(member));
				System.out.println("for set " + set.getIri() + " removed " + (currentCount - newCount) + " members");
			}
		}
		return set;
	}



}
