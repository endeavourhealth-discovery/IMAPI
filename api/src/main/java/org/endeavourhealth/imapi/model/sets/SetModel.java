package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTIri;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder ({"iri","name","description","var","subset","match"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SetModel extends TTIri{

	private TTIriRef graph;
	List<PropertyMap> property;
	private List<SetModel> subset;

	Match match;

	public Match getMatch() {
		return match;
	}

	public SetModel setMatch(Match match) {
		this.match = match;
		return this;
	}

	public TTIriRef getGraph() {
		return graph;
	}

	public SetModel setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}






	public List<SetModel> getSubset() {
		return subset;
	}

	public SetModel setSubset(List<SetModel> setModels) {
		this.subset = setModels;
		return this;
	}
	public SetModel addSubset(SetModel group){
		if (this.subset ==null)
			this.subset = new ArrayList<>();
		this.subset.add(group);
		return this;
	}

	public List<PropertyMap> getProperty() {
		return property;
	}

	public SetModel setProperty(List<PropertyMap> property) {
		this.property = property;
		return this;
	}

	public SetModel addProperty(PropertyMap property){
		if (this.property==null)
			this.property= new ArrayList<>();
		this.property.add(property);
		return this;
	}






	@JsonIgnore
	public String getasJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		return objectMapper.writeValueAsString(this);
	}







}
