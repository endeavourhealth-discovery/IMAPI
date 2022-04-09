package org.endeavourhealth.imapi.model.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.Test;

class QueryTest {

	@Test
	public void createQuery() throws JsonProcessingException {
		Query query = new Query();
		query.addSelect("im:id")
			.addSelect("rdfs:label")
			.addSelect("im:code")
			.addSelect("im:usageTotal");
		Match match= new Match();
		query.setWhere(match);
		match.setEntityType(TTIriRef.iri("im:Concept"));
		match.setGraph(SNOMED.GRAPH_SNOMED);
		match.setProperty(TTIriRef.iri("im:status"))
				.addValueIn(TTIriRef.iri("im:Unassigned"));



		ObjectMapper om= new ObjectMapper();
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(query));
	}

}