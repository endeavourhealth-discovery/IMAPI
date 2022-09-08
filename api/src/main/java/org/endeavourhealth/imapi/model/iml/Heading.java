package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

/** Extends TTIriRef with optional variable, alias, and description
 * for use in dynamic data modelling
 */
@JsonPropertyOrder({"description","@id","name","alias","var"})
public class Heading extends TTIriRef {
	private String alias;
	private String description;
	private TTIriRef status;
	private TTIriRef scheme;
	private String label;
	private List<TTIriRef> type;
	private List<TTIriRef> isContainedIn;


	public TTIriRef getStatus() {
		return status;
	}

	public Heading setStatus(TTIriRef status) {
		this.status = status;
		return this;
	}

	public TTIriRef getScheme() {
		return scheme;
	}

	public Heading setScheme(TTIriRef scheme) {
		this.scheme = scheme;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public Heading setLabel(String label) {
		this.label = label;
		return this;
	}

	public List<TTIriRef> getType() {
		return type;
	}

	public Heading setType(List<TTIriRef> type) {
		this.type = type;
		return this;
	}

	public List<TTIriRef> getIsContainedIn() {
		return isContainedIn;
	}

	public Heading setIsContainedIn(List<TTIriRef> isContainedIn) {
		this.isContainedIn = isContainedIn;
		return this;
	}

	public static Heading iri(String iri) {
		return new Heading(iri);
	}
	public static Heading iri(String iri, String name) {
		return new Heading(iri, name);
	}
	public static Heading iri(TTIriRef iri) {
		return new Heading(iri.getIri());
	}

	public Heading(){
	}

	@Override
	public Heading setName(String name){
		super.setName(name);
		return this;
	}


	@JsonIgnore
	public Heading name(String name){
		super.setName(name);
		return this;
	}



	public Heading(String iri) {
		setIri(iri);
	}
	public Heading(String iri, String name) {
		setIri(iri);
		setName(name);
	}

	public Heading(TTIriRef iriRef){
		setIri(iriRef.getIri());
		if (iriRef.getName()!=null)
			setName(iriRef.getName());
	}


	public String getAlias() {
		return alias;
	}

	public Heading setAlias(String alias) {
		this.alias = alias;
		return this;
	}


	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	public Heading setDescription(String description) {
		this.description = description;
		return this;
	}


}

