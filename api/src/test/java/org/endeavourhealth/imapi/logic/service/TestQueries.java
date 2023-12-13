package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestQueries {
	public static String ex="http://example.org/qry#";

 public static SearchRequest observationConcepts(String input){
	 SearchRequest request= new SearchRequest();
	 request.setIndex("david");
	 request.setTermFilter(input);
	 List<String> schemes= Arrays.asList(IM.NAMESPACE.iri,SNOMED.NAMESPACE.iri);
	 request.setSchemeFilter(schemes);
	 List<String> types= Arrays.asList(IM.CONCEPT_SET.getIri(),IM.CONCEPT.getIri());
	 request.setTypeFilter(types);
	 request.setStatusFilter(Arrays.asList(IM.ACTIVE.getIri()));
	 request
		 .orderBy(f->f.setField("entityType")
			 .addIriValue(IM.MATCH_CLAUSE)
			 .addIriValue(IM.CONCEPT_SET));
	 request
		 .orderBy(a->a.setField("memberOf").addIriValue(TTIriRef.iri(IM.NAMESPACE.iri + "VSET_Observation")));
	 request.orderBy(o->o.setField("name").setStartsWithTerm(true));
	 request.orderBy(o->o.setField("preferredName").setStartsWithTerm(true));
	 request.orderBy(o-> o
		 .setField("subsumptionCount")
		 .setDirection(Order.descending));
	 request.orderBy(o->o
		 .setField("length")
		 .setDirection(Order.ascending));
	 request.orderBy(o->o
		 .setField("weighting")
		 .setDirection(Order.descending));
	 return request;
 }

	public static QueryRequest dataModelPropertyRange() throws JsonProcessingException {
		Query query= new Query()
			.setName("Data model property range")
			.setDescription("get node, class or datatype value (range)  of property objects for specific data model and property")
			.match(m->m
				.setInstanceOf(new Node()
				.setParameter("myDataModel"))
				.property(p->p
					.setIri("http://www.w3.org/ns/shacl#property")
						.match(m1->m1
							.setVariable("shaclProperty")
							.setBool(Bool.and)
								.property(p2->p2
										.setIri(SHACL.PATH.getIri())
										.is(in->in
											.setParameter("myProperty")))
							  .property(p2->p2
									.setBool(Bool.or)
									   .property(p3->p3
												 .setIri(SHACL.CLASS.getIri())
												 .match(m3->m3
													 .setVariable("propType")))
										 .property(p3->p3
												 .setIri(SHACL.NODE.getIri())
												 .match(m3->m3
													 .setVariable("propType")))
										.property(p3->p3
											.setIri(SHACL.DATATYPE.getIri())
												.match(m3->m3
													.setVariable("propType")))))))
			.return_(r->r
					.setNodeRef("propType")
					.property(p->p
							.setIri(RDFS.LABEL.getIri())));
		return new QueryRequest()
			.setQuery(query)
			.argument(a->a
				.setParameter("myDataModel")
				.setValueIri(TTIriRef.iri(IM.NAMESPACE.iri+"Observation")))
			.argument(a->a
				.setParameter("myProperty")
				.setValueIri(TTIriRef.iri(IM.NAMESPACE.iri+"concept")));

	}

	public static QueryRequest rangeSuggestion() throws QueryException{
		return new QueryRequest()
			.setContext(TTManager.createBasicContext())
			.addArgument(new Argument()
				.setParameter("this")
				.setValueIri(TTIriRef.iri(IM.NAMESPACE.iri+"recordOwner")))
			.query(q->q
				.setName("Suggested range for a property")
				.setDescription("get node, class or datatype values (ranges)  of property objects that have 4this as their path")
				.match(m->m
					.property(w->w
						.setIri(SHACL.PATH.getIri())
						.addIs(new Node().setParameter("this")))
					.property(w->w
					.setBool(Bool.or)
						.property(p->p
							.setIri(SHACL.NODE.getIri())
						.match(n->n
							.setVariable("range")))
						.property(p->p
							.setIri(SHACL.CLASS.getIri())
						.match(n->n
							.setVariable("range")))
						.property(p->p
							.setIri(SHACL.DATATYPE.getIri())
							.match(n->n
								.setVariable("range")))))
				.return_(s->s.setNodeRef("range")
					.property(p->p.setIri(RDFS.LABEL.getIri()))));
	}

	public static QueryRequest getShaclProperty(){
		return new QueryRequest()
			.argument(a->a.setParameter("dataModel").setValueIri(TTIriRef.iri(IM.NAMESPACE.iri+"Patient")))
			.argument(a->a.setParameter("property").setValueIri(TTIriRef.iri(IM.NAMESPACE.iri+"age")))
			.query(q->q
				.setName("Query - shacl property predicates for a property is a data model")
				.setDescription("Select the predicates and values and labels of the values for a given data mode and property")
				.match(m->m
					.setInstanceOf(new Node()
					.setParameter("$dataModel"))
					.property(p->p
						.setIri(SHACL.PROPERTY.getIri())
						.match(n->n
							.setVariable("shaclProperty")
							.property(w->w
								.setIri(SHACL.PATH.getIri())
								.is(in->in.setParameter("$property"))))))
				.return_(s->s
					.setNodeRef("shaclProperty")
					.property(p->p
						.setIri(SHACL.CLASS.getIri())
						.return_(n->n
							.property(p1->p1
							.setIri(RDFS.LABEL.getIri()))))
					.property(p->p
					.setIri(SHACL.NODE.getIri())
						.return_(n->n
							.property(p1->p1
								.setIri(RDFS.LABEL.getIri()))))
				.property(p->p
					.setIri(SHACL.DATATYPE.getIri())
					.return_(n->n
						.property(p1->p1.setIri(RDFS.LABEL.getIri()))))
				.property(p->p
					.setIri(SHACL.MAXCOUNT.getIri()))
				.property(p->p
					.setIri(SHACL.MINCOUNT.getIri()))));
	}

	public static QueryRequest getAllowableQueries(){
		return new QueryRequest()
			.setTextSearch("Test for patients either aged between 18 and 65 or with diabetes with the most recent ")
			.argument(a->a
				.setParameter("dataModelIri")
				.setValueIri(TTIriRef.iri(IM.NAMESPACE.iri+"Patient")))
			.query(q->q
				.setName("Allowable queries for patient cohort")
				.match(m->m
					.setTypeOf(IM.NAMESPACE.iri+"CohortQuery")
					.property(p->p
						.setIri(IM.RETURN_TYPE.getIri())
						.is(in->in
							.setParameter("dataModelIri"))))
				.return_(r->r
					.property(p->p
						.setIri(RDFS.LABEL.getIri()))));


	}


	public static QueryRequest getAllowableSubtypes() throws IOException {
		QueryRequest qr= new QueryRequest();
		qr.setContext(TTManager.createBasicContext());
		qr.addArgument(new Argument()
			.setParameter("this")
			.setValueIri(TTIriRef.iri(IM.NAMESPACE.iri+"Concept")));
		Query query= new Query()
			.setName("Allowable child types for a folder")
			.setIri(IM.NAMESPACE.iri+"Query_AllowableChildTypes");
		qr.setQuery(query);
		return qr;
	}



	public static QueryRequest deleteSets(){
		QueryRequest qr= new QueryRequest()
		.addArgument(new Argument()
			.setParameter("this")
			.setValueIri(QR.NAMESPACE))
			.setUpdate(new Update()
				.setIri(IM.NAMESPACE.iri+"DeleteSets")
				.setName("delete sets"));
		return qr;

	}


	public static QueryRequest subtypesParameterised(){
		return new QueryRequest()
			.addArgument(new Argument()
				.setParameter("this")
				.addToValueIriList(TTIriRef.iri("http://snomed.info/sct#76661004"))
				.addToValueIriList(TTIriRef.iri("http://snomed.info/sct#243640007")))
			.setQuery(new Query()
				.setName("Subtypes of concepts as a parameterised query")
				.return_(s->s.setNodeRef("c").property(p->p.setIri(RDFS.LABEL.getIri())))
				.match(f->f
					.setVariable("c")
					.setInstanceOf(new Node()
						.setParameter("this")
						.setDescendantsOrSelfOf(true))));
	}


	public static QueryRequest getMembers(){
		QueryRequest qr= new QueryRequest()
			.setTextSearch("FOXG1")
			.query(q->q
				.setName("Filter concept subtypes that are members of value sets")
				.match(m->m
					.match(m2->m2
						.setBool(Bool.or)
						.match(m1->m1
							.setInstanceOf( new Node().setIri(SNOMED.NAMESPACE.iri+"57148006")
							.setDescendantsOrSelfOf(true)))
						.match(m1->m1
						.setInstanceOf(new Node().setIri(SNOMED.NAMESPACE.iri+"11164009")
						.setDescendantsOrSelfOf(true))))
					.property(w->w
						.setIri(IM.HAS_MEMBER.getIri())
						.setInverse(true)
						.is(n->n
							.setIri(IM.NAMESPACE.iri+"VSET_Conditions"))
						.is(n->n
							.setIri(IM.NAMESPACE.iri+"VSET_ASD")))));
		return qr;
	}


	public static QueryRequest rangeTextSearch(){
		return new QueryRequest()
			.setTextSearch("Hyper")
			.addArgument(new Argument()
				.setParameter("this")
				.addToValueIriList(TTIriRef.iri("http://snomed.info/sct#404684003"))
				.addToValueIriList(TTIriRef.iri("http://snomed.info/sct#71388002")))
			.setQuery(new Query()
				.setName("Get allowable property values with text filter")
				.return_(s->s.property(p->p.setIri(RDFS.LABEL.getIri())))
				.match(f->f
					.setInstanceOf(new Node()
					.setParameter("this")
					.setDescendantsOrSelfOf(true))));
	}

	public static QueryRequest substanceTextSearch(){
		return new QueryRequest()
			.setTextSearch("thia")
			.addArgument(new Argument()
				.setParameter("this")
				.addToValueIriList(TTIriRef.iri("http://snomed.info/sct#105590001")))
			.setQuery(new Query().setIri(IM.NAMESPACE.iri+"Query_GetIsas")
				.setName("substances starting with 'thia'"));
	}

	public static QueryRequest pathDobQuery(){
		return new QueryRequest()
			.setPathQuery(new PathQuery()
				.setName("paths match patient to date of birth")
				.setDepth(3)
				.setSource(IM.NAMESPACE.iri+"Patient")
				.setTarget(IM.NAMESPACE.iri+"dateOfBirth"));

	}
	public static QueryRequest pathQueryAtenolol3() throws JsonProcessingException {
		String json="{\n" +
			"  \"@context\" : {\n" +
			"    \"im\" : \"http://endhealth.info/im#\",\n" +
			"    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",\n" +
			"    \"owl\" : \"http://www.w3.org/2002/07/owl#\",\n" +
			"    \"sh\" : \"http://www.w3.org/ns/shacl#\",\n" +
			"    \"xsd\" : \"http://www.w3.org/2001/XMLSchema#\",\n" +
			"    \"rdfs\" : \"http://www.w3.org/2000/01/rdf-schema#\",\n" +
			"    \"sn\" : \"http://snomed.info/sct#\"\n" +
			"  },\n" +
			"  \"pathQuery\" : {\n" +
			"    \"name\" : \"paths match patient to Atenolol kostas\",\n" +
			"    \"source\" : {\n" +
			"      \"@id\" : \"im:Patient\"\n" +
			"    },\n" +
			"    \"target\" : {\n" +
			"      \"@id\" : \"sn:387506000\"\n" +
			"    },\n" +
			"    \"depth\" : 3\n" +
			"  }\n" +
			"}";
		return new ObjectMapper().readValue(json,QueryRequest.class);
	}

	public static QueryRequest pathToAtenolol(){
		return new QueryRequest()
			.setPathQuery(new PathQuery()
				.setName("paths match patient to Atenolol")
				.setDepth(3)
				.setSource(IM.NAMESPACE.iri+"Patient")
				.setTarget(SNOMED.NAMESPACE.iri+"387506000"));

	}
	public static QueryRequest pathToCSA(){
		return new QueryRequest()
			.setPathQuery(new PathQuery()
				.setName("paths match patient to common service agency")
				.setDepth(3)
				.setSource(IM.NAMESPACE.iri+"Patient")
				.setTarget("http://endhealth.info/im#1000161000252107"));

	}

	public static QueryRequest pathToPostCode(){
		return new QueryRequest()
			.setPathQuery(new PathQuery()
				.setName("paths match patient to post code")
				.setDepth(3)
				.setSource(IM.NAMESPACE.iri+"Patient")
				.setTarget("http://endhealth.info/im#postCode"));

	}



	public static QueryRequest query1() {
		Query query = new Query()
			.setName("FamilyHistoryExpansionObjectFormat");
		query
			.match(f->f
			.property(w->w
				.setIri(IM.HAS_MEMBER.getIri())
				.setInverse(true)
				.addIs(new Node().setIri(IM.NAMESPACE.iri+"VSET_FamilyHistory"))))
			.return_(s->s
				.property(p->p
				.setIri(RDFS.LABEL.getIri()))
			.property(p->p
				.setIri(IM.CODE.getIri()))
			.property(p->p
				.setIri(IM.MATCHED_TO.getIri())
				.setInverse(true)
				.return_(n->n
					.property(p1->p1
						.setIri(RDFS.LABEL.getIri()))
					.property(p1->p1
						.setIri(IM.CODE.getIri())))));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest query2() throws QueryException{

		Query query= new Query()
			.setName("PropertiesOfShapesUsingDateOfBirth")
			.setDescription("all of the data model properties for entities that have a property df a data of birth");
		query
			.match(f->f
				.setTypeOf(SHACL.NODESHAPE.getIri())
			.property(p->p
				.setIri(SHACL.PROPERTY.getIri())
				.match(w1->w1
					.property(p1->p1
					.setIri(SHACL.PATH.getIri())
					.addIs(IM.NAMESPACE.iri+"dateOfBirth")))))
			.match(m->m
				.property(p->p
					.setIri(SHACL.PROPERTY.getIri())))
			.return_(s->s
				.property(p->p
					.setIri(SHACL.PROPERTY.getIri())
					.return_(n->n
						.property(s1->s1.setIri(SHACL.PATH.getIri()))
						.property(s1->s1.setIri(SHACL.NODE.getIri()))
						.property(s1->s1.setIri(SHACL.MINCOUNT.getIri()))
						.property(s1->s1.setIri(SHACL.MAXCOUNT.getIri()))
						.property(s1->s1.setIri(SHACL.CLASS.getIri()))
						.property(s1->s1.setIri(SHACL.DATATYPE.getIri())))));
		return new QueryRequest().setQuery(query);
	}

	public static  QueryRequest statuses(){
		Query query= new Query()
			.setName("Status subset")
			.match(s->s
				.setInstanceOf(new Node().setIri(IM.NAMESPACE.iri+"Status")
				.setDescendantsOrSelfOf(true)
				.setDescendantsOf(true)));
		return new QueryRequest().setQuery(query);
	}

	public static  QueryRequest statuses2(){
		Query query= new Query();
		query.setIri(IM.NAMESPACE.iri+"Query_GetIsas");
		QueryRequest qr= new QueryRequest();
		qr.setQuery(query);
		qr.addArgument("this", TTIriRef.iri(IM.NAMESPACE.iri+"Status"));
		return qr;
	}



	public  static QueryRequest query4() {

		Query query= new Query()
			.setName("AsthmaSubTypesCore");
		query
			.match(f ->f
				.setInstanceOf(new Node()
				.setIri(SNOMED.NAMESPACE.iri+"195967001").setDescendantsOrSelfOf(true)))
			.return_(s->s
				.property(p->p.setIri(RDFS.LABEL.getIri()))
				.property(p->p.setIri(IM.CODE.getIri())));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest getAllowableRanges() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest().setQuery(new Query().setIri(IM.NAMESPACE.iri+"Query_AllowableRanges")
			.setName("Allowable ranges"));
		qr.addArgument(new Argument().setParameter("this")
			.setValueIri(TTIriRef.iri("http://snomed.info/sct#127489000")));
		return qr;
	}

	public static QueryRequest getAllowableProperties() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest().
			addArgument(new Argument()
				.setParameter("this")
				.setValueIri(TTIriRef.iri("http://snomed.info/sct#763158003")))
			.setQuery(new Query()
				.setName("Allowable Properties for medications")
				.setIri(IM.NAMESPACE.iri+"Query_AllowableProperties")
				);
		return qr;
	}

	public static QueryRequest getConcepts() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest()
			.query(q ->q
				.setActiveOnly(true)
				.setName("Search for concepts")
				.match(f->f
					.setTypeOf(IM.CONCEPT.getIri()))
				.return_(s->s
					.property(p->p
						.setIri(RDFS.LABEL.getIri()))))
			.setTextSearch("chest pain");
		return qr;
	}

	public static  QueryRequest getIsas() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest()
			.query(q-> q
				.setName("All subtypes of an entity, active only")
				.setActiveOnly(true)
				.match(f->f.setInstanceOf(new Node().setParameter("this").setDescendantsOrSelfOf(true)))
				.return_(r->r.property(s->s.setIri(RDFS.LABEL.getIri()))));
		qr.addArgument("this",SNOMED.NAMESPACE.iri+"417928002");
		return qr;
	}

	public static QueryRequest AllowablePropertiesForCovid(){
		QueryRequest qr= new QueryRequest()
			.setTextSearch("causative");
		Query query= new Query()
				.setName("AllowablePropertiesForCovidStarting with causative")
				.setDescription("'using property domains get the allowable properties match the supertypes of this concept")
				.setActiveOnly(true);
			query
				.match(f ->f
					.setTypeOf(new Node().setIri(IM.CONCEPT.getIri())
					.setDescendantsOrSelfOf(true))
					.property(w1-> w1
						.setIri(RDFS.DOMAIN.getIri())
						.addIs(new Node().setIri(SNOMED.NAMESPACE.iri+"674814021000119106").setAncestorsOf(true))
					))
				.return_(r->r
						.property(s->s.setIri(IM.CODE.getIri()))
						.property(s->s.setIri(RDFS.LABEL.getIri())));
				qr.setQuery(query);
				return qr;
	}

	public static QueryRequest query6() {
		Query query= new Query()
			.setName("Some Barts cerner codes with context including a regex");
		query
			.return_(r->r
			.property(s->s.setIri(RDFS.LABEL.getIri()))
			.property(s->s.setIri(IM.CODE.getIri()))
			.property(s->s
				.setIri(IM.CONCEPT.getIri())
				.setInverse(true)
				.return_(n->n
				.property(s1->s1.setIri(IM.SOURCE_VALUE.getIri()))
				.property(s1->s1.setIri(IM.SOURCE_REGEX.getIri()))
				.property(s1->s1.setIri(IM.SOURCE_SYSTEM.getIri()))
				.property(s1->s1.setIri(IM.SOURCE_TABLE.getIri()))
				.property(s1->s1.setIri(IM.SOURCE_FIELD.getIri())))))
			.match(f->f
				.property(p->p
					.setIri(IM.HAS_SCHEME.getIri())
					.is(i->i
						.setIri(IM.CODE_SCHEME_BARTS_CERNER.getIri())))
			.property(p->p
					.setIri(IM.CONCEPT.getIri())
				.setInverse(true)
				.match(m1->m1
				.property(w1->w1
					.setIri(IM.SOURCE_REGEX.getIri())))));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest oralNsaids(){
		Query query= new Query()
			.setName("oral none steroidals")
			.return_(r->r
			.property(s->s.setIri(RDFS.LABEL.getIri())))
			.match(rf->rf
				.setBool(Bool.and)
				.match(f->f
						.setInstanceOf(new Node().setIri(SNOMED.NAMESPACE.iri+"763158003").setDescendantsOrSelfOf(true))
					.property(a1->a1
						.setIri(SNOMED.NAMESPACE.iri+"127489000")
						.setDescendantsOrSelfOf(true)
						.setAnyRoleGroup(true)
						.addIs(new Node().setIri(SNOMED.NAMESPACE.iri+"372665008").setDescendantsOrSelfOf(true)))
					.property(a2->a2
						.setIri(SNOMED.NAMESPACE.iri+"411116001").setDescendantsOrSelfOf(true)
						.setAnyRoleGroup(true)
						.addIs(Node.iri(SNOMED.NAMESPACE.iri+"385268001").setDescendantsOrSelfOf(true)))));

		return new QueryRequest().setQuery(query);

	}

	public static TTManager loadForms() throws IOException {
		String coreFile="C:\\Users\\david\\CloudStation\\EhealthTrust\\DiscoveryDataService\\ImportData\\DiscoveryCore\\FormQueries.json";
		TTManager manager= new TTManager();
		manager.loadDocument(new File(coreFile));
		return manager;
	}






}
