package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import org.endeavourhealth.imapi.model.iml.FunctionClause;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"alias","path","property","bool","case","aggregate",
	"select","where","orderBy","direction","limit","groupBy","having"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Select extends TTAlias{
	private Where where;
	private FunctionClause function;
	private List<Select> select;
	private List<OrderLimit> orderBy;

	private List<TTAlias> groupBy;
	private Having having;
	private List<Case> caze;


	public Select setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public Select setInverse(boolean inverse){
		super.setInverse(inverse);
		return this;
	}

	public Where getWhere() {
		return where;
	}

	@JsonSetter
	public Select setWhere(Where match) {
		this.where = match;
		return this;
	}


	public Select where(Consumer<Where> builder){
		this.where= new Where();
		builder.accept(this.where);
		return this;
	}


	@JsonProperty("case")
	public List<Case> getCase() {
		return caze;
	}

	public Select setCase(List<Case> caze) {
		this.caze = caze;
		return this;
	}

	public Select addCase(Case caze) {
		if (this.caze == null)
			this.caze = new ArrayList<>();
		this.caze.add(caze);
		return this;
	}


	public Select setVariable(String variable) {
		super.setVariable(variable);
		return this;
	}

	@JsonSetter
	public Select setAlias(String alias) {
		super.setAlias(alias);
		return this;
	}


	public Having getHaving() {
		return having;
	}

	@JsonSetter
	public Select setHaving(Having having) {
		this.having = having;
		return this;
	}


	public Select having(Consumer<Having> builder) {
		this.having = new Having();
		builder.accept(this.having);
		return this;
	}



	public List<TTAlias> getGroupBy() {
		return groupBy;
	}

	@JsonSetter
	public Select setGroupBy(List<TTAlias> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public Select addGroupBy(TTAlias groupBy) {
		if (this.groupBy == null)
			this.groupBy = new ArrayList<>();
		this.groupBy.add(groupBy);
		return this;
	}


	public List<Select> getSelect() {
		return select;
	}

	public Select setSelect(List<Select> select) {
		this.select = select;
		return this;
	}



	public Select select(Consumer<Select> builder) {
		if (this.select == null)
			this.select = new ArrayList<>();
		Select select = new Select();
		this.select.add(select);
		builder.accept(select);
		return this;
	}


	public Select addSelect(Select select) {
		if (this.select == null)
			this.select = new ArrayList<>();
		this.select.add(select);
		return this;
	}

	public Select addSelect(String property) {
		if (this.select == null)
			this.select = new ArrayList<>();
		this.select.add(new Select().setIri(property));
		return this;
	}

	public List<OrderLimit> getOrderBy() {
		return orderBy;
	}

	@JsonSetter
	public Select setOrderBy(List<OrderLimit> orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Select addOrderBy(OrderLimit orderBy) {
		if (this.orderBy == null)
			this.orderBy = new ArrayList<>();
		this.orderBy.add(orderBy);
		return this;
	}

	public Select orderBy(Consumer<OrderLimit> builder) {
		OrderLimit orderBy = new OrderLimit();
		addOrderBy(orderBy);
		builder.accept(orderBy);
		return this;
	}







	public Select function(Consumer<FunctionClause> builder) {
		this.function = new FunctionClause();
		builder.accept(this.function);
		return this;
	}

	public FunctionClause getFunction() {
		return function;
	}

	public Select setFunction(FunctionClause functionClause) {
		this.function = functionClause;
		return this;
	}
}


