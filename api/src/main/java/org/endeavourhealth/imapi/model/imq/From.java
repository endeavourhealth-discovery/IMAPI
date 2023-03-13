package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"graph","id","iri","set","type","name","alias","includeSubtypes","includeSupertypes","sourceType","description","type","with","bool","from","where"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class From extends TTAlias {
	private Bool boolFrom;
	private List<From> from;
	private Bool boolWhere;

	private TTAlias graph;
	private List<Where> where;
	private String description;

	public Bool getBoolWhere() {
		return boolWhere;
	}

	public From setBoolWhere(Bool boolWhere) {
		this.boolWhere = boolWhere;
		return this;
	}

	public TTAlias getGraph() {
		return graph;
	}

	public From setGraph(TTAlias graph) {
		this.graph = graph;
		return this;
	}



	public From setType(String type){
		super.setType(type);
		return this;
	}

	public From setSet(String set){
		super.setSet(set);
		return this;
	}


	public Bool getBoolFrom() {
		return boolFrom;
	}

	public From setBoolFrom(Bool boolFrom) {
		this.boolFrom = boolFrom;
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




	public From setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	public static From iri(String iri)
	{
		return new From().setIri(iri);
	}
	public From setVariable(String variable){
		super.setVariable(variable);
		return this;
	}

	public From setAlias(String alias){
		super.setAlias(alias);
		return this;
	}

	public From setDescendantsOrSelfOf(boolean include){
		super.setDescendantsOrSelfOf(include);
		return this;
	}

	public From setAncestorsOf(boolean include){
		super.setAncestorsOf(include);
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

	public List<Where> getWhere() {
		return where;
	}

 @JsonSetter
	public From setWhere(List<Where> where) {
		this.where = where;
		return this;
	}

	public From addWhere(Where where){
		if (this.where==null)
			this.where= new ArrayList<>();
		this.where.add(where);
		return this;
	}

	public From where(Consumer<Where> builder){
		Where where= new Where();
		addWhere(where);
		builder.accept(where);
		return this;
	}





}
