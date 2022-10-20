package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * A class representation of a transformation map i.e. deserialize to this class for use by a transform engine
 *<p> Maps from one or many source types to one or many target types using zero or many other maps</p>
 * Note that the maps do not dictate how the source objects are presented
 */
@JsonPropertyOrder({"iri","name","description","source","sourceFormat","target","import","group"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TransformMap extends TTIriRef {
	private String description;
	private TTIriRef sourceFormat;
	private List<TTAlias> source;
	private List<TTAlias> target;
	private List<TTAlias> imports;
	private List<MapGroup> group;

	public TTIriRef getSourceFormat() {
		return sourceFormat;
	}

	public TransformMap setSourceFormat(TTIriRef sourceFormat) {
		this.sourceFormat = sourceFormat;
		return this;
	}

	@Override
	public TransformMap setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public String getDescription() {
		return description;
	}

	public TransformMap setDescription(String description) {
		this.description = description;
		return this;
	}


	@Override
	public TransformMap setName(String name) {
		super.setName(name);
		return this;
	}

	public List<TTAlias> getSource() {
		return source;
	}

	public TransformMap setSource(List<TTAlias> source) {
		this.source = source;
		return this;
	}

	public TransformMap addSource(TTAlias source) {
		if (this.source ==null)
			this.source = new ArrayList<>();
		this.source.add(source);
		return this;
	}

	public List<TTAlias> getTarget() {
		return target;
	}

	public TransformMap setTarget(List<TTAlias> target) {
		this.target = target;
		return this;
	}

	public TransformMap addTarget(TTAlias target) {
		if (this.target ==null)
			this.target = new ArrayList<>();
		this.target.add(target);
		return this;
	}

	@JsonProperty ("import")
	public List<TTAlias> getImports() {
		return imports;
	}

	@JsonProperty("import")
	@JsonSetter
	public TransformMap setImports(List<TTAlias> imports) {
		this.imports = imports;
		return this;
	}

	public TransformMap addImport(TTAlias importMap) {
		if (this.imports==null)
			this.imports= new ArrayList<>();
		this.imports.add(importMap);
		return this;
	}

	public List<MapGroup> getGroup() {
		return group;
	}

	public TransformMap setGroup(List<MapGroup> group) {
		this.group = group;
		return this;
	}

	public TransformMap addGroup(MapGroup group) {
		if (this.group ==null)
			this.group= new ArrayList<>();
		this.group.add(group);
		return this;
	}

	public TransformMap group(Consumer<MapGroup> builder){
		MapGroup group= new MapGroup();
		addGroup(group);
		builder.accept(group);
		return this;
	}
}
