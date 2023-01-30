package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.SourceType;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"id","iri","name","alias","description","with","type","bool","from","where"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class From extends TTAlias {
	private Bool bool;
	private List<From> from;
	private Where where;
	private String description;
	private With with;


	public From setType (TTAlias type){
		super.setIri(type.getIri());
		super.setName(type.getName());
		super.setVariable(type.getVariable());
		super.setIncludeSubtypes(type.isIncludeSubtypes());
		this.setSourceType(SourceType.type);
		return this;
	}

	public From setSourceType(SourceType type){
		super.setSourceType(type);
		return this;
	}



	public Bool getBool() {
		return bool;
	}

	public From setBool(Bool bool) {
		this.bool = bool;
		return this;
	}


	public List<From> getFrom() {
		return from;
	}

	@JsonSetter
	public From setFrom(List<From> from) {
		this.from = from;
		return this;
	}
	public From from(Consumer<From> builder){
		From from= new From();
		addFrom(from);
		builder.accept(from);
		return this;
	}

	public From addFrom(From from) {
		if (this.from==null)
			this.from= new ArrayList<>();
		this.from.add(from);
		return this;
	}


	public With getWith() {
		return with;
	}

	public From setWith(With with) {
		this.with = with;
		return this;
	}


	public From setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	public From setIncludeSubtypes(boolean include){
		super.setIncludeSubtypes(include);
		return this;
	}

	public From setName(String name) {
		super.setName(name);
		return this;
	}




	public From setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}



	public Where getWhere() {
		return where;
	}

	@JsonSetter
	public From setWhere(Where where) {
		this.where = where;
		return this;
	}

	public From where(Consumer<Where> builder){
		this.where= new Where();
		builder.accept(where);
		return this;
	}





}