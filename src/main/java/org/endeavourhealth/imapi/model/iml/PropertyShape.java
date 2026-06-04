package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.imq.ArgumentExtended;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"iri", "label", "comment", "name", "order", "minCount", "maxCount", "componentType", "path", "datatype", "class", "node",
  "functionClause", "validation", "search", "select", "argument", "valueVariable", "isIri", "isTextValue", "isNumericValue", "forceIsValue", "builderChild", "showTitle"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PropertyShape {
  @Getter
  private TTIriRefExtended group;
  @Getter
  private String label;
  @Getter
  private String comment;
  @Getter
  private String name;
  @Getter
  private Boolean showTitle;
  private int order;
  @Getter
  private Integer minCount;
  @Getter
  private Integer maxCount;
  @Getter
  private List<PropertyShape> property;
  private TTIriRefExtended path;
  @Getter
  private PropertyRange datatype;
  @Getter
  private PropertyRange clazz;
  @Getter
  private PropertyRange node;
  @Getter
  private TTIriRefExtended isIri;
  @Getter
  private String isNumericValue;
  @Getter
  private String isTextValue;
  private TTIriRefExtended componentType;
  @Getter
  private TTIriRefExtended validation;
  @Getter
  private String validationErrorMessage;
  @Getter
  private TTIriRefExtended search;
  @Getter
  private TTIriRefExtended function;
  @Getter
  private List<ArgumentExtended> argument;
  @Getter
  private List<ParameterShape> parameter;
  @Getter
  private String valueVariable;
  @Getter
  private TTIriRefExtended valueIri;
  @Getter
  private List<TTIriRefExtended> select;
  @Getter
  private Boolean builderChild;
  @Getter
  private NodeShape expression;
  @Getter
  private Boolean forceIsValue;
  @Getter
  private ArrayButtonsExtended arrayButtons;
  @Getter
  private Object hasValue;
  @Getter
  private TTIriRefExtended hasValueType;
  @Getter
  private String definition;
  @Getter
  private String ascending;
  @Getter
  private String descending;
  @Getter
  private boolean orderable;
  @Getter
  @Setter
  private TTIriRefExtended hasValueSet;
  @Getter
  @Setter
  private boolean definingProperty;
  @Getter
  @Setter
  private TTIriRefExtended isValidEntity;
  @Getter
  @Setter
  private Boolean highCardinality;
  @Getter
  private List<ArgumentExtended> isValidArguments;
  private TTIriRefExtended inversePath;
  private boolean generic;

  public boolean isGeneric() {
    return generic;
  }

  public PropertyShape setGeneric(boolean generic) {
    this.generic = generic;
    return this;
  }

  public TTIriRefExtended getInversePath() {
    return inversePath;
  }

  public PropertyShape setInversePath(TTIriRefExtended inversePath) {
    this.inversePath = inversePath;
    return this;
  }


  public PropertyShape setAscending(String ascending) {
    this.ascending = ascending;
    return this;
  }

  public PropertyShape setDescending(String descending) {
    this.descending = descending;
    return this;
  }

  public PropertyShape setOrderable(boolean orderable) {
    this.orderable = orderable;
    return this;
  }


  public PropertyShape setParameter(List<ParameterShape> parameter) {
    this.parameter = parameter;
    return this;
  }

  public PropertyShape addParameter(ParameterShape parameter) {
    if (this.parameter == null) {
      this.parameter = new ArrayList<>();
    }
    this.parameter.add(parameter);
    return this;
  }

  public PropertyShape parameter(Consumer<ParameterShape> builder) {
    ParameterShape parameter = new ParameterShape();
    addParameter(parameter);
    builder.accept(parameter);
    return this;
  }


  public PropertyShape setGroup(TTIriRefExtended group) {
    this.group = group;
    return this;
  }


  public PropertyShape setHasValue(Object hasValue) {
    this.hasValue = hasValue;
    return this;
  }

  public PropertyShape setHasValueType(TTIriRefExtended hasValueType) {
    this.hasValueType = hasValueType;
    return this;
  }

  public PropertyShape setDefinition(String definition) {
    this.definition = definition;
    return this;
  }


  public PropertyShape setArrayButtons(ArrayButtonsExtended arrayButtons) {
    this.arrayButtons = arrayButtons;
    return this;
  }

  public PropertyShape setForceIsValue(Boolean forceIsValue) {
    this.forceIsValue = forceIsValue;
    return this;
  }

  public void setValidationErrorMessage(String validationErrorMessage) {
    this.validationErrorMessage = validationErrorMessage;
  }

  public void setBuilderChild(Boolean builderChild) {
    this.builderChild = builderChild;
  }

  public PropertyShape setExpression(NodeShape expression) {
    this.expression = expression;
    return this;
  }

  public PropertyShape setSelect(List<TTIriRefExtended> select) {
    this.select = select;
    return this;
  }

  public PropertyShape setValueVariable(String valueVariable) {
    this.valueVariable = valueVariable;
    return this;
  }

  @JsonSetter
  public PropertyShape setArgument(List<ArgumentExtended> argument) {
    this.argument = argument;
    return this;
  }

  public PropertyShape addArgument(ArgumentExtended arg) {
    if (this.argument == null)
      this.argument = new ArrayList<>();
    this.argument.add(arg);
    return this;
  }

  public PropertyShape argument(Consumer<ArgumentExtended> builder) {
    ArgumentExtended arg = new ArgumentExtended();
    this.addArgument(arg);
    builder.accept(arg);
    return this;

  }

  @JsonSetter
  public PropertyShape setFunction(TTIriRefExtended function) {
    this.function = function;
    return this;
  }

  public PropertyShape setIsTextValue(String isTextValue) {
    this.isTextValue = isTextValue;
    return this;
  }

  public PropertyShape setLabel(String label) {
    this.label = label;
    return this;
  }

  public PropertyShape setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public PropertyShape setName(String name) {
    this.name = name;
    return this;
  }

  public PropertyShape setShowTitle(Boolean showTitle) {
    this.showTitle = showTitle;
    return this;
  }

  @JsonProperty(required = true)
  public int getOrder() {
    return order;
  }

  public PropertyShape setOrder(int order) {
    this.order = order;
    return this;
  }

  public PropertyShape setMinCount(Integer minCount) {
    this.minCount = minCount;
    return this;
  }

  public PropertyShape setMaxCount(Integer maxCount) {
    this.maxCount = maxCount;
    return this;
  }

  @JsonProperty(required = true)
  public TTIriRefExtended getPath() {
    return path;
  }

  @JsonSetter
  public PropertyShape setPath(TTIriRefExtended path) {
    this.path = path;
    return this;
  }

  @JsonSetter
  public PropertyShape setDatatype(PropertyRange datatype) {
    this.datatype = datatype;
    return this;
  }

  @JsonSetter
  public PropertyShape setClazz(PropertyRange clazz) {
    this.clazz = clazz;
    return this;
  }

  public PropertyShape setNode(PropertyRange node) {
    this.node = node;
    return this;
  }


  @JsonSetter
  public PropertyShape setIsIri(TTIriRefExtended isIri) {
    this.isIri = isIri;
    return this;
  }

  public PropertyShape setIsNumericValue(String isNumericValue) {
    this.isNumericValue = isNumericValue;
    return this;
  }

  @JsonProperty(required = true)
  public TTIriRefExtended getComponentType() {
    return componentType;
  }

  @JsonSetter
  public PropertyShape setComponentType(TTIriRefExtended componentType) {
    this.componentType = componentType;
    return this;
  }

  @JsonSetter
  public PropertyShape setValidation(TTIriRefExtended validation) {
    this.validation = validation;
    return this;
  }

  @JsonSetter
  public PropertyShape setSearch(TTIriRefExtended search) {
    this.search = search;
    return this;
  }

  public PropertyShape setProperty(List<PropertyShape> property) {
    this.property = property;
    return this;
  }

  public PropertyShape addProperty(PropertyShape prop) {
    if (null == this.property) this.property = new ArrayList<>();
    this.property.add(prop);
    return this;
  }

  @JsonSetter
  public PropertyShape setValueIri(TTIriRefExtended valueIri) {
    this.valueIri = valueIri;
    return this;
  }
}
