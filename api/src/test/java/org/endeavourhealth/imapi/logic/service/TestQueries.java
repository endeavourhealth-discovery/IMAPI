package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.PathQuery;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.iml.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.ECLToIML;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.util.List;
import java.util.zip.DataFormatException;

public class TestQueries {

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


	public static QueryRequest allowableChildTypes(){
		Query query= new Query();
		query.setName("Allowable child types for editor");
		query
			.where(w->w
				.and(a->a
					.setProperty(IM.IS_CONTAINED_IN)
					.setIs(IM.NAMESPACE+"EntityTypes"))
				.and(a->a
					.setProperty(SHACL.PROPERTY)
					.where(w1->w1
						.and(a2->a2
							.setProperty(SHACL.CLASS)
							.setIs(new TTAlias().setVariable("this")))
						.and(a2->a2
							.setProperty(SHACL.PATH)
						.setIn(List.of(TTAlias.iri(IM.IS_CONTAINED_IN.getIri()).setAlias("predicate")
								,TTAlias.iri(RDFS.SUBCLASSOF.getIri()),TTAlias.iri(IM.IS_SUBSET_OF.getIri())))))))
			.select(s->s
				.setProperty(RDFS.LABEL))
			.select(s->s
				.setProperty(SHACL.PROPERTY)
				.where(w->w
					.and(a->a
						.setProperty(SHACL.PATH)
						.setIn(List.of(TTAlias.iri(IM.IS_CONTAINED_IN.getIri()).setAlias("predicate")
							,TTAlias.iri(RDFS.SUBCLASSOF.getIri()),TTAlias.iri(IM.IS_SUBSET_OF.getIri()))))
					.and(a->a
						.setProperty(SHACL.CLASS)
						.setIs(new TTAlias().setVariable("this"))))
				.select(s1->s1
					.setProperty(SHACL.PATH)));
		QueryRequest qr= new QueryRequest()
			.setQuery(query)
			.addArgument("this",IM.FOLDER);
		return qr;

	}
	public static QueryRequest query1() {
		Query query = new Query()
			.setName("FamilyHistoryExpansionObjectFormat")
			.setUsePrefixes(true);
		query
			.where(w->w
				.setProperty(new TTAlias(IM.HAS_MEMBER).setInverse(true))
				.setIs(new TTAlias().setIri(IM.NAMESPACE+"VSET_FamilyHistory")))
			.select(s->s
				.setProperty(RDFS.LABEL.getIri()))
			.select(s->s
				.setProperty(IM.CODE.getIri()))
			.select(s->s
				.setProperty(new TTAlias().setIri(IM.MATCHED_TO.getIri())
					.setInverse(true))
					.select(s2 ->s2
					.setProperty(RDFS.LABEL.getIri()))
					.select(s2 -> s2
					.setProperty(IM.CODE.getIri())));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest query2() {

		Query query= new Query()
			.setName("PropertiesOfShapesUsingDateOfBirth")
			.setDescription("all of the data model properties for entities that have a property df a data of birth")
			.setUsePrefixes(true);
		query
			.from(f->f.setType(SHACL.NODESHAPE))
			.select(s->s
				.setProperty(SHACL.PROPERTY.getIri())
					.select(s1->s1.setProperty(SHACL.PATH))
					.select(s1->s1.setProperty(SHACL.NODE))
					.select(s1->s1.setProperty(SHACL.MINCOUNT))
					.select(s1->s1.setProperty(SHACL.MAXCOUNT))
					.select(s1->s1.setProperty(SHACL.CLASS))
							.select(s1->s1.setProperty(SHACL.DATATYPE)))
			.where(w->w
				.setProperty(SHACL.PROPERTY)
				.where(w1->w1
					.setProperty(SHACL.PATH)
					.setIs(IM.NAMESPACE+"dateOfBirth")));
		return new QueryRequest().setQuery(query);
	}

	public static  QueryRequest statuses(){
		Query query= new Query()
			.setName("Status subset")
			.from(s->s
				.setIri(IM.NAMESPACE+"Status")
				.setIncludeSubtypes(true)
				.setExcludeSelf(true));
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
				.setIri(SNOMED.NAMESPACE+"195967001").setIncludeSubtypes(true))
			.select(s->s.setProperty(RDFS.LABEL))
			.select(s->s.setProperty(IM.CODE));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest getAllowableRanges() throws JsonProcessingException {
		QueryRequest qr = new QueryRequest()
			.setQuery((Query) new Query()
				.setName("Allowable Ranges for a property")
				.setDescription("'using property domains get the allowable properties from the supertypes of this concept")
				.setActiveOnly(true)
				.from(f -> f
					.setType(IM.CONCEPT))
				.select(s->s.setProperty(IM.CODE))
				.select(s->s.setProperty(RDFS.LABEL))
				.where(w -> w
					.setProperty(new TTAlias(RDFS.RANGE).setInverse(true))
					.setIs(new TTAlias().setVariable("$this").setIncludeSupertypes(true).setIncludeSubtypes(true))
				));
		qr.addArgument("this","http://snomed.info/sct#42752001");
		return qr;
	}

	public static QueryRequest getAllowableProperties() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest().query(q->q
				.setName("Allowable Properties for a concept")
				.setDescription("'using property domains get the allowable properties from the supertypes of this concept")
				.setActiveOnly(true)
				.from(f ->f
					.setType(TTAlias.iri(IM.CONCEPT.getIri()).setIncludeSubtypes(true)))
				.select(s->s.setProperty(IM.CODE))
				.select(s->s.setProperty(RDFS.LABEL))
				.where(w->w
					.setProperty(new TTAlias(RDFS.DOMAIN))
					.setIs(new TTAlias().setVariable("$this").setIncludeSupertypes(true))
				));
		qr.addArgument("this",SNOMED.NAMESPACE+"840539006");
		return qr;
	}

