package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.function.Consumer;


@JsonPropertyOrder({"parameter","iri","type","set","variable","name"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Node extends Element{


	@Override
	public Node setRef(String ref) {
		super.setRef(ref);
		return this;
	}

	public Node setParameter(String parameter) {
		super.setParameter(parameter);
		return this;
	}

	public static Node iri(String iri) {
		return new Node(iri);
	}

	public Node(){}

	public Node(String iri){
		super.setIri(iri);
	}







	public Node setAncestorsOf(boolean ancestorsOf) {
		super.setAncestorsOf(ancestorsOf);
		return this;
	}

	public Node setDescendantsOrSelfOf(boolean descendantsOrSelfOf) {
		super.setDescendantsOrSelfOf(descendantsOrSelfOf);
		return this;
	}

	public Node setDescendantsOf(boolean descendantsOf) {
		super.setDescendantsOf(descendantsOf);
		return this;
	}


	@JsonSetter
	public Node setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	public Node setName(String name) {
		super.setName(name);
		return this;
	}

	public Node setVariable(String variable) {
		super.setVariable(variable);
		return this;
	}



}

