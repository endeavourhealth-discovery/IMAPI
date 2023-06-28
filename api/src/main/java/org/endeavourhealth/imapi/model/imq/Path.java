package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.function.Consumer;


@JsonPropertyOrder({"parameter","iri","name","match"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Path extends Property{
	private Match match;

	public Match getMatch() {
		return match;
	}

	public Path setMatch(Match match) {
		this.match = match;
		return this;
	}
	public Path match(Consumer<Match> builder){
		builder.accept(setMatch(new Match()).getMatch());
		return this;
	}

	public Path setParameter(String parameter) {
		super.setParameter(parameter);
		return this;
	}

	public static Path iri(String iri) {
		return new Path(iri);
	}

	public Path(){}

	public Path(String iri){
		super.setIri(iri);
	}




	public Path setAncestorsOf(boolean ancestorsOf) {
		super.setAncestorsOf(ancestorsOf);
		return this;
	}


	public Path setDescendantsOrSelfOf(boolean descendantsOrSelfOf) {
		super.setDescendantsOrSelfOf(descendantsOrSelfOf);
		return this;
	}

	public Path setDescendantsOf(boolean descendantsOf) {
		super.setDescendantsOf(descendantsOf);
		return this;
	}



	public Path setInverse(boolean inverse) {
		super.setInverse(inverse);
		return this;
	}


	public Path setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	public Path setName(String name) {
		super.setName(name);
		return this;
	}


	public Path setVariable(String variable) {
		super.setVariable(variable);
		return this;
	}



}

