package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;

/**
 * A step in a query equivalent to a WHERE clause in SPARQL. The step determines what happens
 * to the matches and what happens to the main entity if not matched.
 * Support one boolean and / or/ not operator for the match clauses (default = and)
 */
public class QueryStep extends TTNode {

	/**
	 * What happens to the matches if matched or not matched
	 * @param action action if matched or not matched
	 */
	public QueryStep setInclusionAction(TTIriRef action){
		this.set(IM.INCLUSION_ACTION,action);
		return this;
	}

	/**
	 * Gets the result set inclusion action when matches are found or not found
	 * @return
	 */
	public TTIriRef getInclusionAction(){
		return this.get(IM.INCLUSION_ACTION).asIriRef();
	}

	public TTIriRef getOperator() {
		return this.get(IM.OPERATOR).asIriRef();
	}

	/**
	 * Sets the AND/OR operator for the match graphs in this clause if more than one Default is AND
	 * @param operator the IRI for AND/OR
	 * @return the clause as modified
	 */
	public QueryStep setOperator(TTIriRef operator) {
		this.set(IM.OPERATOR,operator);
		return this;
	}

	public String getVariable() {
		return this.get(IM.VARIABLE).asLiteral().getValue();
	}

	/**
	 * Assigns a variable name to the result set from this query clause
	 * @param variable the name of the variable
	 * @return Clause as modified
	 */
	public QueryStep setVariable(String variable) {
		this.set(IM.VARIABLE, TTLiteral.literal(variable));
		return this;
	}

	public TTArray getMatches() {
		return this.get(IM.MATCH);
	}

	public QueryStep setMatches(TTArray matches) {
		this.set(IM.MATCH,matches);
		return this;
	}

	/**
	 * Adds a match clause which consists of one subject (the object to start from) and a list of paths (propert values)
	 *
	 * @return the query clause as modified.
	 */
	public MatchClause addMatch(){
		int count=0;
		if (this.get(IM.MATCH)!=null)
			count= this.get(IM.MATCH).size();
		MatchClause match= new MatchClause();
		match.set(IM.HAS_ORDER,TTLiteral.literal(count+1));
		this.addObject(IM.MATCH,match);
		return match ;
	}
}











