package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;
import java.util.function.Consumer;


@JsonPropertyOrder({"iri","name","inverseOf","alias","binding","function","select"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PropertySelect extends ConceptRef {
	private Select select;
	boolean inverseOf=false;
	Function function;



	public PropertySelect select(Consumer<Select> builder){
		this.select= new Select();
		builder.accept(this.select);
		return this;
	}

	@JsonIgnore
	public PropertySelect select(List<TTIriRef> properties){
		Select select= new Select();
		this.setSelect(select);
		for (TTIriRef iri:properties){
			select.addProperty(new PropertySelect(iri));
		}
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
	@Override
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



	@JsonSetter
	public PropertySelect setAlias(String alias) {
		super.setAlias(alias);
		return this;
	}

	@JsonIgnore
	public PropertySelect alias(String alias) {
		super.setAlias(alias);
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
