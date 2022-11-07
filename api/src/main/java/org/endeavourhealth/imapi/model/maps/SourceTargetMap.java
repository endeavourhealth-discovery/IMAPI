package org.endeavourhealth.imapi.model.maps;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.iml.Function;
import org.endeavourhealth.imapi.model.iml.Where;
import org.endeavourhealth.imapi.model.tripletree.TTVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"sourcePaths","function","target","targetPath"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SourceTargetMap {
	private List<MapPath> sourcePaths;
	private Function function;
	private TTVariable targetEntity;
	private String targetPath;
	private Where where;
	private EntityMap valueMap;

	public EntityMap getValueMap() {
		return valueMap;
	}

	@JsonSetter
	public SourceTargetMap setValueMap(EntityMap valueMap) {
		this.valueMap = valueMap;
		return this;
	}

	public SourceTargetMap valueMap(Consumer<EntityMap> builder){
		this.valueMap= new EntityMap();
		builder.accept(this.valueMap);
		return this;
	}

	public Where getWhere() {
		return where;
	}

	@JsonSetter
	public SourceTargetMap setWhere(Where where) {
		this.where = where;
		return this;
	}

	public SourceTargetMap where (Consumer<Where> builder) {
		this.where = new Where();
		builder.accept(this.where);
		return this;
	}

	public List<MapPath> getSourcePaths() {
		return sourcePaths;
	}

	@JsonSetter
	public SourceTargetMap setSourcePaths(List<MapPath> sourcePaths) {
		this.sourcePaths = sourcePaths;
		return this;
	}

	public SourceTargetMap addSourcePath(MapPath path){
		if (this.sourcePaths==null)
			this.sourcePaths= new ArrayList<>();
		this.sourcePaths.add(path);
		return this;
	}

	public SourceTargetMap sourcePath(Consumer<MapPath> builder){
		MapPath path= new MapPath();
		addSourcePath(path);
		builder.accept(path);
		return this;
	}


	public Function getFunction() {
		return function;
	}

	public SourceTargetMap setFunction(Function function) {
		this.function = function;
		return this;
	}



	public SourceTargetMap function(Consumer<Function> builder){
		this.function= new Function();
		builder.accept(this.function);
		return this;
	}

	public TTVariable getTargetEntity() {
		return targetEntity;
	}

	@JsonSetter
	public SourceTargetMap setTargetEntity(TTVariable targetEntity) {
		this.targetEntity = targetEntity;
		return this;
	}

	public SourceTargetMap targetEntity(Consumer<TTVariable> builder){
		this.targetEntity= new TTVariable();
		builder.accept(this.targetEntity);
		return this;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public SourceTargetMap setTargetPath(String targetPath) {
		this.targetPath = targetPath;
		return this;
	}
}
