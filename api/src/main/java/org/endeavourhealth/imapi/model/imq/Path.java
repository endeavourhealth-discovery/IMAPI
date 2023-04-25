package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.function.Consumer;


@JsonPropertyOrder({"parameter","iri","type","set","variable","name"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Path extends Property{
	private Node node;

	public Node getNode() {
		return node;
	}

	public Path setNode(Node node) {
		this.node = node;
		return this;
	}
	public Path node(Consumer<Node> builder){
		builder.accept(setNode(new Node()).getNode());
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

