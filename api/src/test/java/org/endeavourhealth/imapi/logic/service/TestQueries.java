package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.iml.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
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
				.setIs(new TTAlias(IM.NAMESPACE+"VSET_FamilyHistory")))
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
				.setInstance(new TTAlias(SNOMED.NAMESPACE+"195967001").setIncludeSubtypes(true)))
			.select(RDFS.LABEL.getIri())
			.select(IM.CODE.getIri());
		return new QueryRequest().setQuery(query);
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
						.setIs(new TTAlias(SNOMED.NAMESPACE+"674814021000119106").setIncludeSupertypes(true))
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


}
