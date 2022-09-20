package org.endeavourhealth.imapi.model.iml;

import java.util.HashMap;
import java.util.Map;

public class Binding {
	private Map<String,String> predicateBinding = new HashMap<>();
	private Map<String,Binding> predicateObject = new HashMap<>();

	public Map<String, String> getPredicateBinding() {
		return predicateBinding;
	}

	public Binding setPredicateBinding(Map<String, String> predicateBinding) {
		this.predicateBinding = predicateBinding;
		return this;
	}

	public Map<String, Binding> getPredicateObject() {
		return predicateObject;
	}

	public Binding setPredicateObject(Map<String, Binding> predicateObject) {
		this.predicateObject = predicateObject;
		return this;
	}
}
