package org.endeavourhealth.imapi.model.map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Property;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"iri","name","description","sourceType","where","targetType","propertyMap"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MapObject extends TTIriRef {
	private Match where;
	private String sourceType;
	private String targetType;
	private List<MapProperty> propertyMap;

	public Match getWhere() {
		return where;
	}

	public MapObject setWhere(Match where) {
		this.where = where;
		return this;
	}


	public MapObject where (Consumer<Match> builder){
		this.where= new Match();
		builder.accept(this.where);
		return this;
	}

	public String getSourceType() {
		return sourceType;
	}

	public MapObject setSourceType(String sourceType) {
		this.sourceType = sourceType;
		return this;
	}

	public String getTargetType() {
		return targetType;
	}

	public MapObject setTargetType(String targetType) {
		this.targetType = targetType;
		return this;
	}

	public List<MapProperty> getPropertyMap() {
		return propertyMap;
	}

	@JsonSetter
	public MapObject setPropertyMap(List<MapProperty> propertyMap) {
		this.propertyMap = propertyMap;
		return this;
	}


	public MapObject addPropertyMap(MapProperty objectMap){
		if (this.propertyMap ==null) {
			this.propertyMap = new ArrayList<>();
		}
		this.propertyMap.add(objectMap);
		return this;
	}
	public MapObject propertyMap(Consumer<MapProperty> builder){
		MapProperty objectMap= new MapProperty();
		addPropertyMap(objectMap);
		builder.accept(objectMap);
		return this;
	}



	@Override
	public MapObject setDescription(String description) {
		super.setDescription(description);
		return this;
	}

	@Override
	public MapObject setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	@Override
	public MapObject setName(String name) {
		super.setName(name);
		return this;
	}
}
