package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.imq.Argument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@JsonPropertyOrder({"iri","label","comment","name","order","minCount","maxCount","componentType","path","dataType","class","node",
"functionClause","validation","search","select","argument","valueVariable","isIri","isTextValue","isNumericValue","forceIsValue", "builderChild"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PropertyShape {
	private String label;
	private String comment;
	private String name;
	private int order;
	private Integer minCount;
	private Integer maxCount;
	private List<PropertyShape> property;
	private TTIriRef path;
	private TTIriRef datatype;
	private TTIriRef clazz;
	private Set<TTIriRef> node;
	private TTIriRef isIri;
	private String isNumericValue;
	private String isTextValue;
	private TTIriRef componentType;
	private TTIriRef validation;
	private String validationErrorMessage;
	private TTIriRef search;
	private TTIriRef function;
	private List<Argument> argument;
	private String valueVariable;
	private TTIriRef valueIri;
	private List<TTIriRef> select;
	private Boolean builderChild;
	private NodeShape expression;
	private Boolean forceIsValue;

	public Boolean getForceIsValue() {
		return forceIsValue;
	}

	public PropertyShape setForceIsValue(Boolean forceIsValue) {
		this.forceIsValue = forceIsValue;
		return this;
	}

	public String getValidationErrorMessage() {
		return validationErrorMessage;
	}

	public void setValidationErrorMessage(String validationErrorMessage) {
		this.validationErrorMessage = validationErrorMessage;
	}

	public Boolean getBuilderChild() {
		return builderChild;
	}

	public void setBuilderChild(Boolean builderChild) {
		this.builderChild = builderChild;
	}

	public NodeShape getExpression() {
		return expression;
	}

	public PropertyShape setExpression(NodeShape expression) {
		this.expression = expression;
		return this;
	}

	public List<TTIriRef> getSelect() {
		return select;
	}

	public PropertyShape setSelect(List<TTIriRef> select) {
		this.select = select;
		return this;
	}

	public String getValueVariable() {
		return valueVariable;
	}

	public PropertyShape setValueVariable(String valueVariable) {
		this.valueVariable = valueVariable;
		return this;
	}

	public List<Argument> getArgument() {
		return argument;
	}

	@JsonSetter
	public PropertyShape setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}

	public PropertyShape addArgument(Argument arg){
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(arg);
		return this;
	}

	public PropertyShape argument(Consumer<Argument> builder){
		Argument arg= new Argument();
		this.addArgument(arg);
		builder.accept(arg);
		return this;

	}

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

	public Set<TTIriRef> getNode() {
		return node;
	}

	public PropertyShape setNode(Set<TTIriRef> node) {
		this.node = node;
		return this;
	}

	public PropertyShape addNode(TTIriRef node){
		if (this.node==null)
			this.node= new HashSet<>();
		this.node.add(node);
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

	public List<PropertyShape> getProperty() {
		return property;
	}

	public PropertyShape setProperty(List<PropertyShape> property) {
		this.property = property;
		return this;
	}

	public PropertyShape addProperty(PropertyShape prop) {
		if (null==this.property) this.property = new ArrayList<>();
		this.property.add(prop);
		return this;
	}

	public TTIriRef getValueIri() {
		return valueIri;
	}

	public PropertyShape setValueIri(TTIriRef valueIri) {
		this.valueIri = valueIri;
		return this;
	}
}
