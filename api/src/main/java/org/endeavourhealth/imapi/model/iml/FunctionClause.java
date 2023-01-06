package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@JsonPropertyOrder({"iri","name","argument"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class FunctionClause extends TTIriRef {
	List<Argument> argument;
	private Map<String, String> conceptMap;
	private TTIriRef defaultValue;

	public TTIriRef getDefaultValue() {
		return defaultValue;
	}

	public FunctionClause setDefaultValue(TTIriRef defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}


	public FunctionClause setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public Map<String, String> getConceptMap() {
		return conceptMap;
	}

	public FunctionClause setConceptMap(Map<String, String> conceptMap) {
		this.conceptMap = conceptMap;
		return this;
	}

	public FunctionClause addToConceptMap(String from, String to) {
		if (this.conceptMap == null)
			this.conceptMap = new HashMap<>();
		this.conceptMap.put(from, to);
		return this;
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public FunctionClause setName(String name) {
		super.setName(name);
		return this;
	}

	public List<Argument> getArgument() {
		return argument;
	}

	public FunctionClause setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}

	public FunctionClause addArgument(Argument argument) {
		if (this.argument == null)
			this.argument = new ArrayList<>();
		this.argument.add(argument);
		return this;
	}

	public FunctionClause argument(Consumer<Argument> builder) {
		Argument argument = new Argument();
		addArgument(argument);
		builder.accept(argument);
		return this;
	}


	public Argument addArgument() {
		if (this.argument == null)
			this.argument = new ArrayList<>();
		Argument newArg = new Argument();
		this.argument.add(newArg);
		return newArg;
	}


	public FunctionClause setIri(TTIriRef iri) {
		super.setIri(iri.getIri());
		return this;
	}
}
