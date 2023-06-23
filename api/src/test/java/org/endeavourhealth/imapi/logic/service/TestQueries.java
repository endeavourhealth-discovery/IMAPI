package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.*;

import java.io.File;
import java.io.IOException;

public class TestQueries {
	public static String ex="http://example.org/qry#";



	public static QueryRequest dataModelPropertyRange() throws JsonProcessingException {
		String json="{\n" +
			"\"name\":\"Data model property range\",\n" +
			"\"description\":\"get node, class or datatype value (range)  of property objects for specific data model and property\",\n" +
			"\"match\":[{\n" +
			"    \"parameter\":\"myDataModel\",\n" +
			"    \"path\":[{\"@id\":\"http://www.w3.org/ns/shacl#property\",\n" +
			"    \"match\":{\"variable\":\"shaclProperty\",\n" +
			"    \"where\":[\n" +
			"        {\n" +
			"        \"@id\":\"http://www.w3.org/ns/shacl#path\",\n" +
			"        \"in\":[{\"parameter\":\"myProperty\"}]\n" +
			"        },\n" +
			"        {\n" +
			"            \"boolWhere\":\"or\",\n" +
			"            \"where\":[\n" +
			"                {\"@id\":\"http://www.w3.org/ns/shacl#class\"},\n" +
			"                {\"@id\":\"http://www.w3.org/ns/shacl#node\"},\n" +
			"                {\"@id\":\"http://www.w3.org/ns/shacl#datatype\"}\n" +
			"                ],\n" +
			"            \"variable\":\"propType\"\n" +
			"        }]}}\n" +
			"    ]\n" +
			"    }],\n" +
			"    \"return\":[{\"nodeRef\":\"propType\",\"property\":[{\"@id\":\"http://www.w3.org/2000/01/rdf-schema#label\"}]}]\n" +
			"}";
		Query query= new ObjectMapper().readValue(json,Query.class);
		return new QueryRequest()
			.setQuery(query)
			.argument(a->a
				.setParameter("myDataModel")
				.setValueIri(TTIriRef.iri(IM.NAMESPACE+"Observation")))
			.argument(a->a
				.setParameter("myProperty")
				.setValueIri(TTIriRef.iri(IM.NAMESPACE+"concept")));

	}

	public static QueryRequest rangeSuggestion() throws QueryException{
		return new QueryRequest()
			.setContext(TTManager.createBasicContext())
			.addArgument(new Argument()
				.setParameter("this")
				.setValueIri(TTIriRef.iri(IM.NAMESPACE+"recordOwner")))
			.query(q->q
				.setName("Suggested range for a property")
				.setDescription("get node, class or datatype values (ranges)  of property objects that have 4this as their path")
				.match(m->m
					.setBoolMatch(Bool.or)
					.match(m1->m1
						.path(p->p
							.setIri(SHACL.NODE.getIri())
						.match(n->n
							.setVariable("range"))))
					.match(m1->m1
						.path(p->p
							.setIri(SHACL.CLASS.getIri())
						.match(n->n
							.setVariable("range"))))
					.match(m1->m1
						.path(p->p
							.setIri(SHACL.DATATYPE.getIri())
						.match(n->n
							.setVariable("range"))))
					.where(w->w
						.setIri(SHACL.PATH.getIri())
						.addIn(new Node().setParameter("this"))))
				.return_(s->s.setNodeRef("range")
					.property(p->p.setIri(RDFS.LABEL.getIri()))));
	}

	public static QueryRequest getShaclProperty(){
		return new QueryRequest()
			.argument(a->a.setParameter("dataModel").setValueIri(TTIriRef.iri(IM.NAMESPACE+"Patient")))
			.argument(a->a.setParameter("property").setValueIri(TTIriRef.iri(IM.NAMESPACE+"age")))
			.query(q->q
				.setName("Query - shacl property predicates for a property in a data model")
				.setDescription("Select the predicates and values and labels of the values for a given data mode and property")
				.match(m->m
					.setParameter("$dataModel")
					.path(p->p
						.setIri(SHACL.PROPERTY.getIri())
						.match(n->n.setVariable("shaclProperty")))
					.where(w->w
						.setIri(SHACL.PATH.getIri())
						.in(in->in.setParameter("$property"))))
				.return_(s->s
					.setNodeRef("shaclProperty")
					.property(p->p
						.setIri(SHACL.CLASS.getIri())
						.node(n->n
							.property(p1->p1
							.setIri(RDFS.LABEL.getIri()))))
					.property(p->p
					.setIri(SHACL.NODE.getIri())
						.node(n->n
							.property(p1->p1
								.setIri(RDFS.LABEL.getIri()))))
				.property(p->p
					.setIri(SHACL.DATATYPE.getIri())
					.node(n->n
						.property(p1->p1.setIri(RDFS.LABEL.getIri()))))
				.property(p->p
					.setIri(SHACL.MAXCOUNT.getIri()))
				.property(p->p
					.setIri(SHACL.MINCOUNT.getIri()))));
	}


