package com.endavourhealth.dataaccess.entity;

import javax.persistence.*;

@Entity
public class PropertyValue {

	@Id()
	private Integer dbid;
    @ManyToOne()
    @JoinColumn(name="expression", referencedColumnName="dbid")
	private Expression expression;
    @OneToOne()
    @JoinColumn(name="property", referencedColumnName="dbid")
	private Concept property;
    @OneToOne()
    @JoinColumn(name="value_type", referencedColumnName="dbid")
    private Concept valueType;
    private Byte inverse;
    private Integer minCardinality;
    private Integer maxCardinality;
    @OneToOne()
    @JoinColumn(name="value_expression", referencedColumnName="dbid")
    private Expression valueExpression;
    private String valueData;


    public PropertyValue() {
		super();
	}

    public Integer getDbid() {
        return dbid;
    }

    public PropertyValue setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Expression getExpression() {
        return expression;
    }

    public PropertyValue setExpression(Expression expression) {
        this.expression = expression;
        return this;
    }

    public Concept getProperty() {
        return property;
    }

    public PropertyValue setProperty(Concept property) {
        this.property = property;
        return this;
    }

    public Concept getValueType() {
        return valueType;
    }

    public PropertyValue setValueType(Concept valueType) {
        this.valueType = valueType;
        return this;
    }

    public Byte getInverse() {
        return inverse;
    }

    public PropertyValue setInverse(Byte inverse) {
        this.inverse = inverse;
        return this;
    }

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public PropertyValue setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public PropertyValue setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    public Expression getValueExpression() {
        return valueExpression;
    }

    public PropertyValue setValueExpression(Expression valueExpression) {
        this.valueExpression = valueExpression;
        return this;
    }

    public String getValueData() {
        return valueData;
    }

    public PropertyValue setValueData(String valueData) {
        this.valueData = valueData;
        return this;
    }
}
