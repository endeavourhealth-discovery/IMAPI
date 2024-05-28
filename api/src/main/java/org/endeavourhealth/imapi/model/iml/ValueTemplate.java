package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ValueTemplate extends Entity{
	private String parameter;
	private Integer order;
	private String label;
	private TTIriRef valueType;
	private Object defaultValue;
	private List<Object> valueOption;

	public TTIriRef getValueType() {
		return valueType;
	}

	public ValueTemplate setValueType(TTIriRef valueType) {
		this.valueType = valueType;
		return this;
	}

	public List<Object> getValueOption() {
		return valueOption;
	}

	public String getParameter() {
		return parameter;
	}

	public ValueTemplate setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public ValueTemplate setValueOption(List<Object> valueOption) {
		this.valueOption = valueOption;
		return this;
	}
		public ValueTemplate addValueOption (Object valueOption){
			if (this.valueOption == null) {
				this.valueOption = new ArrayList<>();
			}
			this.valueOption.add(valueOption);
			return this;
		}


	public Integer getOrder() {
		return order;
	}

	public ValueTemplate setOrder(Integer order) {
		this.order = order;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public ValueTemplate setLabel(String label) {
		this.label = label;
		return this;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public ValueTemplate setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
}
