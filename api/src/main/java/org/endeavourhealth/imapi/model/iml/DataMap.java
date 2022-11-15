package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DataMap extends TTIriRef {
	private TTVariable source;
	private List<TTVariable> targets;
	private List<MapRule> rules;



	public List<MapRule> getRules() {
		return rules;
	}

	@JsonSetter
	public DataMap setRules(List<MapRule> rules) {
		this.rules = rules;
		return this;
	}

	public DataMap addRule(MapRule rule){
		if (this.rules ==null) {
			this.rules = new ArrayList<>();
		}
		this.rules.add(rule);
		return this;
	}

	public DataMap rule(Consumer<MapRule> builder){
		MapRule rule= new MapRule();
		this.addRule(rule);
		builder.accept(rule);
		return this;
	}


	public TTVariable getSource() {
		return source;
	}

	public DataMap setSource(TTVariable source) {
		this.source = source;
		return this;
	}

	public List<TTVariable> getTargets() {
		return targets;
	}

	public DataMap setTargets(List<TTVariable> targets) {
		this.targets = targets;
		return this;
	}

	public DataMap addTarget(TTVariable target){
		if (this.targets==null)
			this.targets= new ArrayList<>();
		this.targets.add(target);
		return this;
	}

	@Override
	public DataMap setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	@Override
	public DataMap setName(String name) {
		super.setName(name);
		return this;
	}
}
