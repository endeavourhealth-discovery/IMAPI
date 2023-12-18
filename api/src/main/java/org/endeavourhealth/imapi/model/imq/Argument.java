package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Argument {

	private String parameter;
	private String valueData;
	private String valueVariable;
	private TTIriRef valueIri;
	private List<TTIriRef> valueIriList;
	private List<String> valueDataList;
	private Object valueObject;



	public List<TTIriRef> getValueIriList() {
		return valueIriList;
	}

	public Argument setValueIriList(List<TTIriRef> valueIriList) {
		this.valueIriList = valueIriList;
		return this;
	}


	public Object getValueObject() {
		return valueObject;
	}

	public Argument setValueObject(Object valueObject) {
		this.valueObject = valueObject;
		return this;
	}

	public List<String> getValueDataList() {
		return valueDataList;
	}

	public Argument setValueDataList(List<String> valueDataList) {
		this.valueDataList = valueDataList;
		return this;
	}

	public Argument addToValueDataList(String value){
		if (this.valueDataList ==null)
			this.valueDataList = new ArrayList<>();
		this.valueDataList.add(value);
		return this;
	}

	public Argument addToValueIriList(TTIriRef value){
		if (this.valueIriList ==null)
			this.valueIriList = new ArrayList<>();
		this.valueIriList.add(value);
		return this;
	}

	public TTIriRef getValueIri() {
		return valueIri;
	}

	public Argument setValueIri(TTIriRef valueIri) {
		this.valueIri = valueIri;
		return this;
	}
	public Argument setValueIri(Vocabulary valueIri) {
		this.valueIri = valueIri.asTTIriRef();
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