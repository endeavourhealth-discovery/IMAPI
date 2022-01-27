package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.io.InvalidClassException;

public class Argument extends TTNode {

	public Argument(){
		set(RDF.TYPE,IM.ARGUMENT_CLAUSE);
	}

	public TTValue getParameter() throws InvalidClassException {
		return (TTValue) TTUtil.get(this, IM.PARAMETER,TTValue.class);
	}

	public Argument setParameter(String parameter) {
		set(IM.PARAMETER, TTLiteral.literal(parameter));
		return this;
	}
	public Argument setParameter(TTIriRef parameter) {
		set(IM.PARAMETER, parameter);
		return this;
	}

	public String getValue() throws InvalidClassException {
		return (String) TTUtil.get(this,IM.VALUE_DATA,String.class);
	}

	public Argument setValue(String value) {
		set(IM.VALUE_DATA,TTLiteral.literal(value));
		return this;
	}

	public Match getValueMatch(){
		return (Match) TTUtil.get(this,IM.VALUE_MATCH, Match.class);
	}

	public Argument setValueMatch(Match match){
		set(IM.VALUE_MATCH,match);
		return this;
	}

	public TTIriRef getValueIri(){
		return (TTIriRef) TTUtil.get(this,IM.VALUE_MATCH, TTIriRef.class);
	}

	public Argument setValueIri(TTIriRef iri){
		set(IM.VALUE_IRI,iri);
		return this;
	}
}
