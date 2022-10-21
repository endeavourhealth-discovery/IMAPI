package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"context","type","variable","min","max","where","check"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MapRuleSource {
	private String context;
	private String type;
	private String variable;
	private Integer min;
	private Integer max;
	private Where where;
	private Where check;

	public Where getCheck() {
		return check;
	}

	public MapRuleSource setCheck(Where check) {
		this.check = check;
		return this;
	}

	public MapRuleSource check(Consumer<Where> builder){
		this.check= new Where();
		builder.accept(this.check);
		return this;
	}

	public MapRuleSource where(Consumer<Where> builder){
		this.where= new Where();
		builder.accept(this.where);
		return this;
	}

	public String getContext() {
		return context;
	}

	public MapRuleSource setContext(String context) {
		this.context = context;
		return this;
	}

	public String getType() {
		return type;
	}

	public MapRuleSource setType(String type) {
		this.type = type;
		return this;
	}

	public String getVariable() {
		return variable;
	}

	public MapRuleSource setVariable(String variable) {
		this.variable = variable;
		return this;
	}

	public Integer getMin() {
		return min;
	}

	public MapRuleSource setMin(Integer min) {
		this.min = min;
		return this;
	}

	public Integer getMax() {
		return max;
	}

	public MapRuleSource setMax(Integer max) {
		this.max = max;
		return this;
	}

	public Where getWhere() {
		return where;
	}

	public MapRuleSource setWhere(Where where) {
		this.where = where;
		return this;
	}
}
