package org.endeavourhealth.imapi.model.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.IMQToSparql;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.DataFormatException;

class QueryTest {

	@Test
	public void createQuery() throws IOException, DataFormatException {

		/*
		Query query= buildQuery1();
		//Query query= buildQuery2();
		SearchService ss= new SearchService();
		JSONArray ob= ss.queryIM(query);


		ObjectMapper om= new ObjectMapper();
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		try (FileWriter fw= new FileWriter("c:\\temp\\Query.json")) {
			fw.write(om.writerWithDefaultPrettyPrinter().writeValueAsString(query));
		}
		try (FileWriter fw= new FileWriter("c:\\temp\\Result.json")) {
		 fw.write(om.writerWithDefaultPrettyPrinter().writeValueAsString(ob));
		}

		 */
	}

	private Query buildQuery2() throws DataFormatException {
		Query query= new Query();
		query.addSelect("id")
			.addSelect(new Selection().setProperty("im:code").setAlias("code"))
			.addSelect(new Selection().setProperty("rdfs:label").setAlias("term"))
			.addSelect(new Selection().setProperty("im:matchedTo").setInverseOf(true)
				.addSelect(new Selection().setProperty("rdfs:label").setAlias("legacyTerm"))
				.addSelect(new Selection().setProperty("im:code").setAlias("legacyCode"))
				.addSelect(new Selection().setProperty("im:usageTotal").setAlias("usageTotal")))
			.setWhere(new Match()
				.setEntityId(TTIriRef.iri(IM.NAMESPACE+"VSET_EncounterTypes"))
				.setIncludeMembers(true));
		return query;
	}

	private Query buildQuery1() {
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
		return query;
	}

}