package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTContext;

import java.util.ArrayList;
import java.util.List;

/**
 * A document containing various instance entities conforming to data model shapes of various kinds
 */
@JsonPropertyOrder ({"@context","query"})
public class ModelDocument {
	private TTContext context;
	private List<Folder> folder;
	private List<ConceptSet> conceptSet;
	private List<Function> function;
	private List<Query> query;

	@JsonProperty("@context")
	public TTContext getContext() {
		return context;
	}

	public ModelDocument setContext(TTContext context) {
		this.context = context;
		return this;
	}

	public List<Query> getQuery() {
		return query;
	}

	public ModelDocument setQuery(List<Query> query) {
		this.query = query;
		return this;
	}

	public ModelDocument addQuery(Query query){
		if (this.query==null)
			this.query= new ArrayList<>();
		this.query.add(query);
		return this;
	}
}
