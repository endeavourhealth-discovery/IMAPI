package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder ({"iri","name","description","mainEntity","var","resultFormat","subset","distinct","activeOnly","referenceDate","select","groupBy"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query extends Heading {

	private TTIriRef graph;
	private Select select;

	private List<Select> subselect;
	private ResultFormat resultFormat;
	private boolean usePrefixes;
	private String referenceDate;
	private boolean activeOnly;
	private int page;
	private int pageSize;
	private TTIriRef mainEntity;

	@JsonIgnore
	public Query select(Consumer<Select> builder){
		this.select= new Select();
		builder.accept(this.select);
		return this;
	}

	public TTIriRef getMainEntity() {
		return mainEntity;
	}

	public Query setMainEntity(TTIriRef mainEntity) {
		this.mainEntity = mainEntity;
		return this;
	}

	public int getPage() {
		return page;
	}

	public Query setPage(int page) {
		this.page = page;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Query setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public boolean isActiveOnly() {
		return activeOnly;
	}

	public Query setActiveOnly(boolean activeOnly) {
		this.activeOnly = activeOnly;
		return this;
	}

	public String getReferenceDate() {
		return referenceDate;
	}

	public Query setReferenceDate(String referenceDate) {
		this.referenceDate = referenceDate;
		return this;
	}


	@Override
	public Query setIri(String iri){
		super.setIri(iri);
		return this;
	}



	@Override
	public Query setDescription(String description){
		super.setDescription(description);
		return this;
	}




	public boolean isUsePrefixes() {
		return usePrefixes;
	}

	@Override
	public Query setName(String name){
		super.setName(name);
		return this;
	}

	@Override
	@JsonIgnore
	public Query name(String name){
		super.setName(name);
		return this;
	}

	public Query setUsePrefixes(boolean usePrefixes) {
		this.usePrefixes = usePrefixes;
		return this;
	}

	public ResultFormat getResultFormat() {
		return resultFormat;
	}

	public Query setResultFormat(ResultFormat resultFormat) {
		this.resultFormat = resultFormat;
		return this;
	}


	public Query resultFormat(ResultFormat resultFormat) {
		this.resultFormat = resultFormat;
		return this;
	}


	public TTIriRef getGraph() {
		return graph;
	}

	public Query setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}






	public List<Select> getSubselect() {
		return subselect;
	}

	public Query setSubselect(List<Select> columnGroups) {
		this.subselect = columnGroups;
		return this;
	}
	public Query addSubselect(Select group){
		if (this.subselect ==null)
			this.subselect = new ArrayList<>();
		this.subselect.add(group);
		return this;
	}


	public Select getSelect() {
		return select;
	}

	public Query setSelect(Select select) {
		this.select = select;
		return this;
	}

	@JsonIgnore
	public String getasJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}







}
