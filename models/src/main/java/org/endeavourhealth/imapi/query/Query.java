package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;
import java.util.stream.Collectors;

/**
	* A serializable query object containing a sequence of clauses resulting in the definition of a  data set output.
	* <p> In effect a high level process model taylored to the health query ues cases</p>
	*/
public class Query  extends TTEntity {


	public Query(){
		this.addType(IM.QUERY);
	}

	public static int varCount;


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
	public QueryClause getDefinition() {
		return (QueryClause) get(IM.DEFINITION).asNode();
	}



	/** Creates and assigns a new query definition for this query
	 * The query definition contains the main entity and steps
	 * @return the Query definition
	 */
	public QueryClause setDefinition() {
		QueryClause clause= new QueryClause();
		this.set(IM.DEFINITION,clause);
		return clause;
	}

}
