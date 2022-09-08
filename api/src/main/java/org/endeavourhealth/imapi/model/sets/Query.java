package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder ({"iri","name","description","mainEntity","var","resultFormat","subset","distinct","activeOnly","referenceDate","select","groupBy"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query extends Heading {

	private TTIriRef graph;
	private Select select;
	private boolean usePrefixes;
	private boolean activeOnly;
	private Match ask;
	private boolean function;

	public boolean isFunction() {
		return function;
	}

	public Query setFunction(boolean function) {
		this.function = function;
		return this;
	}

	public Match getAsk() {
		return ask;
	}

	@JsonSetter
	public Query setAsk(Match ask) {
		this.ask = ask;
		return this;
	}
	@JsonIgnore
	public Query ask(Consumer<Match> builder){
		Match ask= new Match();
		this.ask= ask;
		builder.accept(ask);
		return this;
	}

	@JsonIgnore
	public Query select(Consumer<Select> builder){
		this.select= new Select();
		builder.accept(this.select);
		return this;
	}



	public boolean isActiveOnly() {
		return activeOnly;
	}

	public Query setActiveOnly(boolean activeOnly) {
		this.activeOnly = activeOnly;
		return this;
	}



	@Override
	public Query setIri(String iri){
		super.setIri(iri);
		return this;
	}



	@Override
	public Query setDescription(String description){
		super.setDescription(description);
		return this;
	}




	public boolean isUsePrefixes() {
		return usePrefixes;
	}

	@Override
	public Query setName(String name){
		super.setName(name);
		return this;
	}

	@Override
	@JsonIgnore
	public Query name(String name){
		super.setName(name);
		return this;
	}

	public Query setUsePrefixes(boolean usePrefixes) {
		this.usePrefixes = usePrefixes;
		return this;
	}


	public TTIriRef getGraph() {
		return graph;
	}

	public Query setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}







	public Select getSelect() {
		return select;
	}

	public Query setSelect(Select select) {
		this.select = select;
		return this;
	}

	@JsonIgnore
	public String getJson() throws JsonProcessingException {
		return Query.getJson(this);
	}

	public static String getJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
	}







}
