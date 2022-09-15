package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@JsonPropertyOrder({"path","name","inverseOf","alias","argument","function","select"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Select extends QueryClause {
	private TTAlias property;
	private boolean sum;
	private boolean average;
	private boolean max;
	List<Argument> argument;
	Function function;

	public TTAlias getProperty() {
		return property;
	}



	@JsonIgnore
	public Select setProperty(String propertyIri) {
		this.property = new TTAlias().setIri(propertyIri);
		return this;
	}

	@JsonIgnore
	public Select setProperty(TTIriRef property) {
		this.property = new TTAlias(property);
		return this;
	}

	public boolean isSum() {
		return sum;
	}

	public Select setSum(boolean sum) {
		this.sum = sum;
		return this;
	}

	public boolean isAverage() {
		return average;
	}

	public Select setAverage(boolean average) {
		this.average = average;
		return this;
	}

	public boolean isMax() {
		return max;
	}

	public Select setMax(boolean max) {
		this.max = max;
		return this;
	}

	public List<Argument> getArgument() {
		return argument;
	}

	@JsonSetter
	public Select setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}

	public Select addArgument(Argument argument){
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}

	public Select argument(Consumer<Argument> builder){
		Argument arg= new Argument();
		this.addArgument(arg);
		builder.accept(arg);
		return this;
	}





	public Select function(Consumer<Function> builder){
		this.function= new Function();
		builder.accept(this.function);
		return this;
	}

	public Function getFunction() {
		return function;
	}

	public Select setFunction(Function function) {
		this.function = function;
		return this;
	}




}
