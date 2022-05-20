package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

class DataSetTest {
	SearchService searchService = new SearchService();



	@Test
	void testBuilders() throws IOException, DataFormatException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {
		DataSet dataSet;


		/*
		dataSet= query1();
		output(dataSet,searchService);
		dataSet= query2();
		output(dataSet,searchService);
		dataSet= query4();
		output(dataSet,searchService);
		dataSet= query5();
		output(dataSet,searchService);
		dataSet= query6();
		output(dataSet,searchService);
		dataSet= query7();
		output(dataSet,searchService);
		dataSet= query8();
		output(dataSet,searchService);
		dataSet= query9();
		output(dataSet,searchService);

		SearchRequest request= query10();
		outputOS(request,searchService,"substancesByTerm");

		 */



	}

	private SearchRequest query10() {
		SearchRequest request= new SearchRequest();
		request.setIndex("david");
		request.setSize(20);
		request.setPage(20);
		request.setTermFilter("substance");
		request.setIsA(List.of(SNOMED.NAMESPACE+"105590001"));
		request.setStatusFilter(Arrays.asList(IM.ACTIVE.getIri()));
		request.getSelect().add("iri");
		request.getSelect().add("name");

		return request;
	}

	private DataSet query9() {
		DataSet dataSet= new DataSet()
			.setUsePrefixes(true)
			.setName("Ranges for finding site")
			.setDescription("retrieves the high level concepts allowable as values of the attribute finding side")
			.setSelect(new Select()
				.addProperty(new PropertyObject()
					.setIri(RDFS.LABEL))
				.setFilter(new Filter()
					.setProperty(RDFS.RANGE)
					.setInverseOf(true)
					.addValueConcept(ConceptRef.iri(SNOMED.NAMESPACE+"363698007")
						.setIncludeSupertypes(true))));
		return dataSet;
	}


	private static DataSet query8() throws JsonProcessingException {
		DataSet dataSet= new DataSet()
			.setUsePrefixes(true)
			.setName("health care organisations in EW1")
			.setDescription("name and address of organisations with post code starting with EW1")
			.setSelect(new Select()
				.setEntityType(ConceptRef.iri(IM.NAMESPACE+"Organisation","Organisation"))
				.addProperty(new PropertyObject(RDFS.LABEL)
				.setAlias("name"))
			.addProperty(new PropertyObject(IM.NAMESPACE+"address")
				.setObject(new Select()
					.addProperty(new PropertyObject()
					.setBinding("*"))))
			.setFilter(new Filter()
				.setProperty(ConceptRef.iri(IM.NAMESPACE+"address"))
				.setValueVar("address")
				.setValueObject(new Filter()
					.setProperty(ConceptRef.iri(IM.NAMESPACE+"postCode"))
					.setValueCompare(new Compare()
						.setValue("E1W 3DP")))));

		return dataSet;
	}


