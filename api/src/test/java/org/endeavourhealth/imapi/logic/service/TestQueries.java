package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.imq.PathQuery;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.SourceType;
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
			.from(f->f
				.where(rw->rw
			.setBool(Bool.and)
			.where(w->w
					.setIri(IM.IS_CONTAINED_IN.getIri())
					.addIn(IM.NAMESPACE+"EntityTypes"))
			.where(a->a
					.setIri(SHACL.PROPERTY.getIri())
				.setBool(Bool.and)
					.where(w1->w1
							.setIri(SHACL.CLASS.getIri())
							.addIn(new TTAlias().setVariable("this")))
				.where(a2->a2
							.setIri(SHACL.PATH.getIri())
						.setIn(List.of(TTAlias.iri(IM.IS_CONTAINED_IN.getIri()).setVariable("predicate")
								,TTAlias.iri(RDFS.SUBCLASSOF.getIri()),TTAlias.iri(IM.IS_SUBSET_OF.getIri())))))))
			.select(s->s
				.setIri(RDFS.LABEL.getIri()))
			.select(s->s
				.setIri(SHACL.PROPERTY.getIri())
				.where(w->w
					.setBool(Bool.and)
					.where(a->a
						.setIri(SHACL.PATH.getIri())
						.setIn(List.of(TTAlias.iri(IM.IS_CONTAINED_IN.getIri()).setAlias("predicate")
							,TTAlias.iri(RDFS.SUBCLASSOF.getIri()),TTAlias.iri(IM.IS_SUBSET_OF.getIri()))))
					.where(a->a
						.setIri(SHACL.CLASS.getIri())
						.addIn(new TTAlias().setVariable("this"))))
				.select(s1->s1
					.setIri(SHACL.PATH.getIri())));
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
			.from(f->f
			.where(w->w
				.setIri(IM.HAS_MEMBER.getIri())
				.setInverse(true)
				.addIn(new TTAlias().setIri(IM.NAMESPACE+"VSET_FamilyHistory"))))
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
				.setIri(SHACL.NODESHAPE.getIri())
				.setSourceType(SourceType.type)
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
			.select(s->s.setIri(RDFS.LABEL.getIri()))
			.select(s->s.setIri(IM.CODE.getIri()));
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest getAllowableRanges() throws JsonProcessingException {
		QueryRequest qr = new QueryRequest()
			.setQuery((Query) new Query()
				.setName("Allowable Ranges for a property")
				.setDescription("'using property domains get the allowable properties from the supertypes of this concept")
				.setActiveOnly(true)
				.select(s->s.setIri(IM.CODE.getIri()))
				.select(s->s.setIri(RDFS.LABEL.getIri()))
				.from(f -> f
					.setIri(IM.CONCEPT.getIri())
					.setSourceType(SourceType.type)
				.where(w -> w
					.setIri(RDFS.RANGE.getIri())
					.setInverse(true)
					.addIn(new TTAlias().setVariable("$this").setIncludeSupertypes(true).setIncludeSubtypes(true))
				)));
		qr.addArgument("this","http://snomed.info/sct#42752001");
		return qr;
	}

	public static QueryRequest getAllowableProperties() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest().query(q->q
				.setName("Allowable Properties for a concept")
				.setDescription("'using property domains get the allowable properties from the supertypes of this concept")
				.setActiveOnly(true)
				.select(s->s.setIri(IM.CODE.getIri()))
				.select(s->s.setIri(RDFS.LABEL.getIri()))
				.from(f ->f
					.setIri(IM.CONCEPT.getIri())
					.setSourceType(SourceType.type)
					.setIncludeSubtypes(true)
				.where(w->w
					.setIri(RDFS.DOMAIN.getIri())
					.addIn(new TTAlias().setVariable("$this").setIncludeSupertypes(true))
				)));
		qr.addArgument("this",SNOMED.NAMESPACE+"840539006");
		return qr;
	}

	public static QueryRequest getConcepts() throws JsonProcessingException {
		QueryRequest qr= new QueryRequest()
			.query(q ->q
				.setActiveOnly(true)
				.setName("Search for concepts")
				.from(f->f
					.setIri(IM.CONCEPT.getIri())
					.setSourceType(SourceType.type))
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
				.from(f->f.setVariable("$this").setIncludeSubtypes(true))
				.select(s->s.setIri(RDFS.LABEL.getIri())));
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
					.setSourceType(SourceType.type)
					.setIri(IM.CONCEPT.getIri())
				.where(w->w
					.setIri(IM.IS_A.getIri())
					.where(w1-> w1
						.setIri(RDFS.DOMAIN.getIri())
						.addIn(new TTAlias().setIri(SNOMED.NAMESPACE+"674814021000119106").setIncludeSupertypes(true))
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
				.setBool(Bool.and)
				.from(f->f
						.setIri(SNOMED.NAMESPACE+"763158003").setIncludeSubtypes(true)
				.where(a->a
					.setIri(IM.ROLE_GROUP.getIri())
					.setBool(Bool.and)
					.where(a1->a1
						.setIri(SNOMED.NAMESPACE+"127489000")
						.setIncludeSubtypes(true)
						.addIn(TTAlias.iri(SNOMED.NAMESPACE+"372665008").setIncludeSubtypes(true)))
					.where(a2->a2
						.setIri(SNOMED.NAMESPACE+"411116001").setIncludeSubtypes(true)
						.addIn(TTAlias.iri(SNOMED.NAMESPACE+"385268001").setIncludeSubtypes(true))))));

		return new QueryRequest().setQuery(query);

	}




}
