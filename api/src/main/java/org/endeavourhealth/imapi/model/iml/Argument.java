package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Argument {

	private String parameter;
	private String valueData;
	private String valueVariable;
	private Compare valueFrom;
	private TTIriRef valueIri;
	private List<String> valueList;
	private Object valueObject;


	public Object getValueObject() {
		return valueObject;
	}

	public Argument setValueObject(Object valueObject) {
		this.valueObject = valueObject;
		return this;
	}

	public List<String> getValueList() {
		return valueList;
	}

	public Argument setValueList(List<String> valueList) {
		this.valueList = valueList;
		return this;
	}

	public Argument addToValueList(String value){
		if (this.valueList==null)
			this.valueList= new ArrayList<>();
			this.valueList.add(value);
			return this;
	}

	public TTIriRef getValueIri() {
		return valueIri;
	}

	public Argument setValueIri(TTIriRef valueIri) {
		this.valueIri = valueIri;
		return this;
	}

	public Compare getValueFrom() {
		return valueFrom;
	}

	public Argument setValueFrom(Compare valueFrom) {
		this.valueFrom = valueFrom;
		return this;
	}

	public String getValueVariable() {
		return valueVariable;
	}

	public Argument setValueVariable(String valueVariable) {
		this.valueVariable = valueVariable;
		return this;
	}

	public String getParameter() {
		return parameter;
	}

	public Argument setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public String getValueData() {
		return valueData;
	}

	public Argument setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}

}
