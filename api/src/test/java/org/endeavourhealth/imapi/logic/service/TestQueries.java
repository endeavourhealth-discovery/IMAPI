package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class TestQueries {
	public static String ex="http://example.org/qry#";


	public static TTContext getDefaultContext(){
		TTContext context= new TTContext();
		context.add(IM.NAMESPACE,"im");
		context.add(RDFS.NAMESPACE,"rdfs");
		context.add(SNOMED.NAMESPACE,"sn");
		context.add(SHACL.NAMESPACE,"sh");
		return context;
	}

	public static QueryRequest testQuery()  {
		Query prof = new Query()
			.setIri(IM.NAMESPACE + "Q_TestQuery")
			.setName("Test for patients either aged between 18 and 65 or with diabetes with the most recent systolic in the last 6 months >150"+
				"not followed by a screening invite, excluding hypertensives")
		  .from(f->f
			.setIri(IM.NAMESPACE+"Patient")
			.where(w->w
				.setDescription("Registered for gms")
				.setIri(IM.IS_SUBSET_OF.getIri())
				.in(t->t.setSet(IM.NAMESPACE+"Q_RegisteredGMS")
					.setName("Registered for GMS services on reference date"))
				.setValueLabel("Registered for GMS on reference date"))
			.where(w->w
				.setDescription("aged 65 to 70 or diabetic")
				.setBool(Bool.or)
				.where (or->or
					.setDescription("aged between 65 and 70")
					.setIri(IM.NAMESPACE+"age")
					.range(r->r
						.from(from->from
							.setOperator(Operator.gte)
							.setValue("65"))
						.to(to->to
							.setOperator(Operator.lt)
							.setValue("70"))))
				.where(or->or
					.setDescription("Diabetic")
					.setIri(IM.NAMESPACE+"observation")
					.where(ob->ob
						.setIri(IM.NAMESPACE+"concept")
						.addIn(new TTAlias().setSet(ex+"Q_Diabetics"))
						.addIn(new TTAlias().setIri(SNOMED.NAMESPACE+"714628002").setDescendantsOf(true)))))
			.where(w->w
				.setDescription("latest BP in last 6 months is >150")
				.setIri(IM.NAMESPACE+"observation")
					.setBool(Bool.and)
					.where(ww->ww
						.setDescription("Home or office based Systolic")
						.setIri(IM.NAMESPACE+"concept")
						.setName("concept")
						.addIn(new TTAlias()
							.setIri(SNOMED.NAMESPACE+"271649006")
							.setName("Systolic blood pressure"))
						.addIn(new TTAlias()
							.setIri(IM.CODE_SCHEME_EMIS.getIri()+"1994021000006104")
							.setName("Home systolic blood pressure"))
						.setValueLabel("Office or home systolic blood pressure"))
					.where(ww->ww
						.setDescription("Last 6 months")
						.setIri(IM.NAMESPACE+"effectiveDate")
						.setAlias("LastBP")
						.setOperator(Operator.gte)
						.setValue("-6")
						.setUnit("MONTHS")
						.setRelativeTo("$referenceDate")
						.setValueLabel("last 6 months"))
					.setOrderBy(new TTAlias().setIri(IM.NAMESPACE+"effectiveDate"))
					.setCount(1)
					.then(ww->ww
						.setIri(IM.NAMESPACE+"numericValue")
						.setDescription(">150")
						.setOperator(Operator.gt)
						.setValue("150")))
			.where(w->w
				.setExclude(true)
				.setDescription("High BP not followed by screening invite")
				.setIri(IM.NAMESPACE+"observation")
				.setBool(Bool.and)
				.where(inv->inv
					.setDescription("Invited for Screening after BP")
					.setIri(IM.NAMESPACE+"concept")
					.addIn(new TTAlias().setSet(IM.NAMESPACE+"InvitedForScreening")))
				.where(after->after
					.setDescription("after high BP")
					.setIri(IM.NAMESPACE+"effectiveDate")
					.setOperator(Operator.gte)
					.setRelativeTo("LastBP")))
			.where(w->w
				.setExclude(true)
				.setDescription("not hypertensive")
				.setIri(IM.NAMESPACE+"observation")
				.where(ob1->ob1
					.setIri(IM.NAMESPACE+"concept")
					.addIn(new TTAlias().setSet(IM.NAMESPACE+"Hypertensives")
						.setName("Hypertensives")))));
		return new QueryRequest().setQuery(prof);
	}


	public static QueryRequest getAllowableSubtypes() throws IOException {
		QueryRequest qr= new QueryRequest();
		qr.setContext(getDefaultContext());
		qr.addArgument(new Argument()
			.setParameter("this")
			.setValueIri(IM.FOLDER));
		Query query= new Query();
		query.setName("Allowable child types for editor");
		query
			.from(f->f
				.where(w1->w1.setIri(IM.IS_CONTAINED_IN.getIri())
					.addIn(IM.NAMESPACE+"EntityTypes")))
			.select(s->s
				.setIri(RDFS.LABEL.getIri()))
			.select(s->s
				.setIri(SHACL.PROPERTY.getIri())
				.where(w1->w1
					.setBool(Bool.and)
					.where(a2->a2
						.setIri(SHACL.NODE.getIri())
						.addIn(new From().setVariable("$this")))
					.where(a2->a2
						.setIri(SHACL.PATH.getIri())
						.setIn(List.of(From.iri(IM.IS_CONTAINED_IN.getIri())
							,From.iri(RDFS.SUBCLASSOF.getIri()),From.iri(IM.IS_SUBSET_OF.getIri())))))
				.select(s1->s1
					.setIri(SHACL.PATH.getIri())));
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
				.select(s->s.setIri(RDFS.LABEL.getIri()))
				.from(f->f
					.setVariable("$this")
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
				.select(s->s.setIri(RDFS.LABEL.getIri()))
				.from(f->f
					.setVariable("this")
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
				.setName("paths from patient to date of birth")
				.setDepth(3)
				.setSource(IM.NAMESPACE+"Patient")
				.setTarget(IM.NAMESPACE+"dateOfBirth"));

	}

	public static QueryRequest pathToAtenolol(){
		return new QueryRequest()
			.setPathQuery(new PathQuery()
				.setName("paths from patient to Atenolol")
				.setDepth(3)
				.setSource(IM.NAMESPACE+"Patient")
				.setTarget(SNOMED.NAMESPACE+"387506000"));

	}
	public static QueryRequest pathToCSA(){
		return new QueryRequest()
			.setPathQuery(new PathQuery()
				.setName("paths from patient to common service agency")
				.setDepth(3)
				.setSource(IM.NAMESPACE+"Patient")
				.setTarget("http://endhealth.info/im#1000161000252107"));

	}

	public static QueryRequest pathToPostCode(){
		return new QueryRequest()
			.setPathQuery(new PathQuery()
				.setName("paths from patient to post code")
				.setDepth(3)
				.setSource(IM.NAMESPACE+"Patient")
				.setTarget("http://endhealth.info/im#postCode"));

	}



	public static QueryRequest query1() {
		Query query = new Query()
			.setName("FamilyHistoryExpansionObjectFormat")
			.setUsePrefixes(true);
		query
			.from(f->f
			.where(w->w
				.setIri(IM.HAS_MEMBER.getIri())
				.setInverse(true)
				.addIn(new From().setIri(IM.NAMESPACE+"VSET_FamilyHistory"))))
			.select(s->s
				.setIri(RDFS.LABEL.getIri()))
			.select(s->s
				.setIri(IM.CODE.getIri()))
			.select(s->s
				.setIri(IM.MATCHED_TO.getIri())
					.setInverse(true))
					.select(s2 ->s2
					.setIri(RDFS.LABEL.getIri()))
					.select(s2 -> s2
					.setIri(IM.CODE.getIri()));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest query2() {

		Query query= new Query()
			.setName("PropertiesOfShapesUsingDateOfBirth")
			.setDescription("all of the data model properties for entities that have a property df a data of birth")
			.setUsePrefixes(true);
		query
			.from(f->f
				.setType(SHACL.NODESHAPE.getIri())
			.where(w->w
				.setIri(SHACL.PROPERTY.getIri())
				.where(w1->w1
					.setIri(SHACL.PATH.getIri())
					.addIn(IM.NAMESPACE+"dateOfBirth"))))
			.select(s->s
				.setIri(SHACL.PROPERTY.getIri())
				.select(s1->s1.setIri(SHACL.PATH.getIri()))
				.select(s1->s1.setIri(SHACL.NODE.getIri()))
				.select(s1->s1.setIri(SHACL.MINCOUNT.getIri()))
				.select(s1->s1.setIri(SHACL.MAXCOUNT.getIri()))
				.select(s1->s1.setIri(SHACL.CLASS.getIri()))
				.select(s1->s1.setIri(SHACL.DATATYPE.getIri())));
		return new QueryRequest().setQuery(query);
	}

	public static  QueryRequest statuses(){
		Query query= new Query()
			.setName("Status subset")
			.from(s->s
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
			.from(f ->f
				.setIri(SNOMED.NAMESPACE+"195967001").setDescendantsOrSelfOf(true))
			.select(s->s.setIri(RDFS.LABEL.getIri()))
			.select(s->s.setIri(IM.CODE.getIri()));
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
				.setName("Allowable Properties for a concept")
				.setIri(IM.NAMESPACE+"Query_AllowableProperties")
				);
		return qr;
	}

	public static QueryRequest getConcepts() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest()
			.query(q ->q
				.setActiveOnly(true)
				.setName("Search for concepts")
				.from(f->f
					.setType(IM.CONCEPT.getIri()))
				.select(s->s
					.setIri(RDFS.LABEL.getIri())))
			.setTextSearch("chest pain");
		return qr;
	}

	public static  QueryRequest getIsas() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest()
			.query(q-> q
				.setName("All subtypes of an entity, active only")
				.setActiveOnly(true)
				.from(f->f.setVariable("$this").setDescendantsOrSelfOf(true))
				.select(s->s.setIri(RDFS.LABEL.getIri())));
		qr.addArgument("this",SNOMED.NAMESPACE+"417928002");
		return qr;
	}

	public static QueryRequest query5(){
		QueryRequest qr= new QueryRequest()
			.setTextSearch("causative");
		Query query= new Query()
				.setName("AllowablePropertiesForCovidStarting with causative")
				.setDescription("'using property domains get the allowable properties from the supertypes of this concept")
				.setActiveOnly(true)
				.setUsePrefixes(true);
			query
				.from(f ->f
					.setType(IM.CONCEPT.getIri())
				.where(w->w
					.setIri(IM.IS_A.getIri())
					.where(w1-> w1
						.setIri(RDFS.DOMAIN.getIri())
						.addIn(new From().setIri(SNOMED.NAMESPACE+"674814021000119106").setAncestorsOf(true))
					)))
						.select(s->s.setIri(IM.CODE.getIri()))
						.select(s->s.setIri(RDFS.LABEL.getIri()));
				qr.setQuery(query);
				return qr;
	}

	public static QueryRequest query6() {
		Query query= new Query()
			.setName("Some Barts cerner codes with context including a regex")
			.setUsePrefixes(true);

		query
			.select(s->s.setIri(RDFS.LABEL.getIri()))
			.select(s->s.setIri(IM.CODE.getIri()))
			.select(s->s
				.setIri(IM.SOURCE_CONTEXT.getIri())
				.select(s1->s1.setIri(IM.SOURCE_CODE_SCHEME.getIri()))
				.select(s1->s1.setIri(IM.SOURCE_HEADING.getIri()))
				.select(s1->s1.setIri(IM.SOURCE_SYSTEM.getIri()))
				.select(s1->s1.setIri(IM.SOURCE_TABLE.getIri()))
				.select(s1->s1.setIri(IM.SOURCE_FIELD.getIri()))
				.select(s1->s1.setIri(IM.SOURCE_SCHEMA.getIri())))
			.from(f->f
			.where(w->w
					.setIri(IM.SOURCE_CONTEXT.getIri())
						.where(w1->w1.
							setIri(IM.SOURCE_REGEX.getIri()))));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest oralNsaids(){
		Query query= new Query()
			.setName("oral none steroidals")
			.setUsePrefixes(true)
			.select(s->s.setIri(RDFS.LABEL.getIri()))
			.from(rf->rf
				.setBoolFrom(Bool.and)
				.from(f->f
						.setIri(SNOMED.NAMESPACE+"763158003").setDescendantsOrSelfOf(true)
				.where(a->a
					.setIri(IM.ROLE_GROUP.getIri())
					.setBool(Bool.and)
					.where(a1->a1
						.setIri(SNOMED.NAMESPACE+"127489000")
						.setDescendantsOrSelfOf(true)
						.addIn(From.iri(SNOMED.NAMESPACE+"372665008").setDescendantsOrSelfOf(true)))
					.where(a2->a2
						.setIri(SNOMED.NAMESPACE+"411116001").setDescendantsOrSelfOf(true)
						.addIn(From.iri(SNOMED.NAMESPACE+"385268001").setDescendantsOrSelfOf(true))))));

		return new QueryRequest().setQuery(query);

	}

	public static TTManager loadForms() throws IOException {
		String coreFile="C:\\Users\\david\\CloudStation\\EhealthTrust\\DiscoveryDataService\\ImportData\\DiscoveryCore\\FormQueries.json";
		TTManager manager= new TTManager();
		manager.loadDocument(new File(coreFile));
		return manager;
	}






}
