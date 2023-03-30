package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Optional;

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

		public static Optional<Operator> get(String val) {
		  return Arrays.stream(Operator.values())
			 .filter(op -> op.value.equals(val))
			 .findFirst();
	}

		// enum constructor - cannot be public or protected
    private Operator(String value)
		{
			this.value = value;
		}
	}
