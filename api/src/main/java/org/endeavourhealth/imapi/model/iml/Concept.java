package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

public class Concept extends Entity{

	private String code;
	private Set<String> im1Id;
	private Set<TTIriRef> subClassOf;
	private Set<Concept> matchedFrom;
	private Integer usage;

	public Set<TTIriRef> getSubClassOf() {
		return subClassOf;
	}

	public Concept setSubClassOf(Set<TTIriRef> subClassOf) {
		this.subClassOf = subClassOf;
		return this;
	}

	public Concept addSubClassOf(TTIriRef superClass){
		if (this.subClassOf==null)
			this.subClassOf= new HashSet<>();
		this.subClassOf.add(superClass);
		return this;
	}

	@JsonProperty("@id")
	public Concept setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	public Integer getUsage() {
		return usage;
	}

	public Concept setUsage(Integer usage) {
		this.usage = usage;
		return this;
	}




	public Concept setName(String name) {
		super.setName(name);
		return this;
	}

	public Concept setDescription(String description) {
		super.setDescription(description);
		return this;
	}

	public String getCode() {
		return code;
	}

	public Concept setCode(String code) {
		this.code = code;
		return this;
	}


	public Concept setScheme(TTIriRef scheme) {
		super.setScheme(scheme);
		return this;
	}

	public Set<String> getIm1Id() {
		return im1Id;
	}

	public Concept setIm1Id(Set<String> im1Id) {
		this.im1Id = im1Id;
		return this;
	}

	public Concept addIm1Id(String im1Id){
		if (this.im1Id==null)
			this.im1Id= new HashSet<>();
		this.im1Id.add(im1Id);
		return this;
	}

	public Set<Concept> getMatchedFrom() {
		return matchedFrom;
	}

	public Concept setMatchedFrom(Set<Concept> matchedFrom) {
		this.matchedFrom = matchedFrom;
		return this;
	}

	public Concept addMatchedFrom(Concept legacy){
		if (this.matchedFrom ==null)
			this.matchedFrom = new HashSet<>();
			this.matchedFrom.add(legacy);
		return this;
	}
}
