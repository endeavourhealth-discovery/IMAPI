package org.endeavourhealth.imapi.query;

/**
 * Node that handles the property path patterns such as sequence alternatives
 * or one or more
 */

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IMQ;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.Arrays;

/**
 * Property path object for a triple block property
 */
public class Path extends TTNode {
	private static TTIriRef[] types= {IMQ.VARIABLE,IMQ.IRI,
		  IMQ.SEQUENCE,IMQ.INVERSE,IMQ.ALTERNATIVE,IMQ.ONE_OR_MORE};
	/**
	 * Create property with path type
	 * @param type
	 */
	public Path(TTIriRef type){
		if (!Arrays.asList(types).contains(type))
			throw new IllegalArgumentException("Invalid property path type");
		set(RDF.TYPE,type);
	}

	/**
	 * Creates a property of a type and path
	 * @param type the property path type
	 * @param path the property path
	 */
	public Path(TTIriRef type, TTValue path){
		this(type);
		set(IMQ.PATH,path);

	}

	/**
	 * instantiates a path object with a TTNode
	 * @param source TTNode containing the path attributes
	 */
	public Path(TTNode source){
		this.setPredicateMap(source.getPredicateMap());
	}

	/**
	 * returns the type of property path e.g. simple var, iri, sequence alternative
	 * @return the iri of the path type
	 */
	public TTIriRef getPathType(){
		return get(RDF.TYPE).asIriRef();
	}

	/**
	 * returns property path value e.g. simple var, iri, array
	 * @return the value of the path
	 */
	public TTValue getPath(){
		return get(IMQ.PATH);
	}

}
