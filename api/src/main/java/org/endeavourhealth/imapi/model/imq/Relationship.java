package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.function.Consumer;


@JsonPropertyOrder({"parameter","iri","type","set","variable","name"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Relationship extends Property{
	private Node node;

	public Node getNode() {
		return node;
	}

	public Relationship setNode(Node node) {
		this.node = node;
		return this;
	}
	public Relationship node(Consumer<Node> builder){
		builder.accept(setNode(new Node()).getNode());
		return this;
	}

	public Relationship setParameter(String parameter) {
		super.setParameter(parameter);
		return this;
	}

	public static Relationship iri(String iri) {
		return new Relationship(iri);
	}

	public Relationship(){}

	public Relationship(String iri){
		super.setIri(iri);
	}




	public Relationship setAncestorsOf(boolean ancestorsOf) {
		super.setAncestorsOf(ancestorsOf);
		return this;
	}


	public Relationship setDescendantsOrSelfOf(boolean descendantsOrSelfOf) {
		super.setDescendantsOrSelfOf(descendantsOrSelfOf);
		return this;
	}

	public Relationship setDescendantsOf(boolean descendantsOf) {
		super.setDescendantsOf(descendantsOf);
		return this;
	}



	public Relationship setInverse(boolean inverse) {
		super.setInverse(inverse);
		return this;
	}


	public Relationship setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	public Relationship setName(String name) {
		super.setName(name);
		return this;
	}


	public Relationship setVariable(String variable) {
		super.setVariable(variable);
		return this;
	}



}

