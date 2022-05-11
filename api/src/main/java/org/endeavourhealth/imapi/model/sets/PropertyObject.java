package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;


@JsonPropertyOrder({"iri","name","inverseOf","alias","object"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PropertyObject extends TTIriRef {
	private String binding;
	private String alias;
	private Select object;
	boolean inverseOf=false;
	Function function;

	public Function getFunction() {
		return function;
	}

	public PropertyObject setFunction(Function function) {
		this.function = function;
		return this;
	}

	public PropertyObject(){}
	public PropertyObject(String iri){
		if (iri.equals("id"))
			iri= IM.NAMESPACE+"id";
		super.setIri(iri);
	}

	public PropertyObject(TTIriRef iri){
		super.setIri(iri.getIri());
	}

	public PropertyObject(String iri,String name){
		super.setIri(iri);
		super.setName(name);
	}
	public PropertyObject setIri(String iri){
		if (iri.equals("id"))
			iri= IM.NAMESPACE+"id";
		super.setIri(iri);
		return this;
	}
	public PropertyObject setIri(String iri,String name){
		super.setIri(iri);
		super.setName(name);
		return this;
	}

	public PropertyObject setIri(TTIriRef iri){
		super.setIri(iri.getIri());
		return this;
	}

	public PropertyObject setName(String name){
		super.setName(name);
		return this;
	}

	public boolean isInverseOf() {
		return inverseOf;
	}

	public PropertyObject setInverseOf(boolean inverseOf) {
		this.inverseOf = inverseOf;
		return this;
	}

	public String getBinding() {
		return binding;
	}

	public PropertyObject setBinding(String binding) {
		this.binding = binding;
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public PropertyObject setAlias(String alias) {
		this.alias = alias;
		return this;
	}


	public Select getObject() {
		return object;
	}

	public PropertyObject setObject(Select object) {
		this.object = object;
		return this;
	}
}
