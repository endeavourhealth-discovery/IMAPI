package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"create","sourcePath","where","function","target","targetPath","valueMap"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MapRule{

	private TTVariable create;
	private String sourceProperty;
	private String sourceVariable;
	private String listMode;
	private Where where;
	private Function function;
	private TTVariable targetEntity;
	private String targetProperty;
	private String targetVariable;
	private DataMap valueMap;

	public DataMap getValueMap() {
		return valueMap;
	}

	@JsonSetter
	public MapRule setValueMap(DataMap valueMap) {
		this.valueMap = valueMap;
		return this;
	}

	public MapRule valueMap(Consumer<DataMap> builder){
		this.valueMap= new DataMap();
		builder.accept(this.valueMap);
		return this;
	}




	public TTVariable getCreate() {
		return create;
	}

	public MapRule setCreate(TTVariable create) {
		this.create = create;
		return this;
	}



	public String getTargetVariable() {
		return targetVariable;
	}

	public MapRule setTargetVariable(String targetVariable) {
		this.targetVariable = targetVariable;
		return this;
	}

	public String getSourceVariable() {
		return sourceVariable;
	}

	public MapRule setSourceVariable(String sourceVariable) {
		this.sourceVariable = sourceVariable;
		return this;
	}

	public String getListMode() {
		return listMode;
	}

	public MapRule setListMode(String listMode) {
		this.listMode = listMode;
		return this;
	}

	public Where getWhere() {
		return where;
	}

	@JsonSetter
	public MapRule setWhere(Where where) {
		this.where = where;
		return this;
	}

	public MapRule where (Consumer<Where> builder){
		this.where= new Where();
		builder.accept(this.where);
		return this;
	}




	public String getSourceProperty() {
		return sourceProperty;
	}

	public MapRule setSourceProperty(String sourceProperty) {
		this.sourceProperty = sourceProperty;
		return this;
	}








	public Function getFunction() {
		return function;
	}

	public MapRule setFunction(Function function) {
		this.function = function;
		return this;
	}



	public MapRule function(Consumer<Function> builder){
		this.function= new Function();
		builder.accept(this.function);
		return this;
	}

	public TTVariable getTargetEntity() {
		return targetEntity;
	}

	@JsonSetter
	public MapRule setTargetEntity(TTVariable targetEntity) {
		this.targetEntity = targetEntity;
		return this;
	}

	public MapRule targetEntity(Consumer<TTVariable> builder){
		this.targetEntity= new TTVariable();
		builder.accept(this.targetEntity);
		return this;
	}

	public String getTargetProperty() {
		return targetProperty;
	}

	public MapRule setTargetProperty(String targetProperty) {
		this.targetProperty = targetProperty;
		return this;
	}

}
