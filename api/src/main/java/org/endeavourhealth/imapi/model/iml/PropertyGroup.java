package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"label","comment","name","order","minCount","maxCount","property"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PropertyGroup {
	private String label;
	private String comment;
	private String name;
	private int order;
	private Integer minCount;
	private Integer maxCount;
	private List<PropertyShape> property;
	private TTIriRef componentType;
	private List<PropertyGroup> subGroup;
	private TTIriRef path;
	private TTIriRef validation;
	private String validationErrorMessage;
	private TTIriRef function;
	private TTIriRef valueIri;
	private Boolean builderChild;

	public Boolean isBuilderChild() {
		return builderChild;
	}

	public void setBuilderChild(Boolean builderChild) {
		this.builderChild = builderChild;
	}

	public TTIriRef getValueIri() {
		return valueIri;
	}

	public PropertyGroup setValueIri(TTIriRef valueIri) {
		this.valueIri = valueIri;
		return this;
	}

	public TTIriRef getFunction() {
		return function;
	}

	public PropertyGroup setFunction(TTIriRef function) {
		this.function = function;
		return this;
	}

	public String getValidationErrorMessage() {
		return validationErrorMessage;
	}

	public void setValidationErrorMessage(String validationErrorMessage) {
		this.validationErrorMessage = validationErrorMessage;
	}

	public TTIriRef getValidation() {
		return validation;
	}

	public PropertyGroup setValidation(TTIriRef validation) {
		this.validation = validation;
		return this;
	}

	public List<PropertyGroup> getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(List<PropertyGroup> subGroup) {
		this.subGroup = subGroup;
	}

	public TTIriRef getPath() {
		return path;
	}

	public void setPath(TTIriRef path) {
		this.path = path;
	}

	public TTIriRef getComponentType() {
		return componentType;
	}

	public void setComponentType(TTIriRef componentType) {
		this.componentType = componentType;
	}

	public String getLabel() {
		return label;
	}

	public PropertyGroup setLabel(String label) {
		this.label = label;
		return this;
	}

	public String getComment() {
		return comment;
	}

	public PropertyGroup setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public String getName() {
		return name;
	}

	public PropertyGroup setName(String name) {
		this.name = name;
		return this;
	}

	public int getOrder() {
		return order;
	}

	public PropertyGroup setOrder(int order) {
		this.order = order;
		return this;
	}

	public Integer getMinCount() {
		return minCount;
	}

	public PropertyGroup setMinCount(Integer minCount) {
		this.minCount = minCount;
		return this;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public PropertyGroup setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
		return this;
	}

	public List<PropertyShape> getProperty() {
		return property;
	}

	@JsonSetter
	public PropertyGroup setProperty(List<PropertyShape> property) {
		this.property = property;
		return this;
	}

	public PropertyGroup addProperty(PropertyShape prop){
		if (this.property==null)
			this.property= new ArrayList<>();
		this.property.add(prop);
		return this;
	}

	@JsonIgnore
	public PropertyGroup property(Consumer<PropertyShape> builder){
		PropertyShape property= new PropertyShape();
		this.addProperty(property);
		builder.accept(property);
		return this;
	}
}