	public static QueryRequest getAllowableSubtypes() throws IOException {
		QueryRequest qr= new QueryRequest();
		qr.setContext(TTManager.createBasicContext());
		qr.addArgument(new Argument()
			.setParameter("this")
			.setValueIri(IM.FOLDER));
		Query query= new Query()
			.setName("Allowable child types for a folder")
			.setIri(IM.NAMESPACE+"Query_AllowableChildTypes");
		qr.setQuery(query);
		return qr;
	}



	public static QueryRequest deleteSets(){
		QueryRequest qr= new QueryRequest()
		.addArgument(new Argument()
			.setParameter("this")
			.setValueIri(TTIriRef.iri(QR.NAMESPACE)))
			.setUpdate(new Update()
				.setIri(IM.NAMESPACE+"DeleteSets")
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
					.setParameter("this")
					.setDescendantsOrSelfOf(true)));
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
					.setParameter("this")
					.setDescendantsOrSelfOf(true)));
	}

	public static QueryRequest substanceTextSearch(){
		return new QueryRequest()
			.setTextSearch("thia")
			.addArgument(new Argument()
				.setParameter("this")
				.addToValueIriList(TTIriRef.iri("http://snomed.info/sct#421149006")))
			.setQuery(new Query().setIri(IM.NAMESPACE+"Query_GetIsas")
				.setName("substances starting with 'thia'"));
	}

	public static QueryRequest pathDobQuery(){
		return new QueryRequest()
			.setPathQuery(new PathQuery()
				.setName("paths match patient to date of birth")
				.setDepth(3)
				.setSource(IM.NAMESPACE+"Patient")
				.setTarget(IM.NAMESPACE+"dateOfBirth"));

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
				.setSource(IM.NAMESPACE+"Patient")
				.setTarget(SNOMED.NAMESPACE+"387506000"));

	}
	public static QueryRequest pathToCSA(){
		return new QueryRequest()
			.setPathQuery(new PathQuery()
				.setName("paths match patient to common service agency")
				.setDepth(3)
				.setSource(IM.NAMESPACE+"Patient")
				.setTarget("http://endhealth.info/im#1000161000252107"));

	}

	public static QueryRequest pathToPostCode(){
		return new QueryRequest()
			.setPathQuery(new PathQuery()
				.setName("paths match patient to post code")
				.setDepth(3)
				.setSource(IM.NAMESPACE+"Patient")
				.setTarget("http://endhealth.info/im#postCode"));

	}



	public static QueryRequest query1() {
		Query query = new Query()
			.setName("FamilyHistoryExpansionObjectFormat")
			.setUsePrefixes(true);
		query
			.match(f->f
			.where(w->w
				.setIri(IM.HAS_MEMBER.getIri())
				.setInverse(true)
				.addIn(new Match().setIri(IM.NAMESPACE+"VSET_FamilyHistory"))))
			.return_(s->s
				.property(p->p
				.setIri(RDFS.LABEL.getIri()))
			.property(p->p
				.setIri(IM.CODE.getIri()))
			.property(p->p
				.setIri(IM.MATCHED_TO.getIri())
				.setInverse(true)
				.node(n->n
					.property(p1->p1
						.setIri(RDFS.LABEL.getIri()))
					.property(p1->p1
						.setIri(IM.CODE.getIri())))));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest query2() throws QueryException{

		Query query= new Query()
			.setName("PropertiesOfShapesUsingDateOfBirth")
			.setDescription("all of the data model properties for entities that have a property df a data of birth")
			.setUsePrefixes(true);
		query
			.match(f->f
				.setType(SHACL.NODESHAPE.getIri())
			.path(p->p
				.setIri(SHACL.PROPERTY.getIri()))
				.where(w1->w1
					.setIri(SHACL.PATH.getIri())
					.addIn(IM.NAMESPACE+"dateOfBirth")))
			.match(m->m
				.path(p->p
					.setIri(SHACL.PROPERTY.getIri())))
			.return_(s->s
				.property(p->p
					.setIri(SHACL.PROPERTY.getIri())
					.node(n->n
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
				.setIri(IM.NAMESPACE+"Status")
				.setDescendantsOrSelfOf(true)
				.setDescendantsOf(true));
		return new QueryRequest().setQuery(query);
	}

	public static  QueryRequest statuses2(){
		Query query= new Query();
		query.setIri(IM.NAMESPACE+"Query_GetIsas");
		QueryRequest qr= new QueryRequest();
		qr.setQuery(query);
		qr.addArgument("this", TTIriRef.iri(IM.NAMESPACE+"Status"));
		return qr;
	}



	public  static QueryRequest query4() {

		Query query= new Query()
			.setName("AsthmaSubTypesCore");
		query
			.setUsePrefixes(true)
			.match(f ->f
				.setIri(SNOMED.NAMESPACE+"195967001").setDescendantsOrSelfOf(true))
			.return_(s->s
				.property(p->p.setIri(RDFS.LABEL.getIri()))
				.property(p->p.setIri(IM.CODE.getIri())));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest getAllowableRanges() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest().setQuery(new Query().setIri(IM.NAMESPACE+"Query_AllowableRanges")
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
				.setIri(IM.NAMESPACE+"Query_AllowableProperties")
				);
		return qr;
	}

	public static QueryRequest getConcepts() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest()
			.query(q ->q
				.setActiveOnly(true)
				.setName("Search for concepts")
				.match(f->f
					.setType(IM.CONCEPT.getIri()))
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
				.match(f->f.setParameter("this").setDescendantsOrSelfOf(true))
				.return_(r->r.property(s->s.setIri(RDFS.LABEL.getIri()))));
		qr.addArgument("this",SNOMED.NAMESPACE+"417928002");
		return qr;
	}

	public static QueryRequest AllowablePropertiesForCovid(){
		QueryRequest qr= new QueryRequest()
			.setTextSearch("causative");
		Query query= new Query()
				.setName("AllowablePropertiesForCovidStarting with causative")
				.setDescription("'using property domains get the allowable properties match the supertypes of this concept")
				.setActiveOnly(true)
				.setUsePrefixes(true);
			query
				.match(f ->f
					.setType(IM.CONCEPT.getIri())
					.setDescendantsOrSelfOf(true)
					.where(w1-> w1
						.setIri(RDFS.DOMAIN.getIri())
						.addIn(new Match().setIri(SNOMED.NAMESPACE+"674814021000119106").setAncestorsOf(true))
					))
				.return_(r->r
						.property(s->s.setIri(IM.CODE.getIri()))
						.property(s->s.setIri(RDFS.LABEL.getIri())));
				qr.setQuery(query);
				return qr;
	}

	public static QueryRequest query6() {
		Query query= new Query()
			.setName("Some Barts cerner codes with context including a regex")
			.setUsePrefixes(true);

		query
			.return_(r->r
			.property(s->s.setIri(RDFS.LABEL.getIri()))
			.property(s->s.setIri(IM.CODE.getIri()))
			.property(s->s
				.setIri(IM.SOURCE_CONTEXT.getIri())
				.node(n->n
				.property(s1->s1.setIri(IM.SOURCE_CODE_SCHEME.getIri()))
				.property(s1->s1.setIri(IM.SOURCE_HEADING.getIri()))
				.property(s1->s1.setIri(IM.SOURCE_SYSTEM.getIri()))
				.property(s1->s1.setIri(IM.SOURCE_TABLE.getIri()))
				.property(s1->s1.setIri(IM.SOURCE_FIELD.getIri()))
				.property(s1->s1.setIri(IM.SOURCE_SCHEMA.getIri())))))
			.match(f->f
			.path(p->p
					.setIri(IM.SOURCE_CONTEXT.getIri()))
				.where(w1->w1
					.setIri(IM.SOURCE_REGEX.getIri())));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest oralNsaids(){
		Query query= new Query()
			.setName("oral none steroidals")
			.setUsePrefixes(true)
			.return_(r->r
			.property(s->s.setIri(RDFS.LABEL.getIri())))
			.match(rf->rf
				.setBoolMatch(Bool.and)
				.match(f->f
						.setIri(SNOMED.NAMESPACE+"763158003").setDescendantsOrSelfOf(true)
					.where(a1->a1
						.setIri(SNOMED.NAMESPACE+"127489000")
						.setDescendantsOrSelfOf(true)
						.setAnyRoleGroup(true)
						.addIn(Match.iri(SNOMED.NAMESPACE+"372665008").setDescendantsOrSelfOf(true)))
					.where(a2->a2
						.setIri(SNOMED.NAMESPACE+"411116001").setDescendantsOrSelfOf(true)
						.setAnyRoleGroup(true)
						.addIn(Match.iri(SNOMED.NAMESPACE+"385268001").setDescendantsOrSelfOf(true)))));

		return new QueryRequest().setQuery(query);

	}

	public static TTManager loadForms() throws IOException {
		String coreFile="C:\\Users\\david\\CloudStation\\EhealthTrust\\DiscoveryDataService\\ImportData\\DiscoveryCore\\FormQueries.json";
		TTManager manager= new TTManager();
		manager.loadDocument(new File(coreFile));
		return manager;
	}






}
