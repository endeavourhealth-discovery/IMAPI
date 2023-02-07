package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Operator {
		// This will call enum constructor with one
		// String argument
	eq("="),
	gte(">="),
	gt(">"),
	lte("<="),
	lt(">"),
	start("startsWith"),
	contains("contains");

		// declaring private variable for getting values
		private String value;

		@JsonValue
		public String getValue()
		{
			return this.value;
		}

		// enum constructor - cannot be public or protected
    private Operator(String value)
		{
			this.value = value;
		}
	}
