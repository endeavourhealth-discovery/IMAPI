package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.tripletree.TTIri;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

class SelectTest {

	@Test
	public void createShapeQuery() throws IOException, DataFormatException {

		/*
		//SetModel query= buildEntityModel1();
		SetModel query = buildQuery2();



		ObjectMapper om= new ObjectMapper();
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		try (FileWriter fw= new FileWriter("c:\\temp\\SetModel.json")) {
			fw.write(om.writerWithDefaultPrettyPrinter().writeValueAsString(query));
		}
		try (FileWriter fw= new FileWriter("c:\\temp\\Result.json")) {
		 fw.write(om.writerWithDefaultPrettyPrinter().writeValueAsString(ob));
		}


		 */
		 
	}

	private SetModel buildQuery2() throws DataFormatException {
		SetModel setModel = new SetModel()
			.addProperty(new PropertyMap()
				.setVar("id")
				.setAlias("id"))
				.addProperty(new PropertyMap()
					.setVar("code")
					.setAlias("code"))
					.addProperty(new PropertyMap()
						.setVar("label")
						.setAlias("name"));
		setModel.addProperty(new PropertyMap()
			.setVar("matchedTo")
			.setObject(new SetModel()
				.addProperty(new PropertyMap()
					.setVar("legacyCode")
					.setAlias("legacyCode"))
				.addProperty(new PropertyMap()
					.setVar("legacyLabel")
					.setAlias("legacyTerm"))
				.addProperty(new PropertyMap()
					.setVar("usage")
					.setAlias("usage"))));

		setModel
			.setMatch(new Match()
				.setEntityId(TTIri.iri(IM.NAMESPACE+"VSET_EncounterTypes"))
					.setIncludeMembers(true)
				.addMay(new Match()
				.setProperty(IM.CODE)
				.setValueVar("code"))
			.addMay(new Match()
				.setProperty(RDFS.LABEL)
				.setValueVar("label"))
			.addMay(new Match()
			.setProperty(IM.MATCHED_TO)
			.setInverseOf(true)
			.setValueObject(new Match()
				.addMay(new Match()
					.setProperty(IM.CODE)
					.setValueVar("legacyCode"))
				.addMay(new Match()
					.setProperty(RDFS.LABEL)
					.setValueVar("legacyLabel"))
				.addAnd(new Match()
					.setProperty(IM.HAS_SCHEME)
					.setValueVar("scheme")
					.setValueIn(List.of(IM.GRAPH_ENCOUNTERS)))
					.addMay(new Match()
						.setProperty(IM.USAGE_TOTAL)
						.setValueVar("usage")))));
		return setModel;
	}

	private SetModel buildQuery1() {
		SetModel query = new SetModel();
		query.addProperty(new PropertyMap()
				.setVar("id"))
			.addProperty(new PropertyMap().setVar("label"))
			.addProperty( new PropertyMap().setVar("code"))
			.addProperty(new PropertyMap().setVar("im:usage"))
				.setMatch(new Match()
					.setEntityType(TTIriRef.iri(IM.CONCEPT.getIri()))
					.setGraph(SNOMED.GRAPH_SNOMED)
		.setProperty(IM.HAS_STATUS)
			.addValueIn(TTIriRef.iri("im:Unassigned")));
		return query;
	}

}