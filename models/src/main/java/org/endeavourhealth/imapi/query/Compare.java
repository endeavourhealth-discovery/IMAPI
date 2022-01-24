package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

public class Compare extends TTNode {

	public Compare(){
		setPredicateTemplate(new TTIriRef[]{IM.COMPARISON,IM.FUNCTION,
		IM.VALUE_DATA});
	}

	public Comparison getComparison() {
		if (get(IM.COMPARISON)==null)
			return null;
		else
			return Comparison.valueOf(get(IM.COMPARISON).asLiteral().getValue());
	}

	public Compare setComparison(Comparison comparison) {
		set(IM.COMPARISON, TTLiteral.literal(comparison.name()));
		return this;
	}

	public String getValue() throws InvalidClassException {
		return (String) TTUtil.get(this,IM.VALUE_DATA,String.class);
	}


	public Compare setValue(String value) {
		 setValue(TTLiteral.literal(value));
		return this;
	}

	public Compare setValue(TTLiteral value){
		set(IM.VALUE_DATA,value);
		return this;
	}


	public Function getFunction() {
		return (Function) TTUtil.get(this,IM.FUNCTION,Function.class);
	}

	public Compare setFunction(Function function) {
		set(IM.FUNCTION,function);
		return this;
	}


}
