package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder ({"iri","name","description","var","select","propertyMap","match","subset"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DataSet extends Heading {

	private TTIriRef graph;
	private List<Select> select;

	private List<DataSet> subset;
	private List<Select> groupBy;
	private Match match;
	private ResultFormat resultFormat;
	private boolean usePrefixes;
	private boolean distinct;

	public boolean isDistinct() {
		return distinct;
	}

	public DataSet setDistinct(boolean distinct) {
		this.distinct = distinct;
		return this;
	}

	public boolean isUsePrefixes() {
		return usePrefixes;
	}

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
	public Match getMatch() {
		return match;
	}

	public DataSet setMatch(Match match) {
		this.match = match;
		return this;
	}

	public TTIriRef getGraph() {
		return graph;
	}

	public DataSet setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}






	public List<DataSet> getSubset() {
		return subset;
	}

	public DataSet setSubset(List<DataSet> dataSets) {
		this.subset = dataSets;
		return this;
	}
	public DataSet addSubset(DataSet group){
		if (this.subset ==null)
			this.subset = new ArrayList<>();
		this.subset.add(group);
		return this;
	}

	public List<Select> getSelect() {
		return select;
	}

	public DataSet setSelect(List<Select> select) {
		this.select = select;
		return this;
	}

	public DataSet addSelect(Select select){
		if (this.select ==null)
			this.select = new ArrayList<>();
		this.select.add(select);
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
