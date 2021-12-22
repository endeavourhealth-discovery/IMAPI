package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;

public class Filter extends TTNode {

	public Filter setCompareTo(TTIriRef equalityOperator,TTValue value){
		set(equalityOperator,value);
		return this;
	}

	public TTIriRef getEqualityOperator(){
		List<TTIriRef> eqOperators= List.of(IM.EQUAL,IM.LESS_THAN,
			IM.LESS_THAN_EQUAL,IM.GREATER_THAN,IM.GREATER_THAN_EQUAL);
		for (TTIriRef eop:eqOperators) {
			if (get(eop) != null)
				return eop;
		}
		return null;
	}
	public TTValue getCompareTo(TTIriRef eqOperator){
		return get(eqOperator).get(0);

	}
}
