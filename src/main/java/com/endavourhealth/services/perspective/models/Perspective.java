package com.endavourhealth.services.perspective.models;

import java.util.Set;

import com.endavourhealth.services.concept.models.Concept;


public class Perspective {

	String name;
	String rootIri;
	Set<String> inheritancePropertyIris;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRootIri() {
		return rootIri;
	}
	
	public void setRootIri(String rootIri) {
		this.rootIri = rootIri;
	}
	
	public Set<String> getInheritancePropertyIris() {
		return inheritancePropertyIris;
	}
	
	public void setInheritancePropertyIris(Set<String> inheritancePropertyIris) {
		this.inheritancePropertyIris = inheritancePropertyIris;
	}
	
	public Concept getConcept(String iri) {
		return null;
	}
}
