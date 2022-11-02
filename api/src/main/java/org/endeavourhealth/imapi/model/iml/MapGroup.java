package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Mapping group containing transformation rules as part of a transformation map
 */
@JsonPropertyOrder({"name","extend","source","target","rule"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MapGroup {
	private String name;
	private String extend;
	private TTAlias source;
	private TTAlias target;
	private List<MapRule> rule;

	public String getName() {
		return name;
	}

	public MapGroup setName(String name) {
		this.name = name;
		return this;
	}

	@JsonProperty("extends")
	public String getExtends() {
		return extend;
	}

	@JsonProperty("extends")
	@JsonSetter
	public MapGroup setExtends(String extend) {
		this.extend = extend;
		return this;
	}

	public TTAlias getSource() {
		return source;
	}

	public MapGroup setSource(TTAlias source) {
		this.source = source;
		return this;
	}

	public TTAlias getTarget() {
		return target;
	}

	public MapGroup setTarget(TTAlias target) {
		this.target = target;
		return this;
	}

	public List<MapRule> getRule() {
		return rule;
	}

	public MapGroup setRule(List<MapRule> rule) {
		this.rule = rule;
		return this;
	}

	public MapGroup addRule(MapRule rule) {
		if (this.rule ==null)
			this.rule= new ArrayList<>();
		this.rule.add(rule);
		return this;
	}

	public MapGroup rule(Consumer<MapRule> builder){
		MapRule rule= new MapRule();
		addRule(rule);
		builder.accept(rule);
		return this;
	}
}
