package com.endavourhealth.services.concept.models;

import java.util.List;

public class Query {
	private String ontology;
	private List<String> module;
	private String term;
	private String view;
	
	public Query() {
		super();
	}
	public String getOntology() {
		return ontology;
	}
	public void setOntology(String ontology) {
		this.ontology = ontology;
	}
	public List<String> getModule() {
		return module;
	}
	public void setModule(List<String> module) {
		this.module = module;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	
	

}
