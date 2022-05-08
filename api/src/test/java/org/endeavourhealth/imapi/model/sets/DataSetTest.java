package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.DataFormatException;

class DataSetTest {


	@Test
	void testBuilders() throws IOException, DataFormatException {
		DataSet dataSet;
		/*
		dataSet= query1();
		output(dataSet);
		dataSet= query2();
		output(dataSet);
		dataSet= query4();
		output(dataSet);

		 */
		dataSet= query5();
		output(dataSet);

		dataSet= query6();
		output(dataSet);
		dataSet= query7();
		output(dataSet);

		dataSet= query8();
		output(dataSet);


	}
	private static DataSet query8() throws JsonProcessingException {
		DataSet dataSet= new DataSet()
			.setUsePrefixes(true)
			.setName("health care organisations in EW1")
			.setDescription("name and address of organisations with post code starting with EW1")
			.addSelect(new Select()
				.setBinding("name"))
			.addSelect(new Select()
				.setBinding("address")
				.addObject(new Select()
					.setBinding("*")))
			.setMatch(new Match()
				.setEntityType(ConceptRef.iri(IM.NAMESPACE+"Organisation","Organisation"))
				.setProperty(ConceptRef.iri(IM.NAMESPACE+"address"))
				.setValueVar("address")
				.setValueObject(new Match()
					.setProperty(ConceptRef.iri(IM.NAMESPACE+"postCode"))
					.setValueCompare(new Compare()
						.setValue("E1W 3DP")))
				.addOptional(new Match()
					.setProperty(RDFS.LABEL)
					.setValueVar("name")));
		String json= dataSet.getasJson();
		ObjectMapper om= new ObjectMapper();
		DataSet newDataSet= om.readValue(json,DataSet.class);

		return dataSet;
	}


