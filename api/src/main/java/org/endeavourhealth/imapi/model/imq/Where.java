package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"exclude","description","type","iri","name","alias","bool","with","where","range"
	,"operator","isNull","value","unit","in","notIn","relativeTo","anyRoleGroup"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Where extends TTAlias implements Assignable{
	private String description;
	private Range range;
	private List<TTAlias> in;
	private List<TTAlias> notIn;
	private Bool bool;
	private List<Where> where;
	private With with;
	private Operator operator;
	private String relativeTo;
	private String value;
	private String unit;
	private String valueLabel;
	private boolean anyRoleGroup;
	private boolean exclude;
	private boolean isNull;

	public boolean getIsNull() {
		return isNull;
	}

	public Where setIsNull(boolean aNull) {
		isNull = aNull;
		return this;
	}

	public boolean isExclude() {
		return exclude;
	}

	public Where setExclude(boolean exclude) {
		this.exclude = exclude;
		return this;
	}

	public String getValueLabel() {
		return valueLabel;
	}

	public Where setValueLabel(String valueLabel) {
		this.valueLabel = valueLabel;
		return this;
	}

	public Where setType(String type) {
		super.setType(type);
		return this;
	}



	public List<TTAlias> getNotIn() {
		return notIn;
	}

	public Where setNotIn(List<TTAlias> notIn) {
		this.notIn = notIn;
		return this;
	}

	public Where addNotIn(TTAlias notIn) {
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


	public With getWith() {
		return with;
	}

	public Where setWith(With with) {
		this.with = with;
		return this;
	}

	public Where with(Consumer<With> builder){
		this.with= new With();
		builder.accept(this.with);
		return this;
	}


	public Where setIri(String iri) {
		super.setIri(iri);
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



	public List<TTAlias> getIn() {
		return in;
	}

	@JsonSetter
	public Where setIn(List<TTAlias> in) {
		this.in = in;
		return this;
	}




	public Where addIn(TTAlias in){
		if (this.in==null)
			this.in= new ArrayList<>();
		this.in.add(in);
		return this;
	}

	public Where in(Consumer<TTAlias> builder){
		TTAlias in = new TTAlias();
		addIn(in);
		builder.accept(in);
		return this;
	}

	public Where notIn(Consumer<TTAlias> builder){
		TTAlias in = new TTAlias();
		addNotIn(in);
		builder.accept(in);
		return this;
	}

	public Where addIn(String in){
		if (this.in==null)
			this.in= new ArrayList<>();
		this.in.add(new TTAlias().setIri(in));
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

	@Override
	public String getRelativeTo() {
		return this.relativeTo;
	}

	@Override
	public Where setRelativeTo(String relativeTo) {
		this.relativeTo= relativeTo;
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

	public Where setAlias(String alias){
		super.setAlias(alias);
		return this;
	}


}
