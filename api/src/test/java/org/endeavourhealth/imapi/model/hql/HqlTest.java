package org.endeavourhealth.imapi.model.hql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.Test;

class HqlTest {

	@Test
	public void createQuery() throws JsonProcessingException {
		Hql hql= new Hql();
		hql.addSelect("id")
			.addSelect("name")
			.addSelect("code")
			.addSelect("usageTotal");
		Match match= new Match();
		hql.setWhere(match);
		match.setEntityType(IM.CONCEPT);
		match.setGraph(SNOMED.GRAPH_SNOMED);
		match.setProperty(IM.HAS_STATUS)
				.addValueIn(IM.UNASSIGNED);



		ObjectMapper om= new ObjectMapper();
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(hql));
	}

}