	public static QueryRequest getConcepts() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest()
			.query(q ->q
				.setActiveOnly(true)
				.setName("Search for concepts")
				.from(w->w
					.setType(TTAlias.iri(IM.CONCEPT.getIri())))
				.select(s->s
					.setProperty(RDFS.LABEL)))
			.setTextSearch("chest pain");
		return qr;
	}

	public static  QueryRequest getIsas() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest()
			.query(q-> q
				.setName("All subtypes of an entity, active only")
				.setActiveOnly(true)
				.from(w->w.setVariable("$this").setIncludeSubtypes(true))
				.select(s->s.setProperty(RDFS.LABEL)));
		qr.addArgument("this",SNOMED.NAMESPACE+"417928002");
		return qr;
	}

	public static QueryRequest getLegPain() throws DataFormatException {
		QueryRequest qr= new QueryRequest();
		String ecl= "* :  { 363698007 |Finding site (attribute)| = 61685007 |Lower limb structure (body structure)| }";
		ECLToIML eclToIML= new ECLToIML();
		Query query= eclToIML.getQueryFromECL(ecl);
		query.setName("Concept With Site being LowerLimb");
		qr.setQuery(query);
		return qr;

	}

	public static QueryRequest complexECL() throws DataFormatException {
		QueryRequest qr= new QueryRequest();
		String ecl= "<< 10601006 OR (<< 29857009 MINUS (<<102588006 OR (<<29857009:263502005 |Clinical course (attribute)| = 424124008 |Sudden onset AND/OR short duration (qualifier value)|)))";
		ECLToIML eclToIML= new ECLToIML();
		Query query= eclToIML.getQueryFromECL(ecl);
		query.setName("Pain in lower limb OR Chest pain minus (Chest wall pain or chest pain with sudden onset");
		query.setActiveOnly(true);
		qr.setQuery(query);
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
					.setType(IM.CONCEPT))
				.select(s->s.setProperty(IM.CODE))
				.select(s->s.setProperty(RDFS.LABEL))
				.where(w->w
					.setProperty(IM.IS_A)
					.where(w1-> w1
						.setProperty(new TTAlias(RDFS.DOMAIN))
						.setIs(new TTAlias().setIri(SNOMED.NAMESPACE+"674814021000119106").setIncludeSupertypes(true))
					));
				qr.setQuery(query);
				return qr;
	}

	public static QueryRequest query6() {
		Query query= new Query()
			.setName("Some Barts cerner codes with context including a regex")
			.setUsePrefixes(true);

		query
			.select(s->s.setProperty(RDFS.LABEL))
			.select(s->s.setProperty(IM.CODE))
			.select(s->s
				.setProperty(IM.SOURCE_CONTEXT)
				.select(s1->s1.setProperty(IM.SOURCE_CODE_SCHEME))
				.select(s1->s1.setProperty(IM.SOURCE_HEADING))
				.select(s1->s1.setProperty(IM.SOURCE_SYSTEM))
				.select(s1->s1.setProperty(IM.SOURCE_TABLE))
				.select(s1->s1.setProperty(IM.SOURCE_FIELD))
				.select(s1->s1.setProperty(IM.SOURCE_SCHEMA)))
			.where(w->w
					.setProperty(IM.SOURCE_CONTEXT)
						.where(w1->w1.
							setProperty(IM.SOURCE_REGEX)));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest oralNsaids(){
		Query query= new Query()
			.setName("oral none steroidals")
			.setUsePrefixes(true)

			.select(s->s.setProperty(RDFS.LABEL))
			.where(w->w
				.and(a->a
					.from(f->f
						.setIri(SNOMED.NAMESPACE+"763158003").setIncludeSubtypes(true)))
				.and(a->a
					.setPathTo(IM.ROLE_GROUP.getIri())
					.and(a1->a1
						.setProperty(TTAlias.iri(SNOMED.NAMESPACE+"127489000").setIncludeSubtypes(true))
						.setIs(TTAlias.iri(SNOMED.NAMESPACE+"372665008").setIncludeSubtypes(true)))
					.and(a2->a2
						.setProperty(TTAlias.iri(SNOMED.NAMESPACE+"411116001").setIncludeSubtypes(true))
						.setIs(TTAlias.iri(SNOMED.NAMESPACE+"385268001").setIncludeSubtypes(true)))));

		return new QueryRequest().setQuery(query);

	}




}
