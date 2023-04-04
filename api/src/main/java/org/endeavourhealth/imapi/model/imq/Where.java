package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"exclude","description","nodeVariable","id","name","bool","where","range"
	,"operator","isNull","value","unit","in","notIn","relativeTo","anyRoleGroup"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Where extends Property implements Assignable,Whereable{
	private String description;
	private Range range;
	private List<Element> in;
	private List<Element> notIn;
	private Bool bool;
	private List<Where> where;
	private Operator operator;
	private String value;
	private String unit;
	private String valueLabel;
	private boolean anyRoleGroup;
	private boolean isNull;
	private Property relativeTo;

	public boolean isNull() {
		return isNull;
	}

	public Where setNull(boolean aNull) {
		isNull = aNull;
		return this;
	}


	public Where setId(String id){
		super.setId(id);
		return this;
	}

	public Where setNode(String node){
		super.setNode(node);
		return this;
	}



	public String getValueLabel() {
		return valueLabel;
	}

	public Where setValueLabel(String valueLabel) {
		this.valueLabel = valueLabel;
		return this;
	}

	public Where setIri(String id){
		super.setId(id);
		return this;
	}




	public List<Element> getNotIn() {
		return notIn;
	}

	public Where setNotIn(List<Element> notIn) {
		this.notIn = notIn;
		return this;
	}

	public Where addNotIn(Element notIn) {
		if (this.notIn==null)
			this.notIn = new ArrayList<>();
		this.notIn.add(notIn);
		return this;
	}


	public boolean isAnyRoleGroup() {
		return anyRoleGroup;
	}

	public Where setAnyRoleGroup(boolean anyRoleGroup) {
		this.anyRoleGroup = anyRoleGroup;
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

	public Where addWhere(Where where){
		if (this.where ==null)
			this.where = new ArrayList<>();
		this.where.add(where);
		return this;
	}


	public Where where(Consumer<Where> builder){
		Where where = new Where();
		addWhere(where);
		builder.accept(where);
		return this;
	}


	public Bool getBool() {
		return bool;
	}

	public Where setBool(Bool bool) {
		this.bool = bool;
		return this;
	}



	public Where setInverse(boolean inverse){
		super.setInverse(inverse);
		return this;
	}

	public Where setDescendantsOrSelfOf(boolean subtypes){
		super.setDescendantsOrSelfOf(subtypes);
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



	public List<Element> getIn() {
		return in;
	}

	@JsonSetter
	public Where setIn(List<Element> in) {
		this.in = in;
		return this;
	}




	public Where addIn(Element in){
		if (this.in==null)
			this.in= new ArrayList<>();
		this.in.add(in);
		return this;
	}

	public Where in(Consumer<Element> builder){
		Element in = new Element();
		addIn(in);
		builder.accept(in);
		return this;
	}

	public Where notIn(Consumer<Element> builder){
		Element in = new Element();
		addNotIn(in);
		builder.accept(in);
		return this;
	}

	public Where addIn(String in){
		if (this.in==null)
			this.in= new ArrayList<>();
		this.in.add(new Element().setId(in));
		return this;
	}


	@Override
	public Where setOperator(Operator operator) {
		this.operator= operator;
		return this;
	}

	public Operator getOperator(){
		return this.operator;
	}


   public Property getRelativeTo(){
		return this.relativeTo;
	 }
	public Where setRelativeTo(Property relativeTo) {
		this.relativeTo= relativeTo;
		return this;
	}

	public Where relativeTo(Consumer<Property> builder){
		builder.accept(setRelativeTo(new Property()).getRelativeTo());
		return this;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public Where setValue(String value) {
		this.value= value;
		return this;
	}



	public Where setUnit(String unit) {
		this.unit = unit;
		return this;
	}

	public String getUnit(){
		return this.unit;
	}


	public Range getRange() {
		return range;
	}

	public Where setRange(Range range) {
		this.range = range;
		return this;
	}

	public Where range(Consumer<Range> builder){
		this.range= new Range();
		builder.accept(this.range);
		return this;
	}



}
