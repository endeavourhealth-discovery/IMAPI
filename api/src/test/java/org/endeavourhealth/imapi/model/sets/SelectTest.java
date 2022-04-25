package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIri;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

class SelectTest {

	@Test
	public void createShapeQuery() throws IOException, DataFormatException {

		/*
		//DataSet query= buildEntityModel1();
		DataSet query = buildQuery2();



		ObjectMapper om= new ObjectMapper();
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		try (FileWriter fw= new FileWriter("c:\\temp\\DataSet.json")) {
			fw.write(om.writerWithDefaultPrettyPrinter().writeValueAsString(query));
		}
		try (FileWriter fw= new FileWriter("c:\\temp\\Result.json")) {
		 fw.write(om.writerWithDefaultPrettyPrinter().writeValueAsString(ob));
		}


		 */
		 
	}

	private DataSet buildQuery2() throws DataFormatException {
		DataSet dataSet = new DataSet()
			.addSelect(new Select()
				.setVar("id")
				.setAlias("id"))
				.addSelect(new Select()
					.setVar("code")
					.setAlias("code"))
					.addSelect(new Select()
						.setVar("label")
						.setAlias("name"));
		dataSet.addSelect(new Select()
			.setVar("matchedTo")
			.setObject(new DataSet()
				.addSelect(new Select()
					.setVar("legacyCode")
					.setAlias("legacyCode"))
				.addSelect(new Select()
					.setVar("legacyLabel")
					.setAlias("legacyTerm"))
				.addSelect(new Select()
					.setVar("usage")
					.setAlias("usage"))));

		dataSet
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
		return dataSet;
	}

	private DataSet buildQuery1() {
		DataSet query = new DataSet();
		query.addSelect(new Select()
				.setVar("id"))
			.addSelect(new Select().setVar("label"))
			.addSelect( new Select().setVar("code"))
			.addSelect(new Select().setVar("im:usage"))
				.setMatch(new Match()
					.setEntityType(TTIriRef.iri(IM.CONCEPT.getIri()))
					.setGraph(SNOMED.GRAPH_SNOMED)
		.setProperty(IM.HAS_STATUS)
			.addValueIn(TTIriRef.iri("im:Unassigned")));
		return query;
	}

}