package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IMQ {
	public static final String NAMESPACE = "http://endhealth.info/imq#";
	public static final TTIriRef LATEST_EVENT = iri(NAMESPACE + "latestEvent");


	//Dataset predicates
	public static final TTIriRef HAS_QUERY = iri(NAMESPACE + "query");

	//root predicate
	public static final TTIriRef STEP= iri(NAMESPACE+"step");
	public static final TTIriRef ALIAS= iri(NAMESPACE+"alias");

	//Step predicates
	public static final TTIriRef GRAPH= iri(NAMESPACE+"graph");


	//Clause predicates
	public static final TTIriRef SELECT = iri(NAMESPACE + "select");
	public static final TTIriRef INSERT = iri(NAMESPACE + "insert");
	public static final TTIriRef CONSTRUCT = iri(NAMESPACE + "construct");
	public static final TTIriRef WHERE = iri(NAMESPACE + "where");


	//SUB clauses

	public static final TTIriRef TRIPLE = iri(NAMESPACE + "triple");
	public static final TTIriRef FILTER = iri(NAMESPACE + "filter");
	public static final TTIriRef UNION = iri(NAMESPACE + "union");
	public static final TTIriRef MINUS = iri(NAMESPACE + "minus");

	//Clause fields

	public static final TTIriRef SELECT_ITEM = iri(NAMESPACE + "selectItem");
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

	//Subject
	public static final TTIriRef SUBJECT = iri(NAMESPACE + "subject");
	public static final TTIriRef PREDICATE = iri(NAMESPACE + "predicate");
	public static final TTIriRef OBJECT = iri(NAMESPACE + "object");

	//Path types
	public static final TTIriRef IRI = iri(NAMESPACE + "Iri");
	public static final TTIriRef INVERSE = iri(NAMESPACE + "Inverse");
	public static final TTIriRef SEQUENCE = iri(NAMESPACE + "Sequence");
	public static final TTIriRef ALTERNATIVE = iri(NAMESPACE + "Alternative");
	public static final TTIriRef ONE_OR_MORE = iri(NAMESPACE + "OneOrMore");
	public static final TTIriRef VARIABLE = iri(NAMESPACE + "Variable");


	//QueryFilter types
	public static final TTIriRef IN = iri(NAMESPACE + "In");
	public static final TTIriRef NOT_IN = iri(NAMESPACE + "NotIn");
	public static final TTIriRef NOT_EXIST = iri(NAMESPACE + "notExist");
	public static final TTIriRef EXIST = iri(NAMESPACE + "exist");
	public static final TTIriRef NUMERICEXPRESSION = iri(NAMESPACE + "numericExpression");
	public static final TTIriRef RDF_EQUAL = iri(NAMESPACE + "rdfEqual");


}