	private static DataSet query7(){
		DataSet prof= new DataSet()
		.setIri(IM.NAMESPACE + "Q_RegisteredGMS")
			.setName("Patients registered for GMS services on the reference date")
			.setDescription("For any registration period,a registration start date before the reference date and no end date," +
				"or an end date after the reference date.")
		.setMatch(new Match()
			.setEntityType(ConceptRef.iri(IM.NAMESPACE+"Person","Person"))
			.setProperty(ConceptRef.iri(IM.NAMESPACE+"hasSubject","has GP registration"))
			.setInverseOf(true)
			.setValueObject(new Match()
				.setEntityType(ConceptRef.iri(IM.NAMESPACE+"GPRegistration"))
				.addAnd(new Match()
					.setName("patient type is regular GMS Patient")
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "patientType"))
					.addValueConcept(ConceptRef.iri(IM.GMS_PATIENT.getIri(),"Regular GMS patient")))
				.addAnd(new Match()
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "effectiveDate"))
					.setName("start of registration is before the reference date")
					.setValueCompare(Comparison.LESS_THAN_OR_EQUAL, "$ReferenceDate"))
				.addOr(new Match()
					.setNotExist(true)
					.setName("the registration has not ended ")
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "endDate")))
				.addOr(new Match()
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "endDate"))
					.setName("the end of registration is after the reference date")
					.setValueCompare(Comparison.GREATER_THAN, "$ReferenceDate"))));
		return prof;
	}

	private static DataSet query5(){
		return new DataSet()
			.setName("AllowablePropertiesForCovid")
			.setDescription("gets the active properties and their subtypes that have a domain which is a super type of covid.")
			.setActiveOnly(true)
			.setDistinct(true)
			.addSelect(new Select()
				.setBinding("id"))
			.addSelect(new Select()
				.setBinding("code"))
			.addSelect(new Select()
				.setBinding("term"))
			.setMatch(new Match()
				.addOptional(new Match()
					.setProperty(RDFS.LABEL)
					.setValueVar("term"))
				.addOptional(new Match()
					.setProperty(IM.CODE)
					.setValueVar("code"))
					.setProperty(RDFS.DOMAIN)
					.setIncludeSubEntities(true)
					.addValueConcept(ConceptRef.iri(SNOMED.NAMESPACE+"674814021000119106").setIncludeSupertypes(true)));
	}

	private static DataSet query4() {


		return new DataSet()
			.setName("AsthmaSubTypesCore")
			.setMatch(new Match()
				.setEntityId(TTIriRef.iri(SNOMED.NAMESPACE+"195967001"))
				.setIncludeSubEntities(true)
				.addOptional(new Match()
					.setProperty(RDFS.LABEL)
					.setValueVar("label"))
				.addOptional(new Match()
					.setProperty(IM.CODE)
					.setValueVar("code")))
			.addSelect(new Select()
				.setBinding("label")
				.setAlias("term"))
			.addSelect(new Select()
				.setBinding("code")
				.setAlias("code"));
	}

	private static DataSet query2() {

		return new DataSet()
			.setName("PropertiesOfShapesUsingDateOfBirth")
			.setDescription("all of the data model properties for entities that have a property df a data of birth")
			.setUsePrefixes(true)
			.setResultFormat(ResultFormat.OBJECT)
			.addSelect(new Select()
				.setBinding("property")
				.addObject(new Select()
					.setBinding("*")))
			.setMatch(new Match()
				.addOptional(new Match()
					.setProperty(SHACL.PROPERTY)
					.setValueVar("property"))
				.setEntityType(SHACL.NODESHAPE)
				.setProperty(SHACL.PROPERTY)
				.setValueObject(new Match()
					.setProperty(SHACL.PATH)
					.addValueConcept(ConceptRef.iri(IM.NAMESPACE+"dateOfBirth"))));

	}

	private static DataSet query6() {
		return new DataSet()
			.setName("EverythingAboutShapesUsingDateOfBirth")
			.setDescription("all of the properties and relationships for entities that have a property of a date of birth")
			.setUsePrefixes(true)
			.setResultFormat(ResultFormat.OBJECT)
			.addSelect(new Select()
				.setBinding("*"))
			.setMatch(new Match()
				.setEntityType(SHACL.NODESHAPE)
				.setProperty(SHACL.PROPERTY)
				.setValueObject(new Match()
					.setProperty(SHACL.PATH)
					.addValueConcept(ConceptRef.iri(IM.NAMESPACE+"dateOfBirth"))));

	}

	private static void output(DataSet dataSet) throws IOException, DataFormatException {

		SearchService ss= new SearchService();
		String json= dataSet.getasJson();
		try (FileWriter wr= new FileWriter("c:\\examples\\querydefinitions\\"+ dataSet.getName()+".json")){
			wr.write(json);
		}
		ResultNode result= ss.queryIM(dataSet);
		try (FileWriter wr= new FileWriter("c:\\examples\\queryresults\\"+ dataSet.getName()+"_result.json")){
			wr.write(result.asJson());
		}

	}

	private static DataSet query1() {
		return new DataSet()
			.setName("FamilyHistoryExpansionObjectFormat")
			.setDescription("expands the family history value set including legacy codes")
			.setResultFormat(ResultFormat.OBJECT)
			.setUsePrefixes(true)
			.addSelect(new Select()
				.setBinding("term"))
			.addSelect(new Select()
				.setBinding("code"))
			.addSelect(new Select()
				.setBinding("matchedTo")
				.addObject(new Select()
					.setBinding("legacyTerm"))
				.addObject(new Select()
					.setBinding("legacyCode"))
				.addObject(new Select()
					.setBinding("scheme")))
			.setMatch(new Match()
						.setEntityIn(TTIriRef.iri(IM.NAMESPACE+"VSET_FamilyHistory"))
				.addOptional(new Match()
					.setProperty(IM.CODE)
					.setValueVar("code"))
				.addOptional(new Match()
					.setProperty(RDFS.LABEL)
					.setValueVar("term"))
				.addOptional(new Match()
					.setProperty(IM.MATCHED_TO)
					.setInverseOf(true)
					.setValueVar("matchedTo")
					.setValueObject(new Match()
						.addOptional(new Match()
							.setProperty(IM.CODE)
							.setValueVar("legacyCode"))
						.addOptional(new Match()
							.setProperty(RDFS.LABEL)
							.setValueVar("legacyTerm"))
						.addAnd(new Match()
							.setProperty(IM.HAS_SCHEME)
							.setValueVar("scheme")
							.addValueConcept(ConceptRef.iri(IM.GRAPH_ENCOUNTERS.getIri()))))));
	}

}
