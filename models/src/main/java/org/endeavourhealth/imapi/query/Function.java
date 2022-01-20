package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

public class Function extends TTNode {

	public Function(){
		setPredicateTemplate(new TTIriRef[]{RDFS.LABEL,IM.ARGUMENT});
	}

	public TTIriRef getName() throws InvalidClassException {
		return (TTIriRef) TTUtil.get(this, IM.FUNCTION_NAME,TTIriRef.class);
	}

	public Function setName(TTIriRef name) {
		set(IM.FUNCTION_NAME,name);
		return this;
	}

	public List<Argument> getArgument() {
		return TTUtil.getOrderedList(this,IM.ARGUMENT,Argument.class);
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
