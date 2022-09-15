package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTContext;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"prefix","iri","name","description","with","with","where","select","subQuery"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query extends QueryClause {
	private TTContext prefix;
	private List<Query> subQuery;
	private boolean activeOnly;
	private boolean usePrefixes;



	public boolean isUsePrefixes() {
		return usePrefixes;
	}

	public Query setUsePrefixes(boolean usePrefixes) {
		this.usePrefixes = usePrefixes;
		return this;
	}

	public boolean isActiveOnly() {
		return activeOnly;
	}

	public Query setActiveOnly(boolean activeOnly) {
		this.activeOnly = activeOnly;
		return this;
	}

	public TTContext getPrefix() {
		return prefix;
	}

	public Query setPrefix(TTContext prefix) {
		this.prefix = prefix;
		return this;
	}

	public List<Query> getSubQuery() {
		return subQuery;
	}

	public Query setSubQuery(List<Query> subQuery) {
		this.subQuery = subQuery;
		return this;
	}

	public Query addSubQuery(Query query){
		if (this.subQuery==null)
			this.subQuery= new ArrayList<>();
		this.subQuery.add(query);
		return this;
	}

	public Query setName(String name) {
		super.setName(name);
		return this;
	}

	public Query setDescription(String description){
		super.setDescription(description);
		return this;
	}


	@JsonIgnore
	public String getJson() throws JsonProcessingException {
		return Query.getJson(this);
	}

	public static String getJson(Object object) throws JsonProcessingException {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        }
	}

}
