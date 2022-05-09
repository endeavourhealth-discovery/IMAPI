package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Locale;
import java.util.zip.DataFormatException;

public class Compare {

	Comparison comparison= Comparison.EQUAL;
	String valueData;


	public Comparison getComparison() {
		return comparison;
	}

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


	@JsonIgnore
	public String getValue() {
		return valueData;
	}

	public Compare setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}


	@JsonIgnore
	public Compare setValue(String valueData) {
		this.valueData = valueData;
		return this;
	}



}
