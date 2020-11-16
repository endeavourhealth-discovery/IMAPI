package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"property","inverseOf","quantificationType","min","max","valueType","valueData","Individual"})
public class ObjectPropertyValue extends QuantificationImpl{

    private ConceptReference property;
    private ConceptReference inverseOf;
    private ConceptReference valueType;
    private String valueData;
    private String individual;
    private ClassExpression expression;

    // subClassExpression

    @JsonProperty("Property")
    public ConceptReference getProperty() {
        return property;
    }

    public ObjectPropertyValue setProperty(ConceptReference property) {
        this.property = property;
        return this;
    }

    @JsonProperty("ValueType")
    public ConceptReference getValueType() {
        return valueType;
    }

    public ObjectPropertyValue setValueType(ConceptReference valueType) {
        this.valueType = valueType;
        return this;
    }

    @JsonIgnore
    public ObjectPropertyValue setValueType(String valueType) {
        this.valueType = new ConceptReference(valueType);
        return this;
    }

    @JsonProperty("InverseOf")
    public ConceptReference getInverseOf() {
        return inverseOf;
    }

    public ObjectPropertyValue setInverseOf(ConceptReference inverseOf) {
        this.inverseOf = inverseOf;
        return this;
    }


    @JsonProperty("Individual")
    public String getIndividual() {
        return individual;
    }

    public ObjectPropertyValue setIndividual(String individual) {
        this.individual = individual;
        return this;
    }
    @JsonProperty("ValueData")
    public String getValueData() {
        return valueData;
    }

    public ObjectPropertyValue setValueData(String valueData) {
        this.valueData = valueData;
        return this;
    }

    @JsonProperty("Expression")
    public ClassExpression getExpression() {
        return expression;
    }

    public ObjectPropertyValue setExpression(ClassExpression expression) {
        this.expression = expression;
        return this;
    }

}
