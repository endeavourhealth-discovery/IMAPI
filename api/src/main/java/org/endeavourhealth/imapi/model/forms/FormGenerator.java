package org.endeavourhealth.imapi.model.forms;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.TableStyleType;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"@id","status","label","comment","targetShape","type","isContainedIn","subClassOf","group","scheme"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class FormGenerator {
	private String iri;
	private TTIriRef status;
	private TTIriRef scheme;
	private String label;
	private String comment;
	private List<TTIriRef> type;
	private TTIriRef targetShape;
	private List<TTIriRef> isContainedIn;
	private List<TTIriRef> subClassOf;
	private List<PropertyGroup> group;

	public String getIri() {
		return iri;
	}

	public FormGenerator setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public TTIriRef getStatus() {
		return status;
	}

	public FormGenerator setStatus(TTIriRef status) {
		this.status = status;
		return this;
	}

	public TTIriRef getScheme() {
		return scheme;
	}

	public FormGenerator setScheme(TTIriRef scheme) {
		this.scheme = scheme;
		return this;
	}

	public List<TTIriRef> getType() {
		return type;
	}

	@JsonProperty("@id")
	public String getId() {
		return iri;
	}

	public FormGenerator setType(List<TTIriRef> type) {
		this.type = type;
		return this;
	}

	public List<TTIriRef> getIsContainedIn() {
		return isContainedIn;
	}

	public FormGenerator setIsContainedIn(List<TTIriRef> isContainedIn) {
		this.isContainedIn = isContainedIn;
		return this;
	}

	public List<TTIriRef> getSubClassOf() {
		return subClassOf;
	}

	public FormGenerator setSubClassOf(List<TTIriRef> subClassOf) {
		this.subClassOf = subClassOf;
		return this;
	}

	public FormGenerator setId(String id) {
		this.iri= id;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public FormGenerator setLabel(String label) {
		this.label = label;
		return this;
	}

	public String getComment() {
		return comment;
	}

	public FormGenerator setComment(String comment) {
		this.comment = comment;
		return this;
	}


	public TTIriRef getTargetShape() {
		return targetShape;
	}

	public FormGenerator setTargetShape(TTIriRef targetShape) {
		this.targetShape = targetShape;
		return this;
	}



	public List<PropertyGroup> getGroup() {
		return group;
	}

	@JsonSetter
	public FormGenerator setGroup(List<PropertyGroup> group) {
		this.group = group;
		return this;
	}

	public FormGenerator addGroup(PropertyGroup group){
		if (this.group==null)
			this.group= new ArrayList<>();
		this.group.add(group);
		return this;
	}

	@JsonIgnore
	public FormGenerator group(Consumer<PropertyGroup> builder){
		PropertyGroup group= new PropertyGroup();
		this.addGroup(group);
		builder.accept(group);
		return this;
	}
	@JsonIgnore
	public String asJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		return objectMapper.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(this);

	}
}
