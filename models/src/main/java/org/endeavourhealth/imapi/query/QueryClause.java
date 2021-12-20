package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@JsonPropertyOrder ({"mainEntity","steps"})
/**
 * The node that defined the query to be run, consisting of a main entity and a set of sequential steps
 * <p>Query steps run sequentially. Branching logic such as rules are performed by the steps which access outputs from other steps via the step variable name</p>
 */
public class QueryClause extends TTNode{

	/**
	 * gets the main root entity associated with the query definition
	 * @return The iri of the main entity
	 */
	public TTIriRef getSubject() {
		return this.get(RDF.SUBJECT).asIriRef();
	}

	/**
	 * Queries should have a main or root entity iri which is used to ensure that all resources are related in some way to the main entity
	 * <p> Typically this may be a Patient but might be events or organisations or locations (households) </p>
	 * @param subject iri of the main entity e.g. http://endhealth.info/im#Patient
	 * @return the query definition as amended
	 */
	public QueryClause setSubject(TTIriRef subject) {
		this.set(RDF.SUBJECT,subject);
		return this;
	}

	/**
	 * Gets the steps involved in the query definition
	 * @return List of steps in sequence order
	 */
	public  List<MatchClause> getMatches() {
		if (this.get(IM.MATCH)==null)
			return null;
		return this.get(IM.MATCH).getElements()
			.stream().sorted(Comparator.comparing(TTValue::getOrder))
			.map(e -> (MatchClause) e)
				.collect(Collectors.toList());
	}

	public QueryClause setMatches(TTArray matches) {
		this.set(IM.MATCH,matches);
		return this;
	}
	public MatchClause addMatch() {
		int count=0;
		if (this.get(IM.MATCH)!=null)
			count= this.get(IM.MATCH).size();
		 MatchClause match = new MatchClause();
		match.set(IM.HAS_ORDER,TTLiteral.literal(count+1));
	 this.addObject(IM.MATCH,match);
		return match;
	}

	public void setVariable(String variable){
		set(IM.VARIABLE,TTLiteral.literal(variable));
	}

	public String getVariable(){
		return get(IM.VARIABLE)==null ? null : get(IM.VARIABLE).asLiteral().getValue();

	}

}
