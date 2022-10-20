package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MapRule {
	private List<MapRuleSource> source;
	private List<MapRuleTransform> target;

	public List<MapRuleSource> getSource() {
		return source;
	}

	@JsonSetter
	public MapRule setSource(List<MapRuleSource> source) {
		this.source = source;
		return this;
	}

	public MapRule addSource(MapRuleSource source){
		if (this.source==null)
			this.source= new ArrayList<>();
		this.source.add(source);
		return this;
	}

	public MapRule source(Consumer<MapRuleSource> builder){
		MapRuleSource source= new MapRuleSource();
		addSource(source);
		builder.accept(source);
		return this;
	}

	public List<MapRuleTransform> getTarget() {
		return target;
	}


	@JsonSetter
	public MapRule setTarget(List<MapRuleTransform> target) {
		this.target = target;
		return this;
	}

	public MapRule addTarget(MapRuleTransform target){
		if (this.target==null)
			this.target= new ArrayList<>();
		this.target.add(target);
		return this;
	}

	public MapRule target(Consumer<MapRuleTransform> builder){
		MapRuleTransform target= new MapRuleTransform();
		addTarget(target);
		builder.accept(target);
		return this;
	}

}
