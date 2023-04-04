package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"nodeVariable","id","variable","case","aggregate", "caze","select"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Select extends Property{
	private FunctionClause function;
	private List<Select> select;
	private List<Case> caze;



	public Select setInverse(boolean inverse){
		super.setInverse(inverse);
		return this;
	}

	public Select setId(String id){
		super.setId(id);
		return this;
	}

	public Select setIri(String id){
		super.setId(id);
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
		this.select.add(new Select().setId(property));
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


