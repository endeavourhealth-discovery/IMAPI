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
	private String type;
	private String set;
	private Relationship path;

	public Relationship getPath() {
		return path;
	}

	public Node setPath(Relationship path) {
		this.path = path;
		return this;
	}
	public Node path(Consumer<Relationship> builder){
		builder.accept(setPath(new Relationship()).getPath());
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
	@JsonProperty("@type")
	public String getType() {
		return type;
	}



	@JsonProperty("@set")
	public String getSet() {
		return set;
	}

	public Node setSet(String set) {
		this.set = set;
		return this;
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


	public Node setType(String type) {
		this.type= type;
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

