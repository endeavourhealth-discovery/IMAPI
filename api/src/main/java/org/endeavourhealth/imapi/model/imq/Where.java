package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"description", "nodeVariable", "iri", "name", "bool", "match", "property", "range"
        , "operator", "isNull", "value", "unit", "in", "isNot", "relativeTo", "anyRoleGroup"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties({"key"})
public class Where extends PropertyRef implements Assignable{
    private String description;
    private Range range;
    private List<Node> is;
    private List<Node> isNot;
    private Match match;
    private Bool boolWhere;
    private List<Where> where;
    private Operator operator;
    private String value;
    private String unit;
    private String valueLabel;
    private boolean anyRoleGroup;
    private boolean isNull;
    private PropertyRef relativeTo;
    private boolean isNotNull;

    public Bool getBoolWhere() {
        return boolWhere;
    }

    public Where setBoolWhere(Bool boolWhere) {
        this.boolWhere = boolWhere;
        return this;
    }

    public boolean isNull() {
        return isNull;
    }

    public Where setNull(boolean aNull) {
        isNull = aNull;
        return this;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public Where setNotNull(boolean notNull) {
        isNotNull = notNull;
        return this;
    }

    public boolean getIsNotNull() {
        return isNotNull;
    }

    public Where setIsNotNull(boolean notNull) {
        isNotNull = notNull;
        return this;
    }

    public List<Where> getWhere() {
        return where;
    }

    @JsonSetter
    public Where setWhere(List<Where> where) {
        this.where = where;
        return this;
    }

    public Where addWhere(Where prop) {
        if (this.where == null)
            this.where = new ArrayList<>();
        this.where.add(prop);
        return this;
    }

    public Where where(Consumer<Where> builder) {
        Where prop = new Where();
        addWhere(prop);
        builder.accept(prop);
        return this;
    }

    public Where setValueVariable(String valueVariable) {
        super.setValueVariable(valueVariable);
        return this;
    }

    public static Where iri(String iri) {
        return new Where(iri);
    }

    public Where() {
    }

    public Where(String iri) {
        super.setIri(iri);
    }


    @JsonProperty("@id")
    public String getId() {
        return super.getIri();
    }

    @Override
    public Where setNodeRef(String nodeRef) {
        super.setNodeRef(nodeRef);
        return this;
    }

    public boolean getIsNull() {
        return isNull;
    }

    public Where setIsNull(boolean aNull) {
        isNull = aNull;
        return this;
    }


    public Where setVariable(String variable) {
        super.setVariable(variable);
        return this;
    }

    public Where setIri(String iri) {
        super.setIri(iri);
        return this;
    }

    public Where setAncestorsOf(boolean entailment) {
        super.setAncestorsOf(entailment);
        return this;
    }


    public String getValueLabel() {
        return valueLabel;
    }

    public Where setValueLabel(String valueLabel) {
        this.valueLabel = valueLabel;
        return this;
    }


    public List<Node> getIsNot() {
        return isNot;
    }

    public Where setIsNot(List<Node> isNot) {
        this.isNot = isNot;
        return this;
    }

    public Where addIsNot(Node notIn) {
        if (this.isNot == null)
            this.isNot = new ArrayList<>();
        this.isNot.add(notIn);
        return this;
    }


    public boolean isAnyRoleGroup() {
        return anyRoleGroup;
    }

    public Where setAnyRoleGroup(boolean anyRoleGroup) {
        this.anyRoleGroup = anyRoleGroup;
        return this;
    }


    public Match getMatch() {
        return match;
    }

    @JsonSetter
    public Where setMatch(Match match) {
        this.match = match;
        return this;
    }


    public Where match(Consumer<Match> builder) {
        builder.accept(setMatch(new Match()).getMatch());
        return this;
    }


    public Where setName(String name) {
        super.setName(name);
        return this;
    }


    public Where setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }


    public List<Node> getIs() {
        return is;
    }

    @JsonSetter
    public Where setIs(List<Node> is) {
        this.is = is;
        return this;
    }


    public Where addIs(Node in) {
        if (this.is == null)
            this.is = new ArrayList<>();
        this.is.add(in);
        return this;
    }

    public Where is(Consumer<Node> builder) {
        Node in = new Node();
        addIs(in);
        builder.accept(in);
        return this;
    }

    public Where isNot(Consumer<Node> builder) {
        Node in = new Node();
        addIsNot(in);
        builder.accept(in);
        return this;
    }

    public Where setInverse(boolean inverse) {
        super.setInverse(inverse);
        return this;
    }

    public Where addIs(String in) {
        if (this.is == null)
            this.is = new ArrayList<>();
        this.is.add(new Node().setIri(in));
        return this;
    }

    public Where setDescendantsOrSelfOf(boolean entailment) {
        super.setDescendantsOrSelfOf(entailment);
        return this;
    }


    @Override
    public Where setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public Operator getOperator() {
        return this.operator;
    }


    public PropertyRef getRelativeTo() {
        return this.relativeTo;
    }

    public Where setRelativeTo(PropertyRef relativeTo) {
        this.relativeTo = relativeTo;
        return this;
    }

    public Where relativeTo(Consumer<PropertyRef> builder) {
        builder.accept(setRelativeTo(new PropertyRef()).getRelativeTo());
        return this;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Where setValue(String value) {
        this.value = value;
        return this;
    }

    public Where setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public Assignable setDataType(TTIriRef datatype) {
        return null;
    }

    @Override
    public TTIriRef getDataType() {
        return null;
    }

    public String getUnit() {
        return this.unit;
    }


    public Range getRange() {
        return range;
    }

    public Where setRange(Range range) {
        this.range = range;
        return this;
    }

    public Where range(Consumer<Range> builder) {
        this.range = new Range();
        builder.accept(this.range);
        return this;
    }


}
