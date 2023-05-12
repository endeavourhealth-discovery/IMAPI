package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

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
	private List<PropertyShape> property;

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



	public List<PropertyShape> getProperty() {
		return property;
	}

	@JsonSetter
	public FormGenerator setProperty(List<PropertyShape> property) {
		this.property = property;
		return this;
	}

	public FormGenerator addProperty(PropertyShape property){
		if (null == this.property)
			this.property = new ArrayList<>();
		this.property.add(property);
		return this;
	}

	@JsonIgnore
	public FormGenerator property(Consumer<PropertyShape> builder){
		PropertyShape property= new PropertyShape();
		this.addProperty(property);
		builder.accept(property);
		return this;
	}
	@JsonIgnore
	public String asJson() throws JsonProcessingException {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            return om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(this);
        }

	}
}