	private static DataSet query7(){
		DataSet prof= new DataSet()
		.setIri(IM.NAMESPACE + "Q_RegisteredGMS")
			.setName("Patients registered for GMS services on the reference date")
			.setDescription("For any registration period,a registration start date before the reference date and no end date," +
				"or an end date after the reference date.")
			.setSelect(new Select()
				.setEntityType(ConceptRef.iri(IM.NAMESPACE+"Person","Person"))
		   .setFilter(new Filter()
			.setProperty(ConceptRef.iri(IM.NAMESPACE+"isSubjectOf","has GP registration"))
			.setValueObject(new Filter()
				.setEntityType(ConceptRef.iri(IM.NAMESPACE+"GPRegistration"))
				.addAnd(new Filter()
					.setName("patient type is regular GMS Patient")
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "patientType"))
					.addValueConcept(ConceptRef.iri(IM.GMS_PATIENT.getIri(),"Regular GMS patient")))
				.addAnd(new Filter()
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "effectiveDate"))
					.setName("start of registration is before the reference date")
					.setValueCompare(Comparison.LESS_THAN_OR_EQUAL, "$ReferenceDate"))
				.addOr(new Filter()
					.setNotExist(true)
					.setName("the registration has not ended ")
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "endDate")))
				.addOr(new Filter()
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "endDate"))
					.setName("the end of registration is after the reference date")
					.setValueCompare(Comparison.GREATER_THAN, "$ReferenceDate")))));
		return prof;
	}


	private static DataSet query5(){
		return new DataSet()
			.setName("AllowablePropertiesForCovid")
			.setDescription("gets the active properties and their subtypes that have a domain which is a super type of covid.")
			.setActiveOnly(true)
			.setSelect(new Select()
				.setDistinct(true)
				.setEntityType(ConceptRef.iri(RDF.PROPERTY.getIri())
					.setIncludeSubtypes(true))
				.addProperty(new PropertyObject("id"))
			.addProperty(new PropertyObject(IM.CODE))
			.addProperty(new PropertyObject(RDFS.LABEL))
			.setFilter(new Filter()
					.setProperty(RDFS.DOMAIN)
					.addValueConcept(ConceptRef.iri(SNOMED.NAMESPACE+"674814021000119106")
						.setIncludeSupertypes(true))));
	}


	private static DataSet query4() {

		return new DataSet()
			.setName("AsthmaSubTypesCore")
			.setSelect(new Select()
				.setEntityId(ConceptRef.iri(SNOMED.NAMESPACE+"195967001")
				.setIncludeSubtypes(true))
				.addProperty(new PropertyObject()
					.setIri(RDFS.LABEL)
					.setAlias("term"))
				.addProperty(new PropertyObject(IM.CODE)
					.setAlias("code")));
	}

	private static DataSet query2() {

		return new DataSet()
			.setName("PropertiesOfShapesUsingDateOfBirth")
			.setDescription("all of the data model properties for entities that have a property df a data of birth")
			.setUsePrefixes(true)
			.setResultFormat(ResultFormat.OBJECT)
			.setSelect(new Select()
				.setEntityType(SHACL.NODESHAPE)
				.addProperty(new PropertyObject(SHACL.PROPERTY)
				.setObject(new Select()
					.addProperty(new PropertyObject()
						.setBinding("*"))))
				.setFilter(new Filter()
					.setProperty(SHACL.PROPERTY)
					.setValueObject(new Filter()
					.setProperty(SHACL.PATH)
					.addValueConcept(ConceptRef.iri(IM.NAMESPACE+"dateOfBirth")))));

	}
	private static DataSet query6() {
		return new DataSet()
			.setName("EverythingAboutShapesUsingDateOfBirth")
			.setDescription("all of the properties and relationships for entities that have a property of a date of birth")
			.setUsePrefixes(true)
			.setResultFormat(ResultFormat.OBJECT)
			.setSelect(new Select()
				.setEntityType(SHACL.NODESHAPE)
				.addProperty(new PropertyObject()
				.setBinding("*"))
			.setFilter(new Filter()
				.setProperty(SHACL.PROPERTY)
				.setValueObject(new Filter()
					.setProperty(SHACL.PATH)
					.addValueConcept(ConceptRef.iri(IM.NAMESPACE+"dateOfBirth")))));

	}



	private static DataSet query1() {
		return new DataSet()
			.setName("FamilyHistoryExpansionObjectFormat")
			.setDescription("expands the family history value set including legacy codes")
			.setResultFormat(ResultFormat.OBJECT)
			.setUsePrefixes(true)
			.setSelect(new Select()
				.setEntityIn(TTIriRef.iri(IM.NAMESPACE+"VSET_FamilyHistory"))
				.addProperty( new PropertyObject(RDFS.LABEL)
					.setAlias("term"))
				.addProperty(new PropertyObject(IM.CODE))
				.addProperty(new PropertyObject(IM.MATCHED_TO)
					.setInverseOf(true)
					.setObject(new Select()
						.addProperty(new PropertyObject(RDFS.LABEL)
							.setAlias("legacyTerm"))
						.addProperty(new PropertyObject(IM.CODE)
							.setAlias("legacyCode")))));
	}

	private static void output(DataSet dataSet,SearchService searchService) throws IOException, DataFormatException {
		/*
		String json = dataSet.getasJson();
		try (FileWriter wr = new FileWriter("c:\\examples\\querydefinitions\\" + dataSet.getName() + ".json")) {
			wr.write(json);
		}
		ObjectMapper om= new ObjectMapper();
		DataSet query= om.readValue(json,DataSet.class);
		ResultNode result = searchService.queryIM(dataSet);
		try (FileWriter wr = new FileWriter("c:\\examples\\queryresults\\" + dataSet.getName() + "_result.json")) {
			wr.write(result.asJson());
		}

		 */



	}

	private static void outputOS(SearchRequest request,SearchService searchService,String name) throws IOException, DataFormatException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {

		ObjectMapper om= new ObjectMapper();
		String json = om.writeValueAsString(request);
		try (FileWriter wr = new FileWriter("c:\\examples\\querydefinitions\\" + name+ ".json")) {
			wr.write(json);
		}
		List<SearchResultSummary> result= searchService.getEntitiesByTerm(request);
		try (FileWriter wr = new FileWriter("c:\\examples\\queryresults\\" + name + "_result.json")) {
			wr.write(om.writerWithDefaultPrettyPrinter().writeValueAsString(result));
		}





	}





}
