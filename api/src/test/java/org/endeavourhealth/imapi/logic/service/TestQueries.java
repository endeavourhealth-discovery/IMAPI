package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.iml.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

public class TestQueries {


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
			.with(f->f.addType(new TTAlias(SHACL.NODESHAPE)))
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


	public  static QueryRequest query4() {

		Query query= new Query()
			.setName("AsthmaSubTypesCore");
		query
			.setUsePrefixes(true)
			.with(f ->f
				.setInstance(new TTAlias().setIri(SNOMED.NAMESPACE+"195967001").setIncludeSubtypes(true)))
			.select(RDFS.LABEL.getIri())
			.select(IM.CODE.getIri());
		return new QueryRequest().setQuery(query);
	}

	public static QueryRequest getAllowableRanges() throws JsonProcessingException {
		QueryRequest qr = new QueryRequest()
			.setQuery((Query) new Query()
				.setName("Allowable Ranges for a property")
				.setDescription("'using property domains get the allowable properties from the supertypes of this concept")
				.setActiveOnly(true)
				.with(f -> f
					.addType(IM.CONCEPT))
				.select(IM.CODE.getIri())
				.select(RDFS.LABEL.getIri())
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
				.with(f ->f
					.addType(TTAlias.iri(IM.CONCEPT.getIri()).setIncludeSubtypes(true)))
				.select(IM.CODE.getIri())
				.select(RDFS.LABEL.getIri())
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
				.with(w->w
					.addType(TTAlias.iri(IM.CONCEPT.getIri())))
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
				.with(w->w.setInstance(new TTAlias().setVariable("$this").setIncludeSubtypes(true)))
				.select(s->s.setProperty(RDFS.LABEL)));
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
				.with(f ->f
					.addType(IM.CONCEPT))
				.select(IM.CODE.getIri())
				.select(RDFS.LABEL.getIri())
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
			.select(RDFS.LABEL)
			.select(IM.CODE)
			.select(s->s
				.setProperty(IM.SOURCE_CONTEXT)
				.select(IM.SOURCE_CODE_SCHEME)
				.select(IM.SOURCE_HEADING)
				.select(IM.SOURCE_SYSTEM)
				.select(IM.SOURCE_TABLE)
				.select(IM.SOURCE_FIELD)
				.select(IM.SOURCE_SCHEMA))
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
			.select(RDFS.LABEL)
			.where(w->w
				.and(a->a
					.setIs(TTAlias.iri(SNOMED.NAMESPACE+"763158003").setIncludeSubtypes(true)))
				.and(a->a
					.setPath(IM.ROLE_GROUP.getIri())
					.and(a1->a1
						.setProperty(TTAlias.iri(SNOMED.NAMESPACE+"127489000").setIncludeSubtypes(true))
						.setIs(TTAlias.iri(SNOMED.NAMESPACE+"372665008").setIncludeSubtypes(true)))
					.and(a2->a2
						.setProperty(TTAlias.iri(SNOMED.NAMESPACE+"411116001").setIncludeSubtypes(true))
						.setIs(TTAlias.iri(SNOMED.NAMESPACE+"385268001").setIncludeSubtypes(true)))));

		return new QueryRequest().setQuery(query);

	}




}
