package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;
import java.util.function.Consumer;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Argument {

	private String parameter;
	private String valueData;
	private String valueVariable;
	private Select valueSelect;
	private TTIriRef valueIri;
	private String valueText;


	public TTIriRef getValueIri() {
		return valueIri;
	}

	public Argument setValueIri(TTIriRef valueIri) {
		this.valueIri = valueIri;
		return this;
	}

	public String getValueText() {
		return valueText;
	}

	public Argument setValueText(String valueText) {
		this.valueText = valueText;
		return this;
	}

	public Select getValueSelect() {
		return valueSelect;
	}

	@JsonSetter
	public Argument setValueSelect(Select valueSelect) {
		this.valueSelect = valueSelect;
		return this;
	}

	@JsonIgnore
	public Argument valueSelect(Consumer<Select> builder){
		Select select= new Select();
		this.valueSelect= select;
		builder.accept(select);
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

	public Object getValueData() {
		return valueData;
	}

	public Argument setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}

}
