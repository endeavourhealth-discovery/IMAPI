package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
	* A serializable query object containing a sequence of clauses resulting in the definition of a  data set output.
	* <p> In effect a high level process model taylored to the health query ues cases</p>
	*/

@JsonPropertyOrder({"prefixes","mainEntity","clauses"})
public class Query {
	private final Map<String,String> prefixes = new HashMap<>();
	private final Map<String,String> prefixMap= new HashMap<>();
	private String mainEntity;
	private List<QueryClause> clauses = new ArrayList<>();



	/** Returns the list of clauses that are logically sequential
	 *
	 * @return List of clauses
	 */
	public List<QueryClause> getClauses() {
		return clauses;
	}

	/** Adds a list of clauses to the query
	 * @param clauses The list of clauses, list order indicating the logical sequence
	 * @return the modified query
	 */
	public Query setClauses(List<QueryClause> clauses) {
		this.clauses = clauses;
		return this;
	}

	/**
	 * Adds a clause to the query
	 * @param clause the clause to be added
	 * @return the updated query
	 */
	public Query addClause(QueryClause clause){
		this.clauses.add(clause);
		return this;
	}

	public String getMainEntity() {
		return mainEntity;
	}

	public Query setMainEntity(String mainEntity) {
		this.mainEntity = mainEntity;
		return this;
	}


	@JsonIgnore
	public String toJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}

	public Map<String, String> getPrefixes() {
		return prefixes;
	}

	@JsonIgnore
	public Map<String, String> getPrefixMap() {
		return prefixMap;
	}
}
