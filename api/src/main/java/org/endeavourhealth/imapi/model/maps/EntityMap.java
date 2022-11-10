package org.endeavourhealth.imapi.model.maps;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@JsonPropertyOrder({"source","inputs","sourceTargetMap","SourceTargetMaps"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class EntityMap extends TTIriRef {
	private TTVariable source;
	private List<TTVariable> targets;
	private List<TTVariable> inputs;
	private List<SourceTargetMap> propertyMaps;

	public List<TTVariable> getTargets() {
		return targets;
	}

	@JsonSetter
	public EntityMap setTargets(List<TTVariable> targets) {
		this.targets = targets;
		return this;
	}

	public EntityMap addTarget(TTVariable target){
		if (this.targets==null)
			this.targets= new ArrayList<>();
		this.targets.add(target);
		return this;
	}

	public EntityMap target(Consumer<TTVariable> builder) {
		TTVariable target= new TTVariable();
		addTarget(target);
		builder.accept(target);
		return this;
	}



	@Override
	public EntityMap setIri(String iri) {
		super.setIri(iri);
		return this;
	}


	public TTVariable getSource() {
		return source;
	}

	@JsonSetter
	public EntityMap setSource(TTVariable source) {
		this.source = source;
		return this;
	}

	public List<TTVariable> getInputs() {
		return inputs;
	}

	public EntityMap setInputs(List<TTVariable> inputs) {
		this.inputs = inputs;
		return this;
	}

	public List<SourceTargetMap> getPropertyMaps() {
		return propertyMaps;
	}

	@JsonSetter
	public EntityMap setPropertyMaps(List<SourceTargetMap> SourceTargetMaps) {
		this.propertyMaps = SourceTargetMaps;
		return this;
	}

	public EntityMap addPropertyMap(SourceTargetMap map){
		if (this.propertyMaps==null)
			this.propertyMaps= new ArrayList<>();
		this.propertyMaps.add(map);
		return this;
	}

	public EntityMap propertyMap(Consumer<SourceTargetMap> builder){
		SourceTargetMap map = new SourceTargetMap();
		addPropertyMap(map);
		builder.accept(map);
		return this;
	}


}
