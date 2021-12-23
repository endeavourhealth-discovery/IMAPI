package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;


public class Filter{
	private Comparison comparison;
	private IriVar set;
	private String value;
	private String var;

	public Filter setClassTest(Comparison comp, TTIriRef concept){
		comparison= comp;
		set= new IriVar(concept.getIri());
		return this;
	}
	public Filter setClassTest(Comparison comp, String concept){
		comparison= comp;
		set= new IriVar(concept);
		return this;
	}
	public Filter setValueTest(Comparison comp, String value){
		comparison= comp;
		this.value = value;
		return this;
	}

	public Comparison getComparison() {
		return comparison;
	}

	public Filter setComparison(Comparison comparison) {
		this.comparison = comparison;
		return this;
	}

	public IriVar getSet() {
		return set;
	}



	public Filter setSet(TTIriRef set) {
		this.set = new IriVar(set.getIri());
		return this;
	}

	public String getValue() {
		return value;
	}

	public Filter setValue(String value) {
		this.value = value;
		return this;
	}

	public String getVar() {
		return var;
	}

	public Filter setVar(String var) {
		this.var = var;
		return this;
	}
}
