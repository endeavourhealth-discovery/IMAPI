package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class With {
	private List<TTAlias> type;
	private TTAlias instance;
	private String resultSet;
	private Query query;

	public Query getQuery() {
		return query;
	}

	public With setQuery(Query query) {
		this.query = query;
		return this;
	}

	public String getResultSet() {
		return resultSet;
	}

	public With setResultSet(String resultSet) {
		this.resultSet = resultSet;
		return this;
	}

	public List<TTAlias> getType() {
		return type;
	}

	public With setType(List<TTAlias> type) {
		this.type = type;
		return this;
	}



	public With addType(TTAlias type){
		if (this.type==null)
		this.type= new ArrayList<>();
		this.type.add(type);
		return this;
	}

	public With addType(TTIriRef type){
		if (this.type==null)
			this.type= new ArrayList<>();
		this.type.add(new TTAlias(type));
		return this;
	}

	public With addType(String type){
		if (this.type==null)
			this.type= new ArrayList<>();
		this.type.add(new TTAlias().setIri(type));
		return this;
	}

	public TTAlias getInstance() {
		return instance;
	}

	public With setInstance(TTAlias instance) {
		this.instance = instance;
		return this;
	}


	@JsonIgnore
	public With setInstance(String instance) {
		this.instance = new TTAlias(TTIriRef.iri(instance));
		return this;
	}
}
