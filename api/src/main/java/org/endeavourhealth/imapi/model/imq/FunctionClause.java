package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FunctionClause extends Value{
	private Function function;
	private List<Argument> argument;
	private Range range;

	public Range getRange() {
		return range;
	}

	public FunctionClause setRange(Range range) {
		this.range = range;
		return this;
	}


	public FunctionClause range(Consumer<Range> builder){
		this.range= new Range();
		builder.accept(this.range);
		return this;
	}

	public Function getFunction() {
		return function;
	}

	public FunctionClause setFunction(Function function) {
		this.function = function;
		return this;
	}

	public List<Argument> getArgument() {
		return argument;
	}

	public FunctionClause setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}


	public FunctionClause addArgument(Argument argument){
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}
	public FunctionClause argument(Consumer<Argument> builder){
		Argument argument= new Argument();
		addArgument(argument);
		builder.accept(argument);
		return this;
	}


	public Value setOperator(Operator operator) {
		super.setOperator(operator);
		return this;
	}




	@Override
	public Value setValue(String value) {
		super.setValue(value);
		return this;
	}


}
