package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IMQ {
	public static final String NAMESPACE = "http://endhealth.info/imq#";

	//shacl target type-
	public static final TTIriRef IMQ_TARGET = iri(NAMESPACE + "IMQTarget");

	//Dataset predicates
	public static final TTIriRef HAS_QUERY = iri(NAMESPACE + "query");


	//Clause predicates
	public static final TTIriRef SELECT = iri(NAMESPACE + "select");
	public static final TTIriRef WHERE = iri(NAMESPACE + "where");


	//SUB clauses

	public static final TTIriRef TRIPLE = iri(NAMESPACE + "triple");
	public static final TTIriRef FILTER = iri(NAMESPACE + "filter");
	public static final TTIriRef UNION = iri(NAMESPACE + "union");
	public static final TTIriRef MINUS = iri(NAMESPACE + "minus");

	//Clause fields

	public static final TTIriRef VAR = iri(NAMESPACE + "var");
	public static final TTIriRef AS = iri(NAMESPACE + "as");
	public static final TTIriRef EXPRESSION = iri(NAMESPACE + "expression");
	public static final TTIriRef GROUP_BY = iri(NAMESPACE + "groupBy");
	public static final TTIriRef ENTITY = iri(NAMESPACE + "entity");
	public static final TTIriRef PROPERTY = iri(NAMESPACE + "property");
	public static final TTIriRef VALUE = iri(NAMESPACE + "value");
	public static final TTIriRef OPTIONAL = iri(NAMESPACE + "optional");
 //Functions
 public static final TTIriRef MAX = iri(NAMESPACE + "Max");
	public static final TTIriRef MIN = iri(NAMESPACE + "Min");

	//Operators
	public static final TTIriRef EQUAL = iri(NAMESPACE + "equal");
	public static final TTIriRef LESS_OR_EQUAL = iri(NAMESPACE + "lessOrEqual");
	public static final TTIriRef GREATER_OR_EQUAL = iri(NAMESPACE + "greaterOrEqual");
	public static final TTIriRef NOT_EQUAL = iri(NAMESPACE + "notEqual");

	//Path types
	public static final TTIriRef IRI = iri(NAMESPACE + "Iri");
	public static final TTIriRef INVERSE = iri(NAMESPACE + "Inverse");
	public static final TTIriRef SEQUENCE = iri(NAMESPACE + "Sequence");
	public static final TTIriRef ALTERNATIVE = iri(NAMESPACE + "Alternative");
	public static final TTIriRef ONE_OR_MORE = iri(NAMESPACE + "OneOrMore");
	public static final TTIriRef VARIABLE = iri(NAMESPACE + "Variable");

	//Path predicate
	public static final TTIriRef PATH = iri(NAMESPACE + "path");

	//Filter types
	public static final TTIriRef IN_LIST = iri(NAMESPACE + "InList");


}
