package org.endeavourhealth.imapi.model.sets;

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
public class Function extends TTIriRef{
	List<Argument> argument;
	private Map<TTIriRef,TTIriRef> conceptMap;
	private TTIriRef defaultConcept;

	public TTIriRef getDefaultConcept() {
		return defaultConcept;
	}

	public Function setDefaultConcept(TTIriRef defaultConcept) {
		this.defaultConcept = defaultConcept;
		return this;
	}

	public Map<TTIriRef, TTIriRef> getConceptMap() {
		return conceptMap;
	}

	public Function setConceptMap(Map<TTIriRef, TTIriRef> conceptMap) {
		this.conceptMap = conceptMap;
		return this;
	}

	public Function addToConceptMap(TTIriRef from,TTIriRef to){
		if (this.conceptMap==null)
			this.conceptMap= new HashMap<>();
		this.conceptMap.put(from,to);
		return this;
	}

	public String getName() {
		return super.getName();
	}

	public Function setName(String name) {
		super.setName(name);
		return this;
	}

	public List<Argument> getArgument() {
		return argument;
	}

	public Function setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}

	public Function addArgument(Argument argument) {
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}

	public Function argument(Consumer<Argument> builder) {
		Argument argument= new Argument();
		addArgument(argument);
		builder.accept(argument);
		return this;
	}



	public Argument addArgument() {
		if (this.argument==null)
			this.argument= new ArrayList<>();
		Argument newArg= new Argument();
		this.argument.add(newArg);
		return newArg;
	}


	public Function setIri(TTIriRef iri) {
		super.setIri(iri.getIri());
		return this;
	}
}
