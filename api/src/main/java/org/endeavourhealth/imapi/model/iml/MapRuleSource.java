package org.endeavourhealth.imapi.model.iml;

import java.util.List;

public class MapRuleSource {
	private String context;
	private String type;
	private String variable;
	private Integer min;
	private Integer max;
	private Where where;
	private Where check;

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
