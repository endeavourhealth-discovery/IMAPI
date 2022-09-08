package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@JsonPropertyOrder({"not","path","in","range","or","function","argument","comparison","value"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Where extends Value{
	private boolean not;
	private String path;
	private List<TTIriRef> in;
	private Range range;
	private List<Argument> argument;
	private List<Where> or;
	private List<Where> and;
	private Function function;
	private Compare compare;

	public Compare getCompare() {
		return compare;
	}

	public Where setCompare(Compare compare) {
		this.compare = compare;
		return this;
	}

	public Function getFunction() {
		return function;
	}

	public Where setFunction(Function function) {
		this.function = function;
		return this;
	}

	public boolean isNot() {
		return not;
	}

	public Where setNot(boolean not) {
		this.not = not;
		return this;
	}

	public String getPath() {
		return path;
	}

	public Where setPath(String path) {
		this.path = path;
		return this;
	}


	public List<TTIriRef> getIn() {
		return in;
	}

	public Where setIn(List<TTIriRef> in) {
		this.in = in;
		return this;
	}

	public Where addIn(TTIriRef in){
		if (this.in==null)
			this.in= new ArrayList<>();
		this.in.add(in);
		return this;
	}

	public Range getRange() {
		return range;
	}

	public Where setRange(Range range) {
		this.range = range;
		return this;
	}

	public List<Argument> getArgument() {
		return argument;
	}

	public Where setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}
	public Where addArgument(Argument argument){
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}

	@JsonIgnore
	public Where argument(Consumer<Argument> builder){
		Argument argument= new Argument();
		addArgument(argument);
		builder.accept(argument);
		return this;
	}

	public List<Where> getOr() {
		return or;
	}

	public Where setOr(List<Where> or) {
		this.or = or;
		return this;
	}
	
	public Where addOr(Where or){
		if (this.or== null)
			this.or= new ArrayList<>();
		this.or.add(or);
		return this;
	}
	public Where or(Consumer<Where> builder){
		Where or = new Where();
		addOr(or);
		builder.accept(or);
		return this;
		
	}
	public List<Where> getAnd() {
		return and;
	}

	public Where setAnd(List<Where> and) {
		this.and = and;
		return this;
	}

	public Where addAnd(Where and){
		if (this.and== null)
			this.and= new ArrayList<>();
		this.and.add(and);
		return this;
	}
	public Where and(Consumer<Where> builder){
		Where and = new Where();
		addAnd(and);
		builder.accept(and);
		return this;

	}
}
