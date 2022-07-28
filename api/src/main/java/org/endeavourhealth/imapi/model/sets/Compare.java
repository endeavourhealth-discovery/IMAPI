package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.zip.DataFormatException;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Compare {

	Comparison comparison= Comparison.EQUAL;
	String valueData;
	String valueVariable;
	String valueSelect;


	public String getValueSelect() {
		return valueSelect;
	}

	public Compare setValueSelect(String valueSelect) {
		this.valueSelect = valueSelect;
		return this;
	}

	public Comparison getComparison() {
		return comparison;
	}

	@JsonSetter
	public Compare setComparison(Comparison comparison) {
		this.comparison = comparison;
		return this;
	}


	@JsonSetter
	public Compare setComparison(String comp) throws DataFormatException {
		comp= comp.toLowerCase();
		switch (comp) {
			case "=":
				this.comparison = Comparison.EQUAL;
				break;
			case "<=":
				this.comparison = Comparison.LESS_THAN_OR_EQUAL;
				break;
			case "<":
				this.comparison = Comparison.LESS_THAN;
				break;
			case ">=":
				this.comparison = Comparison.GREATER_THAN_OR_EQUAL;
				break;
			case ">":
				this.comparison = Comparison.GREATER_THAN;
				break;
			case "like" :
				this.comparison= Comparison.LIKE;
				break;
			default:
				if (comp.startsWith("e"))
					this.comparison = Comparison.EQUAL;
				else if (comp.startsWith("less_than_or_e"))
					this.comparison = Comparison.LESS_THAN_OR_EQUAL;
				else if (comp.startsWith("less"))
					this.comparison = Comparison.LESS_THAN;
				else if (comp.startsWith("greater_than_or_e"))
					this.comparison = Comparison.GREATER_THAN_OR_EQUAL;
				else if (comp.startsWith("great"))
					this.comparison = Comparison.GREATER_THAN;
				else if (comp.startsWith("starts"))
					this.comparison = Comparison.STARTS_WITH;
				else
					throw new DataFormatException("Invalid comparison operator - " + comp);
		}

		return this;
	}

	public String getValueData() {
		return valueData;
	}


	public Compare setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}

	public String getValueVariable() {
		return valueVariable;
	}

	public Compare setValueVariable(String valueVariable) {
		this.valueVariable = valueVariable;
		return this;
	}
}
