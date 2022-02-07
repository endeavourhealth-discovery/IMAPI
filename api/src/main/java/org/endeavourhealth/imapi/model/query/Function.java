package org.endeavourhealth.imapi.model.query;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;

public class Function extends TTNode {

	public Function(){
		setPredicateTemplate(new TTIriRef[]{IM.FUNCTION_IRI,IM.ARGUMENT});
	}

	public TTIriRef getName(){
		return (TTIriRef) TTUtil.get(this, IM.FUNCTION_IRI,TTIriRef.class);
	}

	public Function setName(TTIriRef name) {
		set(IM.FUNCTION_IRI,name);
		return this;
	}

	public List<Argument> getArgument() {
		return TTUtil.getOrderedList(this,IM.ARGUMENT, Argument.class);
	}

	public Function setArgument(TTArray argument) {
		set(IM.ARGUMENT,argument);
		return this;
	}

	public Function addArgument(Argument argument){
		TTUtil.add(this,IM.ARGUMENT,argument);
		return this;
	}
}
