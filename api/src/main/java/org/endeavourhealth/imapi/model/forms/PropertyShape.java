package org.endeavourhealth.imapi.model.forms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@JsonPropertyOrder({"label","comment","name","order","minCount","maxCount","componentType","path","dataType","class","node",
"function","entity","value","validation","search"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PropertyShape {
	private String label;
	private String comment;
	private String name;
	private int order;
	private Integer minCount;
	private Integer maxCount;
	private TTIriRef path;
	private TTIriRef datatype;
	private TTIriRef clazz;
	private TTIriRef node;
	private TTIriRef function;
	private TTIriRef isIri;
	private String isNumericValue;
	private String isTextValue;
	private TTIriRef componentType;
	private TTIriRef validation;
	private TTIriRef search;

	public TTIriRef getFunction() {
		return function;
	}

	public PropertyShape setFunction(TTIriRef function) {
		this.function = function;
		return this;
	}

	public String getIsTextValue() {
		return isTextValue;
	}

	public PropertyShape setIsTextValue(String isTextValue) {
		this.isTextValue = isTextValue;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public PropertyShape setLabel(String label) {
		this.label = label;
		return this;
	}

	public String getComment() {
		return comment;
	}

	public PropertyShape setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public String getName() {
		return name;
	}

	public PropertyShape setName(String name) {
		this.name = name;
		return this;
	}

	public int getOrder() {
		return order;
	}

	public PropertyShape setOrder(int order) {
		this.order = order;
		return this;
	}

	public Integer getMinCount() {
		return minCount;
	}

	public PropertyShape setMinCount(Integer minCount) {
		this.minCount = minCount;
		return this;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public PropertyShape setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
		return this;
	}

	public TTIriRef getPath() {
		return path;
	}

	public PropertyShape setPath(TTIriRef path) {
		this.path = path;
		return this;
	}

	public TTIriRef getDatatype() {
		return datatype;
	}

	public PropertyShape setDatatype(TTIriRef datatype) {
		this.datatype = datatype;
		return this;
	}

	public TTIriRef getClazz() {
		return clazz;
	}

	public PropertyShape setClazz(TTIriRef clazz) {
		this.clazz = clazz;
		return this;
	}

	public TTIriRef getNode() {
		return node;
	}

	public PropertyShape setNode(TTIriRef node) {
		this.node = node;
		return this;
	}

	public TTIriRef getIsIri() {
		return isIri;
	}

	public PropertyShape setIsIri(TTIriRef isIri) {
		this.isIri = isIri;
		return this;
	}

	public String getIsNumericValue() {
		return isNumericValue;
	}

	public PropertyShape setIsNumericValue(String isNumericValue) {
		this.isNumericValue = isNumericValue;
		return this;
	}

	public TTIriRef getComponentType() {
		return componentType;
	}

	public PropertyShape setComponentType(TTIriRef componentType) {
		this.componentType = componentType;
		return this;
	}

	public TTIriRef getValidation() {
		return validation;
	}

	public PropertyShape setValidation(TTIriRef validation) {
		this.validation = validation;
		return this;
	}

	public TTIriRef getSearch() {
		return search;
	}

	public PropertyShape setSearch(TTIriRef search) {
		this.search = search;
		return this;
	}
}
