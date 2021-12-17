package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.*;

/**
	* A serializable query object containing a sequence of clauses resulting in the definition of a  data set output.
	* <p> In effect a high level process model taylored to the health query ues cases</p>
	*/

@JsonPropertyOrder({"prefixes","mainEntity","clauses"})
public class Query  extends TTEntity {
	public static String dataModelGraph= "http://endhealth.info/im#";


	public Query(){
	}





	@JsonIgnore
	public String toJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		return json;
	}

	/**
	 * gets the query definition i.e. set of steps associated with this query
	 * @return Query definition with a main entity and steps
	 */
	public QueryDefinition getQueryDefinition() {
		return (QueryDefinition) get(IM.DEFINITION).asNode();
	}

	/**
	 * Assignes  a previously created query definition to this query
	 * @param queryDefinition the previously created query definition
	 * @return the Query definition containing the query steps
	 */
	public Query setQueryDefinition(QueryDefinition queryDefinition) {
		this.set(IM.DEFINITION,queryDefinition);
		return this;
	}

	/** Creates and assigns a new query definition for this query
	 * The query definition contains the main entity and steps
	 * @return the Query definition
	 */
	public QueryDefinition setQueryDefinition() {
		this.set(IM.DEFINITION,new QueryDefinition());
		return (QueryDefinition) get(IM.DEFINITION).asNode();
	}
}
