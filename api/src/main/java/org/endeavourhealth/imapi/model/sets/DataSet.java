package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder ({"iri","name","description","var","resultFormat","subset","distinct","activeOnly","referenceDate","select","groupBy","match"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DataSet extends Heading {

	private TTIriRef graph;
	private Select select;

	private List<Select> subselect;
	private List<Select> groupBy;
	private ResultFormat resultFormat;
	private boolean usePrefixes;
	private String referenceDate;
	private boolean activeOnly;
	private int page;
	private int pageSize;
	private TTIriRef mainEntity;

	public TTIriRef getMainEntity() {
		return mainEntity;
	}

	public DataSet setMainEntity(TTIriRef mainEntity) {
		this.mainEntity = mainEntity;
		return this;
	}

	public int getPage() {
		return page;
	}

	public DataSet setPage(int page) {
		this.page = page;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public DataSet setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public boolean isActiveOnly() {
		return activeOnly;
	}

	public DataSet setActiveOnly(boolean activeOnly) {
		this.activeOnly = activeOnly;
		return this;
	}

	public String getReferenceDate() {
		return referenceDate;
	}

	public DataSet setReferenceDate(String referenceDate) {
		this.referenceDate = referenceDate;
		return this;
	}


	@Override
	public DataSet setIri(String iri){
		super.setIri(iri);
		return this;
	}



	@Override
	public DataSet setDescription(String description){
		super.setDescription(description);
		return this;
	}




	public boolean isUsePrefixes() {
		return usePrefixes;
	}

	@Override
	public DataSet setName(String name){
		super.setName(name);
		return this;
	}

	public DataSet setUsePrefixes(boolean usePrefixes) {
		this.usePrefixes = usePrefixes;
		return this;
	}

	public ResultFormat getResultFormat() {
		return resultFormat;
	}

	public DataSet setResultFormat(ResultFormat resultFormat) {
		this.resultFormat = resultFormat;
		return this;
	}

	public List<Select> getGroupBy() {
		return groupBy;
	}

	public DataSet setGroupBy(List<Select> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public DataSet addGroupBy(Select group){
		if (this.groupBy==null)
			this.groupBy= new ArrayList<>();
		this.groupBy.add(group);
		return this;
	}


	public TTIriRef getGraph() {
		return graph;
	}

	public DataSet setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}






	public List<Select> getSubselect() {
		return subselect;
	}

	public DataSet setSubselect(List<Select> columnGroups) {
		this.subselect = columnGroups;
		return this;
	}
	public DataSet addSubselect(Select group){
		if (this.subselect ==null)
			this.subselect = new ArrayList<>();
		this.subselect.add(group);
		return this;
	}


	public Select getSelect() {
		return select;
	}

	public DataSet setSelect(Select select) {
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
