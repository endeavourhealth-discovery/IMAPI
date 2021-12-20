package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;
import java.util.stream.Collectors;

/**
	* A serializable query object containing a sequence of clauses resulting in the definition of a  data set output.
	* <p> In effect a high level process model taylored to the health query ues cases</p>
	*/
public class Query  extends TTEntity {
	public static String dataModelGraph= "http://endhealth.info/im#";


	public Query(){
		this.addType(IM.QUERY);
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
	public List<QueryClause> getClauses() {
		return get(IM.CLAUSE)==null ?null :
		get(IM.CLAUSE).getAsOrdered().stream()
			.map(e-> (QueryClause) e)
			.collect(Collectors.toList());
	}

	/**
	 * Assigns  a previously created query definition to this query
	 * @return the Query definition containing the query steps
	 */
	public Query setClauses(TTArray clauses) {
		this.set(IM.DEFINITION,clauses);
		return this;
	}

	/** Creates and assigns a new query definition for this query
	 * The query definition contains the main entity and steps
	 * @return the Query definition
	 */
	public QueryClause addClause() {
		QueryClause clause= new QueryClause();
		clause.set(IM.HAS_ORDER, TTLiteral.literal(get(IM.HAS_ORDER)==null ?0: get(IM.HAS_ORDER).size()));
		this.addObject(IM.CLAUSE,clause);
		return clause;
	}
}
