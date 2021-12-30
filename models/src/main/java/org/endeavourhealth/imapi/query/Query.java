package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializerV2;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
	* A serializable query object containing a sequence of clauses resulting in the definition of a  data set output.
	* <p> In effect a high level process model taylored to the health query ues cases</p>
	*/

@JsonPropertyOrder({"prefix","operator","where","clause"})
public class Query  extends Clause implements TTValue, Serializable {
	private static final Map<String,String> nsPrefix= new HashMap<>();
	private static final Map<String,String> prefix= new HashMap<>();

	public Query(){
		addPrefix(IM.NAMESPACE,"im");
		addPrefix(RDFS.NAMESPACE,"rdfs");
		addPrefix(RDF.NAMESPACE,"rdf");
	}

	public static void addPrefix(String ns,String px){
		nsPrefix.put(ns,px);
		prefix.put(px,ns);
	}



	public Map<String,String> getPrefix(){
		return prefix;
	}


	public static String getShort(String iri) {
		if (iri==null)
			return null;
		int end = Integer.max(iri.lastIndexOf("/"), iri.lastIndexOf("#"));
		String path = iri.substring(0, end + 1);
		String prefix = nsPrefix.get(path);
		if (prefix == null)
			return iri;
		else
		if (end<iri.length()-1)
			return prefix + ":" + iri.substring(end + 1);
		else if(end == iri.length()-1)
			return prefix + ":";
		else return iri;

	}

}
