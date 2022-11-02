package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

public class Concept{
	private String iri;
	private String name;
	private String description;
	private String code;
	private TTIriRef scheme;
	private Set<String> im1Id;
	private Set<Concept> matchedFrom;
	private Integer usage;


	@JsonProperty("@id")
	public String getIri() {
		return iri;
	}

	@JsonProperty("@id")
	public Concept setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public Integer getUsage() {
		return usage;
	}

	public Concept setUsage(Integer usage) {
		this.usage = usage;
		return this;
	}


	public String getName() {
		return name;
	}


	public Concept setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Concept setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getCode() {
		return code;
	}

	public Concept setCode(String code) {
		this.code = code;
		return this;
	}

	public TTIriRef getScheme() {
		return scheme;
	}

	public Concept setScheme(TTIriRef scheme) {
		this.scheme = scheme;
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
