package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.FilterClient;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sets.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.*;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

class IMQueryTest {
	SearchService searchService = new SearchService();



	@Test
	void testBuilders() throws DataFormatException, IOException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {
		Query query;
/*
		query= query1();
		output(query,searchService);
		query= query2();
		output(query,searchService);
		query= query4();
		output(query,searchService);
		query= query5();
		output(query,searchService);
		query= query6();
		output(query,searchService);
		query= query7();
		output(query,searchService);

		query= query8();
		output(query,searchService);

		query= query9();
		output(query,searchService);

		SearchRequest request= query10();
		outputOS(request,searchService,"substancesByTerm");

		query= query11();
		output(query,searchService);

			query= query12();
		output(query,searchService);


		query= query13();
		output(query,searchService);

*/

	}


	private Query query13() {
		return new Query()
			.setName("Some Barts cerner codes with context including a regex")
			.setUsePrefixes(true)
			.setResultFormat(ResultFormat.OBJECT)
			.setSelect(new Select()
				.addProperty(new PropertySelect(RDFS.LABEL.getIri()))
				.addProperty(new PropertySelect(IM.CODE.getIri()))
				.addProperty(new PropertySelect(IM.HAS_SOURCE_CONTEXT.getIri())
					.setSelect(new Select()
						.addProperty(new PropertySelect().setBinding("*"))))
				.setMatch(new Match()
					.setProperty(IM.HAS_SOURCE_CONTEXT)
					.setMatch(
						new Match()
							.setProperty(IM.SOURCE_REGEX)
					)));
	}

	private Query query12() {
		return new Query()
			.setName("Some codes not matched")
			.setUsePrefixes(true)
			.setResultFormat(ResultFormat.OBJECT)
			.setSelect(new Select()
				.addProperty(new PropertySelect().setIri(RDFS.LABEL.getIri()))
				.addProperty(new PropertySelect().setIri(IM.CODE.getIri()))
				.setMatch(new Match()
					.addAnd(new Match()
						.setNotExist(true)
						.setProperty(IM.MATCHED_TO))
					.addAnd(new Match()
						.setProperty(IM.HAS_SCHEME)
						.addIsConcept(IM.CODE_SCHEME_BARTS_CERNER))));
	}

	private Query query11() {
		return new Query()
			.setName("Data model for entity")
			.setDescription("Get the properties of an entity")
			.setMainEntity(SHACL.NODESHAPE)
			.setResultFormat(ResultFormat.OBJECT)
			.setUsePrefixes(true)
			.setSelect(new Select().addProperty(new PropertySelect().setBinding("*"))
				.setMatch(new Match()
					.setEntityId(TTIriRef.iri(IM.NAMESPACE+"Entity"))));

	}

	private SearchRequest query10() {
		SearchRequest request= new SearchRequest();
		request.setSize(20);
		request.setPage(1);
		request.setTermFilter("amiloride");
		request.setIsA(List.of(SNOMED.NAMESPACE+"105590001"));
		request.setStatusFilter(List.of(IM.ACTIVE.getIri()));
		request.getSelect().add("iri");
		request.getSelect().add("name");

		return request;
	}

	private Query query9() {
		return new Query()
			.setUsePrefixes(true)
			.setResultFormat(ResultFormat.OBJECT)
			.setName("Ranges for finding site")
			.setDescription("retrieves the high level concepts allowable as values of the attribute finding side")
			.setSelect(new Select()
				.addProperty(new PropertySelect()
					.setIri(RDFS.LABEL.getIri()))
				.setMatch(new Match()
					.setProperty(RDFS.RANGE)
					.setInverseOf(true)
					.addIsConcept(ConceptRef.iri(SNOMED.NAMESPACE+"363698007")
						.setIncludeSupertypes(true))));
	}


	private static Query query8() {

		return new Query()
			.setUsePrefixes(true)
			.setName("health care organisations in EW1")
			.setDescription("name and address of organisations with post code starting with EW1")
			.setSelect(new Select()
				.setEntityType(ConceptRef.iri(IM.NAMESPACE+"Organisation","Organisation"))
				.addProperty(new PropertySelect(RDFS.LABEL.getIri())
				.setAlias("name"))
			.addProperty(new PropertySelect()
				.setIri(IM.NAMESPACE+"address")
				.setSelect(new Select()
					.addProperty(new PropertySelect(IM.NAMESPACE+"postCode")
						.setAlias("postCode"))
			.setMatch(new Match()
				.setProperty(ConceptRef.iri(IM.NAMESPACE+"address"))
				.setValueVar("address")
				.setMatch(new Match()
					.setProperty(ConceptRef.iri(IM.NAMESPACE+"postCode"))
					.setValue(new Compare()
						.setValueData("E1W 3DP")))))));
	}


