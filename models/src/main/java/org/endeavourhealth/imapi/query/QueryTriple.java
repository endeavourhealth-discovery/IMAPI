package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IMQ;

public class QueryTriple extends TTNode {

	public QueryTriple(TTNode source){
		this.setPredicateMap(source.getPredicateMap());
	}

	public QueryTriple(){
	}
	/**
	 * sets a simple variable property
	 * @param var the var without ? or $
	 * @return the triple
	 */
	public QueryTriple setVarPath(String var){
		Path path=new Path(IMQ.VARIABLE);
		this.set(IMQ.PROPERTY,path);
		path.set(IMQ.VAR, TTLiteral.literal(var));
		return this;
	}

	/**
	 * Sets a standard IRI based property
	 * @param iri  iri of the property
	 * @return the triple
	 */
	public QueryTriple setIriPath(TTIriRef iri){
		Path path=new Path(IMQ.IRI);
		this.set(IMQ.PROPERTY,path);
		path.set(IMQ.IRI,iri );
		return this;
	}

	/**
	 * Sets a sequence property path of IRIs
	 * @param sequence an array of IRIs representing the sequence
	 * @return the triple
	 */
	public QueryTriple setSequencePath(TTArray sequence){
		Path path=new Path(IMQ.SEQUENCE);
		this.set(IMQ.PROPERTY,path);
		path.set(IMQ.SEQUENCE,sequence);
		return this;
	}
	/**
	 * Sets a alternative property path of IRIs
	 * @param alternative an array of IRIs representing the sequence
	 * @return the triple
	 */
	public QueryTriple setAlternativePath(TTArray alternative){
		Path path=new Path(IMQ.ALTERNATIVE);
		this.set(IMQ.PROPERTY,path);
		path.set(IMQ.ALTERNATIVE,alternative);
		return this;
	}
	/**
	 * Sets a one or more (transitive)  property path IRI
	 * @param iri representing the transitive property
	 * @return the triple
	 */
	public QueryTriple setOneOrMorePath(TTIriRef iri){
		Path path=new Path(IMQ.ONE_OR_MORE);
		this.set(IMQ.PROPERTY,path);
		path.set(IMQ.ONE_OR_MORE,iri);
		return this;
	}
	/**
	 * Sets inverse property path
	 * @param iri representing the property to inverse
	 * @return the triple
	 */
	public QueryTriple setInversePath(TTIriRef iri){
		Path path=new Path(IMQ.INVERSE);
		this.set(IMQ.PROPERTY,path);
		path.set(IMQ.INVERSE,iri);
		return this;
	}

	/**
	 * Sets the triple block as optional (default being mandatory).
	 * If the predicate exists then the triple is optional, if null then mandatory
	 * @param optional true or false (default false)
	 */
	public void setOptional(boolean optional){
		if (optional)
			this.set(IMQ.OPTIONAL,TTLiteral.literal("true"));
		else
			this.getPredicateMap().remove(IMQ.OPTIONAL);
	}

	/**
	 * Retrieves the optional status of the triple. If the predicate optional exists then true otherwise mandatory
	 * @return true if optional predicate set, otherwise false
	 */
	public boolean getOptional(){
		return this.get(IMQ.OPTIONAL) != null;
	}

}
