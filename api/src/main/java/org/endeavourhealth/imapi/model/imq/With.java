package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTAlias;

import java.util.ArrayList;
import java.util.function.Consumer;

public class With extends Where{
	private TTAlias orderBy;
	private Order direction;
	private Integer count=1;
	private Where then;

	public Where getThen() {
		return then;
	}

	public With setThen(Where then) {
		this.then = then;
		return this;
	}

	public With then(Consumer<Where> builder){
		this.then= new Where();
		builder.accept(this.then);
		return this;
	}

	public TTAlias getOrderBy() {
		return orderBy;
	}

	public With setOrderBy(TTAlias orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Order getDirection() {
		return direction;
	}

	public With setDirection(Order direction) {
		this.direction = direction;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public With setCount(Integer count) {
		this.count = count;
		return this;
	}
	public With setBool(Bool bool){
		super.setBool(bool);
		return this;
	}
	public With where (Consumer<Where> builder){
		super.where(builder);
		return this;
	}
	public With setAlias(String alias){
		super.setAlias(alias);
		return this;
	}

	public With setValueLabel(String valueLabel) {
		super.setValueLabel(valueLabel);
		return this;
	}


	public With setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	public With setInverse(boolean inverse){
		super.setInverse(inverse);
		return this;
	}

	public With setDescendantsOrSelfOf(boolean subtypes){
		super.setDescendantsOrSelfOf(subtypes);
		return this;
	}


	public With setName(String name) {
		super.setName(name);
		return this;
	}


	public With setDescription(String description) {
		super.setDescription(description);
		return this;
	}


	public With addIn(TTAlias in){
		super.addIn(in);
		return this;
	}

	public With in(Consumer<TTAlias> builder){
		super.in(builder);
		return this;
	}

	public With notIn(Consumer<TTAlias> builder){
		super.notIn(builder);
		return this;
	}

	public With addIn(String in){
		super.addIn(in);
		return this;
	}


	@Override
	public With setOperator(Operator operator) {
		super.setOperator(operator);
		return this;
	}



}