	private static Query query7(){
		return new Query()
		.setIri(IM.NAMESPACE + "Q_RegisteredGMS")
			.setName("Patients registered for GMS services on the reference date")
			.setDescription("For any registration period,a registration start date before the reference date and no end date," +
				"or an end date after the reference date.")
			.setSelect(new Select()
				.setEntityType(ConceptRef.iri(IM.NAMESPACE+"Person","Person"))
		   .setMatch(new Match()
			.setProperty(ConceptRef.iri(IM.NAMESPACE+"isSubjectOf","has GP registration"))
			.setMatch(new Match()
				.setEntityType(ConceptRef.iri(IM.NAMESPACE+"GPRegistration"))
				.addAnd(new Match()
					.setName("patient type is regular GMS Patient")
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "patientType"))
					.addIsConcept(ConceptRef.iri(IM.GMS_PATIENT.getIri(),"Regular GMS patient")))
				.addAnd(new Match()
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "effectiveDate"))
					.setName("start of registration is before the reference date")
					.setValue(Comparison.LESS_THAN_OR_EQUAL, "$ReferenceDate"))
				.addOr(new Match()
					.setNotExist(true)
					.setName("the registration has not ended ")
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "endDate")))
				.addOr(new Match()
					.setProperty(ConceptRef.iri(IM.NAMESPACE + "endDate"))
					.setName("the end of registration is after the reference date")
					.setValue(Comparison.GREATER_THAN, "$ReferenceDate")))));
	}


	private static Query query5(){
		return new Query()
			.setName("AllowablePropertiesForCovid")
			.setDescription("gets the active properties and their subtypes that have a domain which is a super type of covid.")
			.setActiveOnly(true)
			.setSelect(new Select()
				.setDistinct(true)
				.setEntityType(ConceptRef.iri(RDF.PROPERTY.getIri())
					.setIncludeSubtypes(true))
				.addProperty(new PropertySelect("id")
					.setAlias("id"))
			.addProperty(new PropertySelect(IM.CODE.getIri())
				.setAlias("code"))
			.addProperty(new PropertySelect(RDFS.LABEL.getIri())
				.setAlias("term"))
			.setMatch(new Match()
					.setProperty(RDFS.DOMAIN)
					.addIsConcept(ConceptRef.iri(SNOMED.NAMESPACE+"674814021000119106")
						.setIncludeSupertypes(true))));
	}


	private static Query query4() {

		return new Query()
			.setName("AsthmaSubTypesCore")
			.setSelect(new Select()
				.setEntityId(ConceptRef.iri(SNOMED.NAMESPACE+"195967001")
				.setIncludeSubtypes(true))
				.addProperty(new PropertySelect()
					.setIri(RDFS.LABEL.getIri())
					.setAlias("term"))
				.addProperty(new PropertySelect(IM.CODE.getIri())
					.setAlias("code")));
	}

	private static Query query2() {

		return new Query()
			.setName("PropertiesOfShapesUsingDateOfBirth")
			.setDescription("all of the data model properties for entities that have a property df a data of birth")
			.setUsePrefixes(true)
			.setResultFormat(ResultFormat.OBJECT)
			.setSelect(new Select()
				.setEntityType(SHACL.NODESHAPE)
				.addProperty(new PropertySelect(SHACL.PROPERTY.getIri())
				.setSelect(new Select()
					.addProperty(new PropertySelect()
						.setBinding("*"))))
				.setMatch(new Match()
					.setProperty(SHACL.PROPERTY)
					.setMatch(new Match()
					.setProperty(SHACL.PATH)
					.addIsConcept(ConceptRef.iri(IM.NAMESPACE+"dateOfBirth")))));

	}
	private static Query query6() {
		return new Query()
			.setName("EverythingAboutShapesUsingDateOfBirth")
			.setDescription("all of the properties and relationships for entities that have a property of a date of birth")
			.setUsePrefixes(true)
			.setResultFormat(ResultFormat.OBJECT)
			.setSelect(new Select()
				.setEntityType(SHACL.NODESHAPE)
				.addProperty(new PropertySelect()
				.setBinding("*"))
			.setMatch(new Match()
				.setProperty(SHACL.PROPERTY)
				.setMatch(new Match()
					.setProperty(SHACL.PATH)
					.addIsConcept(ConceptRef.iri(IM.NAMESPACE+"dateOfBirth")))));

	}



	private static Query query1() {
		return new Query()
			.setName("FamilyHistoryExpansionObjectFormat")
			.setDescription("expands the family history value set including legacy codes")
			.setResultFormat(ResultFormat.OBJECT)
			.setUsePrefixes(true)
			.setSelect(new Select()
				.setEntityIn(TTIriRef.iri(IM.NAMESPACE+"VSET_FamilyHistory"))
				.addProperty( new PropertySelect(RDFS.LABEL.getIri())
					.setAlias("term"))
				.addProperty(new PropertySelect(IM.CODE.getIri()))
				.addProperty(new PropertySelect(IM.MATCHED_TO.getIri())
					.setInverseOf(true)
					.setSelect(new Select()
						.addProperty(new PropertySelect(RDFS.LABEL.getIri())
							.setAlias("legacyTerm"))
						.addProperty(new PropertySelect(IM.CODE.getIri())
							.setAlias("legacyCode")))));
	}

	private static void output(Query dataSet, SearchService searchService) throws IOException, DataFormatException {

		String json = dataSet.getasJson();
		try (FileWriter wr = new FileWriter("c:\\examples\\querydefinitions\\" + dataSet.getName() + ".json")) {
			wr.write(json);
		}
		ObjectMapper om= new ObjectMapper();
		ObjectNode result = searchService.queryIM(dataSet);
		try (FileWriter wr = new FileWriter("c:\\examples\\queryresults\\" + dataSet.getName() + "_result.json")) {
			wr.write(om.writerWithDefaultPrettyPrinter().writeValueAsString(result));
		}



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
