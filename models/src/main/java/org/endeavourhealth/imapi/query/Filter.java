package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IMQ;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.Arrays;
import java.util.List;

public class Filter extends TTNode {

	/**
	 * Instantiates a filter from an RDF node
	 * @param source pre-populated node
	 */
	public Filter(TTNode source){
		this.setPredicateMap(source.getPredicateMap());
	}

	public Filter(TTIriRef type){
		this.set(RDF.TYPE,type);
	}
	public Filter(){
	}

	/**
	 * Convenience method for getting the filter type
	 * @return
	 */
	public TTIriRef getType(){
		return get(RDF.TYPE).asIriRef();
	}

	/**
	 * Adds an object to an IN list
	 * @param entry the entry to add to the in LIST
	 * @return the query or filter clause with the in LIST
	 */
	public Filter addInList(TTValue entry){
		if (get(IMQ.EXPRESSION)==null)
			set(IMQ.EXPRESSION,new TTArray());
			get(IMQ.EXPRESSION).asArray().add((TTIriRef) entry);
		return this;
	}
	/**
	 * adds the variable the filter is filtering on.
	 * @return the Filter for chaining.
	 */
	public Filter setFilterVar(String var){
		set(IMQ.VAR,TTLiteral.literal(var));
		return this;
	}
}
