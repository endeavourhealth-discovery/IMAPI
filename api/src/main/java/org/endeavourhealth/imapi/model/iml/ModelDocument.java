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
	private List<Entity> folder;
	private List<ConceptSet> conceptSet;
	private List<FunctionClause> functionClause;
	private List<QueryEntity> query;

	@JsonProperty("@context")
	public TTContext getContext() {
		return context;
	}

	public ModelDocument setContext(TTContext context) {
		this.context = context;
		return this;
	}

	public List<QueryEntity> getQuery() {
		return query;
	}

	public ModelDocument setQuery(List<QueryEntity> query) {
		this.query = query;
		return this;
	}

	public ModelDocument addQuery(QueryEntity query){
		if (this.query==null)
			this.query= new ArrayList<>();
		this.query.add(query);
		return this;
	}

	public List<Entity> getFolder() {
		return folder;
	}

	public ModelDocument setFolder(List<Entity> folder) {
		this.folder = folder;
		return this;
	}

	public ModelDocument addFolder(Entity folder){
		if (this.folder==null)
			this.folder= new ArrayList<>();
		this.folder.add(folder);
		return this;
	}

	public List<ConceptSet> getConceptSet() {
		return conceptSet;
	}

	public ModelDocument setConceptSet(List<ConceptSet> conceptSet) {
		this.conceptSet = conceptSet;
		return this;
	}

	public ModelDocument addConceptSet(ConceptSet set){
		if (this.conceptSet==null)
			this.conceptSet= new ArrayList<>();
		this.conceptSet.add(set);
		return this;
	}

	public List<FunctionClause> getFunction() {
		return functionClause;
	}

	public ModelDocument setFunction(List<FunctionClause> functionClause) {
		this.functionClause = functionClause;
		return this;
	}

	public ModelDocument addFunction(FunctionClause functionClause){
		if (this.functionClause ==null)
			this.functionClause = new ArrayList<>();
		this.functionClause.add(functionClause);
		return this;
	}
}
