package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"description","nodeVariable","iri","name","bool","match","property","range"
	,"operator","isNull","value","unit","in","notIn","relativeTo","anyRoleGroup"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Property extends PropertyRef implements Assignable{
	private String description;
	private Range range;
	private List<Node> in;
	private List<Node> notIn;
	private Match match;
	private Bool bool;
	private List<Property> property;
	private Operator operator;
	private String value;
	private String unit;
	private String valueLabel;
	private boolean anyRoleGroup;
	private boolean isNull;
	private PropertyRef relativeTo;

	public Bool getBool() {
		return bool;
	}

	public Property setBool(Bool bool) {
		this.bool = bool;
		return this;
	}

	public List<Property> getProperty() {
		return property;
	}

	@JsonSetter
	public Property setProperty(List<Property> property) {
		this.property = property;
		return this;
	}

	public Property addProperty(Property prop){
		if (this.property ==null)
			this.property = new ArrayList<>();
		this.property.add(prop);
		return this;
	}

	public Property property(Consumer<Property> builder){
		Property prop= new Property();
		addProperty(prop);
		builder.accept(prop);
		return this;
	}

	public Property setValueVariable(String valueVariable) {
		super.setValueVariable(valueVariable);
		return this;
	}
	public static Property iri(String iri) {
		return new Property(iri);
	}

	public Property(){}

	public Property(String iri){
		super.setIri(iri);
	}


	@JsonProperty("@id")
	public String getId() {
		return super.getIri();
	}

	@Override
	public Property setNodeRef(String nodeRef) {
		super.setNodeRef(nodeRef);
		return this;
	}

	public boolean isNull() {
		return isNull;
	}

	public Property setNull(boolean aNull) {
		isNull = aNull;
		return this;
	}


	public Property setVariable(String variable){
		super.setVariable(variable);
		return this;
	}

	public Property setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public Property setAncestorsOf(boolean entailment){
		super.setAncestorsOf(entailment);
		return this;
	}



	public String getValueLabel() {
		return valueLabel;
	}

	public Property setValueLabel(String valueLabel) {
		this.valueLabel = valueLabel;
		return this;
	}





	public List<Node> getNotIn() {
		return notIn;
	}

	public Property setNotIn(List<Node> notIn) {
		this.notIn = notIn;
		return this;
	}

	public Property addNotIn(Node notIn) {
		if (this.notIn==null)
			this.notIn = new ArrayList<>();
		this.notIn.add(notIn);
		return this;
	}


	public boolean isAnyRoleGroup() {
		return anyRoleGroup;
	}

	public Property setAnyRoleGroup(boolean anyRoleGroup) {
		this.anyRoleGroup = anyRoleGroup;
		return this;
	}


	public Match getMatch() {
		return match;
	}

	@JsonSetter
	public Property setMatch(Match match) {
		this.match = match;
		return this;
	}




	public Property match(Consumer<Match> builder){
		builder.accept(setMatch(new Match()).getMatch());
		return this;
	}


	public Property setName(String name) {
		super.setName(name);
		return this;
	}


	public Property setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}



	public List<Node> getIn() {
		return in;
	}

	@JsonSetter
	public Property setIn(List<Node> in) {
		this.in = in;
		return this;
	}




	public Property addIn(Node in){
		if (this.in==null)
			this.in= new ArrayList<>();
		this.in.add(in);
		return this;
	}

	public Property in(Consumer<Node> builder){
		Node in = new Node();
		addIn(in);
		builder.accept(in);
		return this;
	}

	public Property notIn(Consumer<Node> builder){
		Match in = new Match();
		addNotIn(in);
		builder.accept(in);
		return this;
	}

	public Property setInverse(boolean inverse){
		super.setInverse(inverse);
		return this;
	}

	public Property addIn(String in){
		if (this.in==null)
			this.in= new ArrayList<>();
		this.in.add(new Match().setIri(in));
		return this;
	}

	public Property setDescendantsOrSelfOf(boolean entailment){
		super.setDescendantsOrSelfOf(entailment);
		return this;
	}




	@Override
	public Property setOperator(Operator operator) {
		this.operator= operator;
		return this;
	}

	public Operator getOperator(){
		return this.operator;
	}


   public PropertyRef getRelativeTo(){
		return this.relativeTo;
	 }
	public Property setRelativeTo(PropertyRef relativeTo) {
		this.relativeTo= relativeTo;
		return this;
	}

	public Property relativeTo(Consumer<PropertyRef> builder){
		builder.accept(setRelativeTo(new PropertyRef()).getRelativeTo());
		return this;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public Property setValue(String value) {
		this.value= value;
		return this;
	}



	public Property setUnit(String unit) {
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

	public String getUnit(){
		return this.unit;
	}


	public Range getRange() {
		return range;
	}

	public Property setRange(Range range) {
		this.range = range;
		return this;
	}

	public Property range(Consumer<Range> builder){
		this.range= new Range();
		builder.accept(this.range);
		return this;
	}



}
