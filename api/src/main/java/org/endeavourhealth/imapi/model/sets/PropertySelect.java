package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.function.Consumer;


@JsonPropertyOrder({"iri","name","inverseOf","alias","binding","function","select"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PropertySelect extends TTIriRef {
	private String binding;
	private String alias;
	private Select select;
	boolean inverseOf=false;
	Function function;

	public PropertySelect select(Consumer<Select> builder){
		this.select= new Select();
		builder.accept(this.select);
		return this;
	}

	public PropertySelect function(Consumer<Function> builder){
		this.function= new Function();
		builder.accept(this.function);
		return this;
	}

	public Function getFunction() {
		return function;
	}

	public PropertySelect setFunction(Function function) {
		this.function = function;
		return this;
	}

	public PropertySelect(){}
	public PropertySelect(String iri){
		if (iri.equals("id"))
			iri= IM.NAMESPACE+"id";

		super.setIri(iri);
	}

	public PropertySelect(TTIriRef iri){
		super.setIri(iri.getIri());
	}

	public PropertySelect(String iri, String name){
		super.setIri(iri);
		super.setName(name);
	}
	public PropertySelect setIri(String iri){
		if (iri.equals("id"))
			iri= IM.NAMESPACE+"id";
		super.setIri(iri);
		return this;
	}
	public PropertySelect setIri(String iri, String name){
		super.setIri(iri);
		super.setName(name);
		return this;
	}

	public PropertySelect setIri(TTIriRef iri){
		super.setIri(iri.getIri());
		return this;
	}

	public PropertySelect setName(String name){
		super.setName(name);
		return this;
	}

	public boolean isInverseOf() {
		return inverseOf;
	}

	public PropertySelect setInverseOf(boolean inverseOf) {
		this.inverseOf = inverseOf;
		return this;
	}

	public String getBinding() {
		return binding;
	}

	public PropertySelect setBinding(String binding) {
		this.binding = binding;
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public PropertySelect setAlias(String alias) {
		this.alias = alias;
		return this;
	}


	public Select getSelect() {
		return select;
	}

	public PropertySelect setSelect(Select select) {
		this.select = select;
		return this;
	}
}
