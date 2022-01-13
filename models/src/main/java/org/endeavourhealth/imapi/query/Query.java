package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
	* A specialised query object containing a sequence of query clauses designed to map to SPARQL or SQL
 * Query can be converted to and from TTEntity for graph storage using asEntity or from Entity methods
	*
	*/

@JsonPropertyOrder({"iri","name","description","type","mainEntityType","mainEntityVar","prefix","select","step"})
public class Query{
	private String name;
	private String description;
	private String iri;
	private TTIriRef type;
	private TTIriRef mainEntityType;
	private String mainEntityVar;
	private List<TTIriRef> folder;
	private List<Select> select;
	private List<Step> step;
	private static final Map<String,String> nsPrefix= new HashMap<>();
	private static final Map<String,String> prefix= new HashMap<>();

	public Query(){
		addPrefix(IM.NAMESPACE,"im");
		addPrefix(RDFS.NAMESPACE,"rdfs");
		addPrefix(RDF.NAMESPACE,"rdf");
		addPrefix(SNOMED.NAMESPACE,"sn");
	}

	/**
	 * Returns the query object as a TTEntity with the query clause definitions as json literal value
	 * of the im:queryDefinition predicate
	 * @return TTEntity representation of the query
	 * @throws JsonProcessingException
	 */
	public TTEntity asEntity() throws JsonProcessingException {
		TTEntity entity = new TTEntity()
			.setIri(this.getIri())
			.setName(this.getName())
			.setDescription(this.getDescription())
			.addType(IM.QUERY);
		if (this.getFolder()!=null)
			this.getFolder().stream().forEach(f-> entity.addObject(IM.IS_CONTAINED_IN,f));

		ObjectMapper objectMapper= new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		String json=objectMapper.writeValueAsString(this);
		entity.set(IM.QUERY_DEFINITION, TTLiteral.literal(json));
		return entity;

	}

	/** Creates a query business object from a TTEntity query type
	 * the main query is derived from the json literal of the im:queryDefinition predicate
	 * @param entityQuery the qery in TTEntity form.
	 * @return the query object.
	 * @throws JsonProcessingException
	 */
	public Query fromEntity(TTEntity entityQuery) throws JsonProcessingException {
		ObjectMapper om= new ObjectMapper();
		TTValue definition= entityQuery.get(IM.QUERY_DEFINITION).getElements().get(0);
		String json= definition.asLiteral().getValue();
		Query qry=om.readValue(json,Query.class);
		qry.setIri(entityQuery.getIri());
		qry.setName(entityQuery.getName());
		qry.setDescription(entityQuery.getDescription());
		qry.setType(IM.QUERY);
		if (entityQuery.get(IM.IS_CONTAINED_IN)!=null)
			entityQuery.get(IM.IS_CONTAINED_IN).getElements().stream().forEach(
				f-> qry.addFolder(f.asIriRef()));
		return qry;

	}

	public static void addPrefix(String ns,String px){
		nsPrefix.put(ns,px);
		prefix.put(px,ns);
	}



	public Map<String,String> getPrefix(){
		return prefix;
	}


	public static String getShort(String iri) {
		if (iri==null)
			return null;
		int end = Integer.max(iri.lastIndexOf("/"), iri.lastIndexOf("#"));
		String path = iri.substring(0, end + 1);
		String prefix = nsPrefix.get(path);
		if (prefix == null)
			return iri;
		else
		if (end<iri.length()-1)
			return prefix + ":" + iri.substring(end + 1);
		else if(end == iri.length()-1)
			return prefix + ":";
		else return iri;

	}

	public TTIriRef getType() {
		return type;
	}

	public Query setType(TTIriRef type) {
		this.type = type;
		return this;
	}

	public TTIriRef getMainEntityType() {
		return mainEntityType;
	}

	public Query setMainEntityType(TTIriRef mainEntityType) {
		this.mainEntityType = mainEntityType;
		return this;
	}

	public String getMainEntityVar() {
		return mainEntityVar;
	}

	public Query setMainEntityVar(String mainEntityVar) {
		this.mainEntityVar = mainEntityVar;
		return this;
	}

	public List<TTIriRef> getFolder() {
		return folder;
	}

	public Query setFolder(List<TTIriRef> folder) {
		this.folder = folder;
		return this;
	}

	public Query addFolder(TTIriRef folder){
		if (this.folder==null)
			this.folder= new ArrayList<>();
		this.folder.add(folder);
		return this;
	}

	public String getName() {
		return name;
	}

	public Query setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Query setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getIri() {
		return iri;
	}

	public Query setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public List<Step> getStep() {
		return step;
	}

	public Query setStep(List<Step> step) {
		this.step = step;
		return this;
	}

	public Query addStep(Step step){
		if (this.step==null)
			this.step= new ArrayList<>();
		this.step.add(step);
		return this;
	}
	public List<Select> getSelect() {
		return select;
	}


	public Query addSelect(Select sct){
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(sct);
		return this;
	}

	public Query setSelect(List<Select> select) {
		this.select = select;
		return this;
	}

}
