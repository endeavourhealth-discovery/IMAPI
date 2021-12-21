package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;

public class WhereClause extends TTNode {


	/**
	 * gets the main root entity associated with the query definition
	 * @return The iri of the main entity
	 */
	public TTIriRef getSubject() {
		return this.get(RDF.SUBJECT).asIriRef();
	}

	/**
	 * Queries should have a main or root entity iri which is used to ensure that all resources are related in some way to the main entity
	 * <p> Typically this may be a Patient but might be events or organisations or locations (households) </p>
	 * @param subject iri or variable representing the subject of the triple e.g. http://endhealth.info/im#Patient
	 * @return the query definition as amended
	 */
	public WhereClause setSubject(TTValue subject) {
		this.set(RDF.SUBJECT,subject);
		return this;
	}

	public WhereClause setPredicate(TTIriRef predicate){
		this.set(RDF.PREDICATE,predicate);
		return this;
	}

	public WhereClause setPredicate(String predicate){
		return setPredicate(TTIriRef.iri(predicate));
	}

	public WhereClause setObjectType(TTIriRef objectType) {
		this.set(RDF.OBJECT,objectType);
		if (this.get(IM.VARIABLE)==null)
			this.set(IM.VARIABLE,TTLiteral.literal(getVarFromIri(objectType)));
		return this;
	}

	public WhereClause setObjectType(String objectType) {
		return setObjectType(TTIriRef.iri(objectType));
	}

	public WhereClause setObjectVar(String variable){
		set(IM.VARIABLE,TTLiteral.literal(variable));
		return this;
	}


	public Filter setFilter(){
		Filter filter= new Filter();
		this.set(IM.FILTER,filter);
		return filter;
	}

	public WhereClause setOptional(boolean optional){
		if (optional)
			this.set(IM.OPTIONAL, TTLiteral.literal(true));
		else
			this.getPredicateMap().remove(IM.OPTIONAL);
		return this;
	}

	public WhereClause setNot(){
		this.set(IM.NOT,TTLiteral.literal(true));
		return this;
	}

	public boolean isOptional(){
		return get(IM.OPTIONAL) != null;
	}



	public TTLiteral getObjectVar(){
		return get(IM.VARIABLE).asLiteral();

	}

	public TTIriRef getPredicate(){
		return get(RDF.PREDICATE).asIriRef();
	}


	private String getVarFromIri(TTIriRef objectType) {
		String var= objectType.getIri();
		if (var.contains("#")) {
			var = var.substring(var.lastIndexOf("#") + 1);
			var= var.replace(".","").replace("/","_");
			return var;
		}
		else{
			Query.varCount= Query.varCount+1;
			var="O_"+Query.varCount;
			return var;
		}

	}










}
